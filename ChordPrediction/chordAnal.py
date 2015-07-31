import os
import pdb
import numpy as np



markov2 = {}
markov3 = {}
markov4 = {}

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

progRef = {}
progRef['G'] = {'G':'I', 'A':'ii', 'B':'iii', 'C':'IV', 'D':'V', 'E':'vi', 'F#':'viio'}
progRef['A'] = {'A':'I', 'B':'ii', 'C#':'iii', 'D':'IV', 'E':'V', 'F#':'vi', 'G#':'viio'}
progRef['a'] = {'A':'i', 'B':'iio', 'C':'III', 'D':'iv', 'E':'v', 'F':'VI', 'G':'VII'}
progRef['C'] = {'C':'I', 'D':'ii', 'E':'iii', 'F':'IV', 'G':'V', 'A':'vi', 'B':'viio'}
progRef['b'] = {'B':'i', 'C#':'iio', 'D':'III', 'E':'iv', 'F#':'v', 'G':'VI', 'A':'VII'}
progRef['e'] = {'E':'i', 'F#':'iio', 'G':'III', 'A':'iv', 'B':'v', 'C':'VI', 'D':'VII'}
progRef['g'] = {'G':'i', 'A':'iio', 'B-':'III', 'C':'iv', 'D':'v', 'E-':'VI', 'F':'VII'}
progRef['F'] = {'F':'I', 'G':'ii', 'A':'iii', 'B-':'IV', 'C':'V', 'D':'vi', 'E':'viio'}
progRef['B-'] = {'B-':'I', 'C':'ii', 'D':'iii', 'E-':'IV', 'F':'V', 'G':'vi', 'A':'viio'}
progRef['A-'] = {'A-':'I', 'B-':'ii', 'C':'iii', 'D-':'IV', 'E-':'V', 'F':'vi', 'G':'viio'}
progRef['c'] = {'C':'i', 'D':'iio', 'E-':'III', 'F':'iv', 'G':'v', 'A-':'VI', 'B-':'VII'}
progRef['d'] = {'D':'i', 'E':'iio', 'F':'III', 'G':'iv', 'A':'v', 'B-':'VI', 'C':'VII'}
progRef['E'] = {'E':'I', 'F#':'ii', 'G#':'iii', 'A':'IV', 'B':'V', 'C#':'vi', 'D#':'viio'}
progRef['E-'] = {'E-':'I', 'F':'ii', 'G':'iii', 'A-':'IV', 'B-':'V', 'C':'vi', 'D':'VII'}
progRef['D'] = {'D':'I', 'E':'ii', 'F#':'iii', 'G':'IV', 'A':'V', 'B':'vi', 'C#':'VII'}

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


for prog in progs:
  pdb.set_trace()
  

pdb.set_trace()
