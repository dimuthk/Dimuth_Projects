import os
import pdb
import numpy as np

class Markov():
  def __init__(self):
    self.progRef = {}
    self.progRef['G'] = {'G':'I', 'A':'ii', 'B':'iii', 'C':'IV', 'D':'V', 'E':'vi', 'F#':'viio'}
    self.progRef['A'] = {'A':'I', 'B':'ii', 'C#':'iii', 'D':'IV', 'E':'V', 'F#':'vi', 'G#':'viio'}
    self.progRef['a'] = {'A':'i', 'B':'iio', 'C':'III', 'D':'iv', 'E':'v', 'F':'VI', 'G':'VII'}
    self.progRef['C'] = {'C':'I', 'D':'ii', 'E':'iii', 'F':'IV', 'G':'V', 'A':'vi', 'B':'viio'}
    self.progRef['b'] = {'B':'i', 'C#':'iio', 'D':'III', 'E':'iv', 'F#':'v', 'G':'VI', 'A':'VII'}
    self.progRef['e'] = {'E':'i', 'F#':'iio', 'G':'III', 'A':'iv', 'B':'v', 'C':'VI', 'D':'VII'}
    self.progRef['g'] = {'G':'i', 'A':'iio', 'B-':'III', 'C':'iv', 'D':'v', 'E-':'VI', 'F':'VII'}
    self.progRef['F'] = {'F':'I', 'G':'ii', 'A':'iii', 'B-':'IV', 'C':'V', 'D':'vi', 'E':'viio'}
    self.progRef['B-'] = {'B-':'I', 'C':'ii', 'D':'iii', 'E-':'IV', 'F':'V', 'G':'vi', 'A':'viio'}
    self.progRef['A-'] = {'A-':'I', 'B-':'ii', 'C':'iii', 'D-':'IV', 'E-':'V', 'F':'vi', 'G':'viio'}
    self.progRef['c'] = {'C':'i', 'D':'iio', 'E-':'III', 'F':'iv', 'G':'v', 'A-':'VI', 'B-':'VII'}
    self.progRef['d'] = {'D':'i', 'E':'iio', 'F':'III', 'G':'iv', 'A':'v', 'B-':'VI', 'C':'VII'}
    self.progRef['E'] = {'E':'I', 'F#':'ii', 'G#':'iii', 'A':'IV', 'B':'V', 'C#':'vi', 'D#':'viio'}
    self.progRef['E-'] = {'E-':'I', 'F':'ii', 'G':'iii', 'A-':'IV', 'B-':'V', 'C':'vi', 'D':'VII'}
    self.progRef['D'] = {'D':'I', 'E':'ii', 'F#':'iii', 'G':'IV', 'A':'V', 'B':'vi', 'C#':'VII'}

    self.progRefRev = {}
    for key in self.progRef:
      self.progRefRev[key] = dict([(item[1],item[0]) for item in self.progRef[key].items()])

    self.markov2 = {}
    self.markov3 = {}
    self.markov4 = {}

    for fname in os.listdir("chord_data/"):
      prev = list()

      for line in open("chord_data/" + fname).readlines()[1:]:
        contents = line.split()
        
        key = contents[13]
        chord = contents[12]

        if len(prev) > 0:
          self.train(key, chord, prev[-1])

        prev += [chord]


  def train(self, key, curr, prev):
    try:
      prevNote = self.progRef[key][prev]
      currNote = self.progRef[key][curr]
    except:
      return 

    self.markov2[prevNote] = self.markov2.get(prevNote, {})
    self.markov2[prevNote][currNote] = self.markov2[prevNote].get(currNote, 0) + 1

  # given root scores of previous line, predict current line
  def predict2(self, key, rootScores):
    labels = self.progRef[key].keys()
    labels = sorted(labels)
    labels = labels[2:] + labels[:2]
    scores = dict(zip(labels, rootScores))

    progScores = {}
    for label in labels:
      note = self.progRef[key][label]

      for dest in self.markov2[note]:
        progScores[dest] = float(self.markov2[note][dest]) / scores[label]
    
    bestNote =  max(progScores.keys(), key = lambda k: progScores[k])
    if bestNote in self.progRefRev[key]:
      return self.progRefRev[key][bestNote]
    else:
      return "N/A"


'''
for fname in os.listdir("chord_data/"):
  prev = list()
  for line in open("chord_data/" + fname).readlines()[1:]:
    chord = line.split()[14]
  
    if len(prev) > 0:
      markov2[prev[0]] = markov2.get(prev[0], {})
      markov2[prev[0]][chord] = markov2[prev[0]].get(chord, 0) + 1

    if len(prev) > 1:
      markov3[tuple(prev[:2])] = markov3.get(prev[0], {})
      markov3[tuple(prev[:2])][chord] = markov3[tuple(prev[:2])].get(chord, 0) + 1

    if len(prev) > 2:
      markov4[tuple(prev[:3])] = markov4.get(prev[0], {})
      markov4[tuple(prev[:3])][chord] = markov4[tuple(prev[:3])].get(chord, 0) + 1
    
    if len(prev) == 4:
      prev = prev[1:]

    prev += [chord]

pdb.set_trace()
'''

'''
progs = list()
for fname in os.listdir("chord_data/"):
  prog = list()
  for line in open("chord_data/" + fname).readlines()[1:]:
    contents = line.split()

    key = contents[13]

    if len(prog) > 0:
      if prog[-1] != contents[12]:
        prog += [contents[12]]
    else:
      prog += [contents[12]]
  
  progs.append([progRef[key].get(p,'-') for p in prog])

markov = Markov()
for prog in progs:
  prev = list()

  for chord in prog:
    pdb.set_trace()
  

pdb.set_trace()
'''
