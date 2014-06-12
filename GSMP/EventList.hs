{-# LANGUAGE GADTs, NoMonomorphismRestriction, BangPatterns #-}
module EventList ( EventList (..)
                 , schedule
                 , nextEvent
                 , initEL
                 ) where
import qualified Data.PSQueue as PQ
import Debug.Trace
import System.Random (RandomGen, random, StdGen)
import Data.Maybe (fromJust)
import Control.DeepSeq
import Probability (Probability, mkP)
import StateSpace (Event (..), Time)

-- Event List to retrieve next event, and schedule/cancel events
data EventList a where
  EventList :: (Show a, Eq a, Ord a) =>
               { eventPQ :: PQ.PSQ a Time
               , sysTime :: !Time
               } -> EventList a

instance (Show a, Ord a, Eq a) => Show (EventList a) where
  show el = "EventList: \n" ++ "EventPQ: " ++ show (eventPQ el)

-- get the next event from the event list
nextEvent :: (Show a, Eq a, Ord a) => EventList a -> (a, Time, EventList a)
nextEvent eList =
  let
    (eventID, elapsedTime, pq') = case PQ.minView (eventPQ eList) of
      Just (k PQ.:-> v, pq) -> (k, v, pq)
      Nothing -> error "Empty event list. Cannot get next event."
    modList = EventList pq' elapsedTime
  in (eventID, (elapsedTime - sysTime eList), modList)

-- initialize the event list given the initial state
initEL :: (RandomGen g, Show a, Eq a, Ord a, NFData a) => [Event a] -> g -> (EventList a, g)
initEL events g =
  let eList = EventList (PQ.empty :: (Ord a) => PQ.PSQ a Time) 0.0
      (eList', g') = schedule eList events [] g
  in (eList', g')


generateRandoms :: (RandomGen g) => Int -> g  -> ([Double], g)
generateRandoms n g = foldl (\(rands, gen) _ -> let (!rand, gen') = random gen
                                                in ((rand:rands), gen')) ([], g) (replicate n 0)

-- schedule the new events and cancel inactive events from the event list
schedule :: (RandomGen g, Show a, Eq a, Ord a, NFData a) => EventList a -> [Event a] -> [a]
            -> g -> (EventList a, g)
schedule eList newEvents oldEvents g =
  let
    pq = eventPQ eList
    (rands, g') = generateRandoms (length newEvents) g
    priorities = zipWith ($) (map clockFn newEvents) (map mkP rands)
    newEv = zipWith (,) (map eID newEvents) priorities
    prunedPQ = foldl (\el ev -> PQ.delete ev el) pq oldEvents
    finPQ = newEv `deepseq` foldl (\el ev -> PQ.insert (fst ev) (snd ev) el) prunedPQ newEv
  in (EventList finPQ (sysTime eList), g')
