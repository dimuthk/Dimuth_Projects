import numpy as np
import pdb

class ViterbiAlg:
  def __init__(self, obs_space, state_space, state_trans, emission, init_probs):
    self.O =  obs_space #all possible observations
    self.S = state_space #all possible states
    self.A = state_trans #transition matrix giving probs of moving to other spaces
    self.B = emission #transition matrix giving probs of observation from curr state
    self.pi = init_probs #initial probabilities for spaces

    self.N = len(self.O)
    self.K = len(self.S)
  
  # assume x and y are Counters
  def jaccard(self, x, y):
    intersection = sum((x & y).values())
    union = sum((x | y).values())
    return float(intersection) / union


  # you don't just get tuples for matrix B. for an index i (the chord), return the 
  def getB(self, i,j):
    return self.B[(i,j)] if (i,j) in self.B else 0
  

  # get the index from O that is closest.
  def indexOf(self, y):
    return max(range(self.N), key = lambda k: self.jaccard(self.O[k], y))
  

  def predict(self,Y):
    #init T1
    T = len(Y)
    T1 = np.zeros((self.K,T))
    T2 = np.zeros((self.K,T))
    Z = np.zeros(T, dtype = int)
    X = list()
    
    for i in range(self.K):
      #T1[i,0] = self.pi[i] * self.B[(i, self.indexOf(Y[0]))]
      T1[i,0] = self.pi[i] * self.getB(i, self.indexOf(Y[0]))

    for i in range(1,T):
      for j in range(self.K):
        #T1[j,i] = max([T1[k,i-1] * self.A[k,j] * self.B[(j,self.indexOf(Y[i]))] for k in range(self.K)])
        T1[j,i] = max([T1[k,i-1] * self.A[k,j] * self.getB(j, self.indexOf(Y[i])) for k in range(self.K)])
        #T2[j,i] = max([k for k in range(self.K)], key=lambda k: T1[k,i-1]*self.A[k,j] * self.B[(j,self.indexOf(Y[i]))])
        T2[j,i] = max([k for k in range(self.K)], key=lambda k: T1[k,i-1]*self.A[k,j] * self.getB(j, self.indexOf(Y[i])))

    Z[T-1] = max([k for k in range(self.K)], key = lambda k: T1[k, T-1])
    X.append(self.S[Z[T-1]])

    for i in sorted(range(1,T), reverse=True):
      Z[i-1] = T2[Z[i],i]
      X.append(self.S[Z[i-1]])
  
    return list(reversed(X))

'''
S = ('Healthy', 'Fever')
O = ('normal', 'cold', 'dizzy')
pi = {0: 0.6, 1: 0.4}

A = np.matrix([[0.7, 0.3],[0.4,0.6]])
B = np.matrix([[0.5,0.4,0.1],[0.1,0.45,0.45]])

case = ViterbiAlg(O,S,A,B,pi)
Y = ('normal','normal','cold','dizzy')
print case.predict(Y)
'''
