module Estimator ( Estimator
                 , getNumTrials
                 , initEst
                 ) where
import Probability (Probability, getP)
import Data.Number.Erf (invnormcdf)

data Estimator = Estimator { k :: Integer
                           , sRun :: Float
                           , vRun :: Float
                           , conf :: Probability
                           }

initEst :: Probability -> Estimator
initEst confidence =
  Estimator { k = 0
            , sRun = 0
            , vRun = 0
            , conf = confidence
            }
  
updateEst :: Estimator -> Float -> Estimator
updateEst est val =
  Estimator { k = k est + 1
            , sRun = sRun est + val
            , vRun = updateV est val
            , conf = conf est
            }
  where
    updateV est val
      | k' <= 1 = 1.0/0
      | otherwise = (diff / fromInteger k') * (diff / fromInteger k' - 1)
      where
        k' = k est + 1
        diff = sRun est - (sRun est - 1) * val

getVar :: Estimator -> Float
getVar est = vRun est / fromInteger (k est) - 1

getMean :: Estimator -> Float
getMean est = sRun est / fromInteger (k est)

getCIRange :: Estimator -> (Float, Float)
getCIRange est = (m - hw, m + hw)
  where
    m = getMean est
    hw = getHalfWidth est
  
getHalfWidth :: Estimator -> Float
getHalfWidth est = z * sqrt (getVar est / fromInteger (k est))
  where z = getZScore est

getRelError :: Estimator -> Float
getRelError est = getHalfWidth est / abs (getMean est)

getZScore :: Estimator -> Float
getZScore est = invnormcdf $ getP $ conf est

getNumTrials :: Estimator -> Float -> Int
getNumTrials est eps = ceiling $ (var * z^2) / (width^2)
  where
    var = getVar est
    z = getZScore est
    width = getMean est * eps
    
