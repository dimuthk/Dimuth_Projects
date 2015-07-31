{-# LANGUAGE BangPatterns #-}
module Estimator ( Estimator (..)
                 , getNumTrials
                 , initEst
                 , updateEst
                 , getRelError
                 , getVar
                 , getMean
                 , getHalfWidth
                 , getCIRange
                 ) where
import Probability (Probability, getP)
import Data.Number.Erf (invnormcdf)

--k : number of values processed so far
--sRun/vRun : sum of values/variance
--conf : desired confidence interval
data Estimator = Estimator { k :: Integer
                           , sRun :: Double
                           , vRun :: Double
                           , conf :: Probability
                           }

--initialize estimate. confidence interval is given by user
initEst :: Probability -> Estimator
initEst confidence =
  Estimator { k = 0
            , sRun = 0
            , vRun = 0
            , conf = confidence
            }

--update estimate. 
updateEst :: Estimator -> Double -> Estimator
updateEst est val = let
  -- increment number of values
  !k' = k est + 1

  -- update run sum based on value
  runSum = sRun est
  diff = runSum - (fromInteger k' - 1) * val
  !sRun' = runSum + val

  -- update variance
  !vRun'
    | k' <= 1 = 1.0/0
    | k' == 2 = (diff / fromInteger k') * (diff / (fromInteger k' - 1))
    | otherwise = vRun est + (diff / fromInteger k') * (diff / (fromInteger k' - 1))
  conf' = conf est
  in Estimator { k = k'
               , sRun = sRun'
               , vRun = vRun'
               , conf = conf'
               }


getVar :: Estimator -> Double
getVar est = vRun est / (fromInteger (k est) - 1)

getMean :: Estimator -> Double
getMean est = sRun est / fromInteger (k est)

getCIRange :: Estimator -> (Double, Double)
getCIRange est = (m - hw, m + hw)
  where
    m = getMean est
    hw = getHalfWidth est
  
getHalfWidth :: Estimator -> Double
getHalfWidth est = z * sqrt (getVar est / fromInteger (k est))
  where z = getZScore est

getRelError :: Estimator -> Double
getRelError est = getHalfWidth est / abs (getMean est)

getZScore :: Estimator -> Double
getZScore est = invnormcdf $ getP $ conf est

getNumTrials :: Estimator -> Double -> Int
getNumTrials est eps = ceiling $ (var * z^2) / (width^2)
  where
    var = getVar est
    z = getZScore est
    width = getMean est * eps
    
