Run Main.hs to run CoinToss case. 

Estimator.hs :: Functions to keep track of the current value of the estimator, so that quantities like mean, variance, confidence intervals etc. can be calculated for the given quantity of interest. Numerically stable formulae for these statistical measures are used that require O(1) space so that we do not have to keep all the simulation values in memory.

StateSpace.hs :: Defines the types (interfaces) that client code must use to specify the state-space, events, transitions, and active events (see Table 2 for more details).

EventList.hs :: Defines the event list data structure, which uses a priority search queue to schedule events, and other helper functions to schedule events at each state transition.

GSMP.hs :: The heart of the algorithm that specifies how to simulate the system to obtain point estimates for the quantity of interest, to a desired precision.
