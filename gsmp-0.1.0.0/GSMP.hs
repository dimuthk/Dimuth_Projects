{-# LANGUAGE TemplateHaskell, Rank2Types #-}
module GSMP where
import StateSpace
import Probability
import Estimator (Estimator, initEst)
import Numeric.Probability.Distribution (selectP)
import Control.Monad.State
import Control.Monad.Trans.Maybe (MaybeT(..), runMaybeT)
import Data.Maybe (isNothing, fromJust)
import Control.Lens
import System.Random



--data EventList = EventList
data EventList = [Event]

data Sim s = Sim { _st :: StateSpace s
                 , _gen :: StdGen
                 , _time :: Estimator
                 , _ev :: EventList
                 , _activeEvents :: ActiveEvents s
                 , _transition :: Transition s
                 }

makeLenses ''Sim

-- data TermFn s = NumReps Int | Conf Probability | StateFunction (StateSpace s -> Bool)

type TermFn s = Sim s -> Bool

-- Replace this by importing from the EventList class
schedule :: EventList -> [Event] -> EventList
schedule el [e] = 

nextEvent :: EventList -> (Event, EventList)
nextEvent el = undefined

-- transitioning to the next state (to implement : calculating quantity of interest)
stateTransition :: State (Sim s) ()
stateTransition = do
  cst <- use st
  evList <- use ev
  trans <- use transition
  activeEv <- use activeEvents
  g <- use gen
  let (nev, evList') = nextEvent evList
      states = trans cst nev
      rand :: Double
      (rand, g') = random g
      nextState = (selectP (fromJust states) rand) 
  ev .= evList'
  gen .= g'
  st .= nextState
  ev .= schedule evList' (activeEv cst)

simRep :: StateCond s -> State (Sim s) ()
simRep termFn = do
  curState <- use st
  if (termFn curState)
    then return ()
    else do
    stateTransition
    simRep termFn
