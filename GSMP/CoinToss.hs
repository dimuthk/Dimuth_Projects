-- Coin Toss simulation. 
-- Consider a gambling game in which a fair coin is repeatedly flipped until |#heads - #tails| = 3. The player receives $8.99 at the end of the game but must pay $1 for each coin flip. No quitting in the middle of the game. Is this game a good bet over the long run? That is, what is the expected reward? This is a simple example of decision-making under uncertainty (to play or not to play).


{-# LANGUAGE BangPatterns #-}
module CoinToss (simCoinToss) where
import Numeric.Probability.Distribution (cons)
import Probability (Probability, mkP)
import Estimator (Estimator)
import GSMP
import StateSpace (StateSpace (..), Event (..), Time, Acc, Seed, MaybeDist)


-- Cointoss event. No other events. Clock deterministically increments by one tick
e :: Event String
e = Event { eID = "Coin Toss", clockFn = \ _ -> 1.0 }

-- Heads cnt, tails cnt
type Result = StateSpace (Integer, Integer) 

-- Initial state
initState :: Result
initState = StateSpace (0, 0)
  
-- equal probability of incrementing heads or tails by one
trans :: Result -> String -> MaybeDist Result
trans (StateSpace (!x, !y)) _
  = return $ cons [(StateSpace (x+1, y), 0.5), (StateSpace (x, y+1), 0.5)]

-- All states have single associated event (cointoss).
activeEv :: Result -> [Event String]
activeEv _ = [e]

canceledEv :: Result -> [String]
canceledEv _ = []

-- terminate simulation when 3 more heads than tails
termFn :: Result -> Time -> Bool
termFn (StateSpace (x, y)) _ 
  | abs (x - y) > 3 = True
  | otherwise = False

-- return accuracy-representative values. this simulation does not require memory
qof :: Result -> Time -> Acc -> Acc
qof (StateSpace (!x, !y)) _ _ = [fromInteger x, fromInteger y]

-- process accuracy given acc numbers
processVal :: Acc -> Double
processVal acc = 8.99 - sum acc 

-- desired confidence interval
conf :: Probability
conf = mkP 0.95

simCoinToss :: Seed -> TermCriterion -> IO Estimator
simCoinToss seed termCond =
  simulate initState activeEv canceledEv trans qof [0.0, 0.0] termFn processVal termCond seed conf 

