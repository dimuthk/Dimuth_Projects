module Main where
import Text.Printf
import CoinToss (simCoinToss)
import Estimator (Estimator (..), getMean, getVar, getNumTrials, getHalfWidth)
import GSMP (TermCriterion (..))
import Probability (Probability, getP)

printResult :: Estimator -> IO ()
printResult est = do
  _ <- printf "\n\n Statistics \n\n"
  _ <- printf "Number of simulation replications: %d \n" (k est)
  _ <- printf "Mean: %.4f \n" (getMean est)
  _ <- printf "Standard Deviation: %.4f \n" (sqrt (getVar est) :: Double)
  _ <- printf "%.0f %% Confidence Interval: [%.4f, %.4f] \n" ((getP (conf est)) * 100) (mean - hw) (mean + hw)
  _ <- printf "Number of repetitions required for estimating mean to within 0.1 %%: %d \n"
       (getNumTrials est 0.001)
  return ()
  where
    mean = getMean est
    hw = getHalfWidth est

main :: IO ()
main = do
  est <- simCoinToss Nothing (NumReps 30000) 
  printResult est
  return ()
