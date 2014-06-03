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
    self.hit = 0
    self.miss = 0

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
          self.train(key, chord, prev[:1], self.markov2)

        if len(prev) > 1:
          self.train(key, chord, prev[:2], self.markov3)

        if len(prev) > 2:
          self.train(key, chord, prev[:3], self.markov4)
          prev = prev[1:]
          
        prev += [chord]

  
  def train(self, key, curr, prevs, markovDict):
    try:
      prevNotes = [self.progRef[key][prev] for prev in prevs]
      currNote = self.progRef[key][curr]
    except:
      return

    markovDict[tuple(prevNotes)] = markovDict.get(tuple(prevNotes), {})
    markovDict[tuple(prevNotes)][currNote] = markovDict[tuple(prevNotes)].get(currNote, 0) + 1


  def predict(self, key, rootScores, markovDict):
    labels = sorted(self.progRef[key].keys())
    labels = labels[2:] + labels [:2]
    scores = [dict(zip(labels, rootScore)) for rootScore in rootScores]

    progScores = {}
    # possible progressions
    for prog in markovDict:
      # get the score for this progression
      try:
        notes = [self.progRefRev[key][note] for note in prog]
      except:
        continue
      noteScores = [scores[i][notes[i]] for i in range(len(notes))]

      for dest in markovDict[prog]:
        progScores[dest] = max(progScores.get(dest,0), float(markovDict[prog][dest])) / sum(noteScores)
        #progScores[dest] = max(progScores.get(dest,0), float(1)) / sum(noteScores)

    bestNotes = sorted(progScores.keys(), key = lambda k: progScores[k], reverse=True)

    for bestNote in bestNotes:
      if bestNote in self.progRefRev[key]:
        return self.progRefRev[key][bestNote]

    self.miss += 1
    print "MISS", self.miss
    return "N/A"


