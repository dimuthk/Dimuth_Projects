{-# LANGUAGE GADTs, StandaloneDeriving, ParallelListComp, NoMonomorphismRestriction #-}

module StateSpace ( Event (..)
                  , StateSpace (..)
                  , ActiveEvents
                  , Transition
                  , StateCond
                  , StateQty
                  , MaybeDist
                  ) where

import Numeric.Probability.Distribution (T (..), decons, cons)
import Probability (Probability (..))

data Event = Event { eID :: String
                   , clockFn :: Probability -> Float
                   }

-- Using Generalized ADTs so as to impose equality class constraint
-- data StateSpace s where
--   StateSpace :: (Eq s, Show s) =>
--                 { curState :: s } -> StateSpace s

data StateSpace s = StateSpace {curState :: s} deriving (Show, Eq)

instance Functor StateSpace where
  fmap f = StateSpace . f . curState

    
-- Convenient type declarations to be used by Client 
type ActiveEvents s = StateSpace s -> [Event]
type Transition s = StateSpace s -> Event -> MaybeDist (StateSpace s)
type StateCond s = StateSpace s -> Bool -- Can be used to specify terminating condition
type StateQty s = StateSpace s -> Float -- Can be used to specify the quantity being calculated

type MaybeDist s = Maybe (T Double s)
    
-- Simple example usage (client side)

-- Use the identity function to map from [0, 1] random numbers to [0, 1]
unif :: Probability -> Float
unif = getP

e :: Event
e = Event { eID = "arrival", clockFn = unif }
      
type Board = StateSpace (Int, Int) 

trans :: Board -> Event -> MaybeDist Board
trans (StateSpace (x, y)) (Event event _)
  | x `elem` [1..7] && y `elem` [2..8] && event == "arrival" = 
     return $ cons [(StateSpace (x+1, y), 0.5), (StateSpace (x+1, y-1), 0.5)]
  | otherwise = Nothing

