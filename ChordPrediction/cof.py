import pdb

#A,a,C,b,e,G,F,d,D,g,B-
#A-,E

class COF:
  def __init__(self):
    self.cofDict = {}
    self.cofDict['A'] = {'A':['a','c#','e'], 'B':['b','d','f#'], 'C':['c#','e','g#'], 'D':['d','f#','a'], 'E':['e','g#','b'], 'F':['f#','a','c#'], 'G':['g#','b','d']}

    self.cofDict['G'] = {'A':['a','c','e'], 'B':['b','d','f#'], 'C':['c','e','g'], 'D':['d','f#','a'], 'E':['e','g','b'], 'F':['f#','a','c'], 'G':['g','b','d']}
    self.cofDict['e'] = {'A':['a','c','e'], 'B':['b','d','f#'], 'C':['c','e','g'], 'D':['d','f#','a'], 'E':['e','g','b'], 'F':['f#','a','c'], 'G':['g','b','d']}

    self.cofDict['F'] = {'A':['a','c','e'], 'B':['b-','d','f'], 'C':['c','e','g'], 'D':['d','f','a'], 'E':['e','g','b-'], 'F':['f','a','c'], 'G':['g','b-','d']}
    self.cofDict['d'] = {'A':['a','c','e'], 'B':['b-','d','f'], 'C':['c','e','g'], 'D':['d','f','a'], 'E':['e','g','b-'], 'F':['f','a','c'], 'G':['g','b-','d']}

    self.cofDict['D'] = {'A':['a','c#','e'], 'B':['b','d','f#'], 'C':['c#','e','g'], 'D':['d','f#','a'], 'E':['e','g','b'], 'F':['f#','a','c#'], 'G':['g','b','d']}
    self.cofDict['b'] = {'A':['a','c#','e'], 'B':['b','d','f#'], 'C':['c#','e','g'], 'D':['d','f#','a'], 'E':['e','g','b'], 'F':['f#','a','c#'], 'G':['g','b','d']}

    self.cofDict['C'] = {'A':['a','c','e'], 'B':['b','d','f'], 'C':['c','e','g'], 'D':['d','f','a'], 'E':['e','g','b'], 'F':['f','a','c'], 'G':['g','b','d']}
    self.cofDict['a'] = {'A':['a','c','e'], 'B':['b','d','f'], 'C':['c','e','g'], 'D':['d','f','a'], 'E':['e','g','b'], 'F':['f','a','c'], 'G':['g','b','d']}

    self.cofDict['B-'] = {'A':['a','c','e-'], 'B':['b-','d','f'], 'C':['c','e-','g'], 'D':['d','f','a'], 'E':['e-','g','b-'], 'F':['f','a','c'], 'G':['g','b-','d']}
    self.cofDict['g'] = {'A':['a','c','e-'], 'B':['b-','d','f'], 'C':['c','e-','g'], 'D':['d','f','a'], 'E':['e-','g','b-'], 'F':['f','a','c'], 'G':['g','b-','d']}

    self.cofDict['E-'] = {'A':['a-','c','e-'], 'B':['b-','d','f'], 'C':['c','e-','g'], 'D':['d','f','a-'], 'E':['e-','g','b-'], 'F':['f','a-','c'], 'G':['g','b-','d']}
    self.cofDict['c'] = {'A':['a-','c','e-'], 'B':['b-','d','f'], 'C':['c','e-','g'], 'D':['d','f','a-'], 'E':['e-','g','b-'], 'F':['f','a-','c'], 'G':['g','b-','d']}

    self.cofDict['A-'] = {'A':['a-','c','e-'], 'B':['b-','d-','f'], 'C':['c','e-','g'], 'D':['d-','f','a-'], 'E':['e-','g','b-'], 'F':['f','a-','c'], 'G':['g','b-','d-']}

    self.cofDict['E'] = {'A':['a','c#','e'], 'B':['b','d#','f#'], 'C':['c#','e','g#'], 'D':['d#','f#','a'], 'E':['e','g#','b'], 'F':['f#','a','c#'], 'G':['g#','b','d#']}

  def jaccard(self, list1, list2):
    return float(len(set(list1).intersection(set(list2))))
    
  
  def guessChord2(self, key, pitches):
    ref = self.cofDict[key]
    keys = ref.keys()
    vals = [self.jaccard(ref[k],pitches) for k in keys]
    best = max(ref.keys(), key = lambda k: self.jaccard(ref[k], pitches))  
    vals[keys.index(best)] = -100
    return keys[vals.index(max(vals))]
      
  def guessChord(self, key, pitches):
    ref = self.cofDict[key]
    return max(ref.keys(), key = lambda k: self.jaccard(ref[k], pitches))
