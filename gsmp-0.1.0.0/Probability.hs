module Probability ( Probability
                   , getP
                   , mkP) where

newtype Probability = P { getP :: Float }
                    deriving (Eq, Ord, Show)

mkP :: Float -> Probability
mkP x | 0 <= x && x <= 1 = P x
      | otherwise = error $ show x ++ " is not in [0, 1]"

instance Num Probability where
  P x + P y = mkP (x + y)
  P x * P y = mkP (x * y)
  abs (P x) = P x
  signum (P x) = 1
  fromInteger = mkP . fromInteger
