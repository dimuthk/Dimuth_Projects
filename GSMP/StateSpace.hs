{-# LANGUAGE GADTs, BangPatterns, GeneralizedNewtypeDeriving #-}

module StateSpace ( Event (..)
                  , StateSpace (..)
                  , Time
                  , ActiveEvents
                  , CanceledEvents
                  , Transition
                  , StateCond
                  , StateQty
                  , ProcessEst
                  , MaybeDist
                  , Seed
                  , Acc
                  ) where

import Numeric.Probability.Distribution (T (..))
import Probability (Probability)
import Control.DeepSeq

type Seed = Maybe Int
type Time = Double

-- Event description. Contains identification no and inverse probability function to retrieve time
data Event a where
  Event :: (Ord a, Eq a, Show a, NFData a) => 
           { eID :: !a
           , clockFn :: Probability -> Time
           } -> Event a

newtype StateSpace s = StateSpace {curState :: s} deriving (Show, Eq, NFData)

instance Functor StateSpace where
  fmap f = StateSpace . f . curState

    
-- Convenient type declarations to be used by Client 
type ActiveEvents s a = StateSpace s -> [Event a]
type CanceledEvents s a = StateSpace s -> [a]
type Transition s a = StateSpace s -> a -> MaybeDist (StateSpace s)
type StateCond s = StateSpace s -> Time ->  Bool -- Can be used to specify terminating condition
type Acc = [Double]
type StateQty s = StateSpace s -> Time -> Acc -> Acc -- Can be used to specify the quantity being calculated
type ProcessEst = [Double] -> Double
type MaybeDist s = Maybe (T Double s)
    
