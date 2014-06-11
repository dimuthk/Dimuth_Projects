{-# LANGUAGE TemplateHaskell, Rank2Types, NoMonomorphismRestriction, BangPatterns, FlexibleContexts #-}
module GSMP where
import Control.Monad.State.Strict
import Control.Lens
import Control.DeepSeq
import System.Random
import Numeric.Probability.Distribution (selectP)
import StateSpace (Time, StateSpace, ActiveEvents, CanceledEvents,
                   Transition, StateCond, StateQty, ProcessEst, Seed, Acc)
import Probability
import Estimator (Estimator, initEst, updateEst, getRelError, getMean)
import EventList (EventList(..), schedule, nextEvent, initEL)

--simulation description
data Sim s a = Sim { _st :: StateSpace s
                   , _el :: EventList a
                   , _gen :: StdGen
                   , _tTrans :: Time
                   , _accum :: [Double]
                   , _est :: Estimator
                   , _rep :: Int
                   }

--allow getter and setter methods in simulation
makeLenses ''Sim

--termination criterion, based on repetition count or confidence
data TermCriterion = NumReps Int | Prec Probability 

stateTransition :: (Show a, Ord a, Eq a, NFData s, NFData a) =>
                   ActiveEvents s a -> CanceledEvents s a ->
                   Transition s a -> State (Sim s a) ()
stateTransition activeEv canceledEv trans = do
  --retrieve current state, eventlist, and RNG objects
  cst <- use st
  evList <- use el
  g <- use gen

  --obtain next event, time, and modified eventList
  let (nev, t, evList') = nextEvent evList
      --obtain transition states and probabilities
      states = cst `deepseq` trans cst nev
      rand :: Double
      (!rand, g') = random g

      --select next state given distribution and generator
      nextState = case states of
        Just x -> selectP x rand
        Nothing -> error "Invalid state transition: current state is a sink."

      --modify eventList to match next state
      (evList'', g'') = schedule evList' (activeEv nextState) (canceledEv nextState) g'

  --update simulation attributes
  st .= nextState
  el .= evList''
  gen .= g''
  tTrans .= t


--continue simulations until termination condition achieved
simRep :: (Show s, Show a, Ord a, Eq a, NFData s) => State (Sim s a) () ->
          StateQty s -> StateCond s -> State (Sim s a) ()
simRep transState qof termFn = do
  curState <- use st
  eventList <- use el
  timeTrans <- use tTrans
  let curTime = sysTime eventList
  if (termFn curState curTime)
    then return ()
    else do
    curAcc <- use accum
    accum .= curAcc `deepseq` curState `deepseq` qof curState timeTrans curAcc
    transState
    simRep transState qof termFn 


--update state values in simulation
updateSim :: (Show s, Show a, Eq a, Ord a, NFData a) =>
           StateSpace s -> ActiveEvents s a -> StateQty s -> Acc -> State (Sim s a) ()
updateSim initState activeEv qof initAcc = do
  st .= initState
  accum .= qof initState 0.0 initAcc
  randomGen <- use gen
  let (el', gen') = initEL (activeEv initState) randomGen
  el .= el'
  gen .= gen'


--initialize simulation parameters with inputted confidence interval
initSim :: (Show s, Show a, Eq a, Ord a, NFData a) =>
           StateSpace s -> ActiveEvents s a -> StateQty s -> Acc -> Probability -> StdGen -> Sim s a
initSim initState activeEv qof initAcc conf gen =
  let
    est = initEst conf
    acc = qof initState 0.0 initAcc
    (el', gen') = initEL (activeEv initState) gen
  in Sim initState el' gen' 0.0 acc est 0


terminate :: TermCriterion -> Estimator -> Int -> Bool
terminate term curEst nrep =
  case term of
    Prec x -> (getRelError curEst) <= (getP x)
    NumReps n -> nrep >= n

--perform N simulations
simNReps :: (Show s, Show a, Eq a, Ord a) =>
            State (Sim s a) () -> State (Sim s a) () ->
            ProcessEst -> TermCriterion -> State (Sim s a) Estimator
simNReps updateRep simulateRep processAcc endCond = do
  curEst <- use est
  nrep <- use rep
  if terminate endCond curEst nrep
    then return curEst
    else do
    simulateRep
    acc <- use accum
    est .= updateEst curEst (processAcc acc)
    rep .= (nrep `seq` (nrep + 1))
    updateRep
    simNReps updateRep simulateRep processAcc endCond
      
--run single simulation.
simulate :: (Show s, Show a, Eq a, Ord a, NFData s, NFData a) =>
            StateSpace s -> ActiveEvents s a -> CanceledEvents s a -> Transition s a -> StateQty s ->
            Acc -> StateCond s -> ProcessEst -> TermCriterion -> Seed -> Probability -> IO Estimator
simulate initState activeEv canceledEv transFn qof initAcc termFn processAcc endCond seed conf = do
  randomGen <- case seed of
    Just x -> return (mkStdGen x)
    _ -> getStdGen

  let sim0 = initSim initState activeEv qof initAcc conf randomGen
      updateRep = updateSim initState activeEv qof initAcc
      transition = stateTransition activeEv canceledEv transFn
      simulateRep = simRep transition qof termFn
  return $ evalState (simNReps updateRep simulateRep processAcc endCond) sim0

      
      

