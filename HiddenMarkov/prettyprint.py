# for debugging and other potentially useful stuff...
# build the A and B matrices. A should be easy enough, but
# you have to label the coordinates.

# how about B? do like this:
# for chord i, a list of 

import marshal
import pdb
import numpy as np
import sys
import matplotlib.pyplot as py

choice = sys.argv[0]

path = "transitions/"
int_A = marshal.load(open(path+"int_A","rb"))
int_B = marshal.load(open(path+"int_B","rb"))
song_A = marshal.load(open(path+"song_A","rb"))
song_B = marshal.load(open(path+"song_B","rb"))
real_chords = marshal.load(open(path+"real_chords","rb"))
real_notes = marshal.load(open(path+"real_notes","rb"))
int_init = marshal.load(open(path+"initInt","rb"))
song_init = marshal.load(open(path+"initSong","rb"))


# reverse the indexes
def rev(index):
  revIndex = {}
  for k,v in index.items():
    revIndex[v] = k
  return revIndex

c_indexIntRev = rev(marshal.load(open(path+"c_indexInt","rb")))
m_indexIntRev = rev(marshal.load(open(path+"m_indexInt","rb")))
c_indexSongRev = rev(marshal.load(open(path+"c_indexSong","rb")))
m_indexSongRev = rev(marshal.load(open(path+"m_indexSong","rb")))
chd_sym = rev(marshal.load(open(path+"chd_sym","rb")))


def printInit(index, vector, real_chords):
  print '\n\n'
  chords = '\t'.join([real_chords[index[i]] for i in range(len(vector))])
  print chords
  vals = '\t'.join([str("%.2f"%vector[i]) for i in range(len(vector))])
  print vals
  print '\n\n'
  #py.pie(vector, labels = [real_chords[index[i]] for i in range(len(vector))], \
  #       shadow = True, startangle=90)
  #py.show()

def printA(index, matrix, real_chords, fname):
  fout = open(fname,"w")
  # round everything to 2 decimal places
  print "\nMATRIX A\n"
  chords = '\t'.join([real_chords[index[i]] for i in range(len(matrix))])
  print '\t' + chords

  chord_ref = chords.split('\t')
  fout.write("graph G{\n")
  
  
  for i in range(len(matrix)):
    row = matrix[i]
    line = real_chords[index[i]]
    for j, item in enumerate(row):
      line += '\t' + str("%.2f" % item)
      fout.write(real_chords[index[i]] + ' -> ' + chord_ref[j] + \
                 "[weight=" + str(item) + "]\n")
    print line

  fout.write("}")
  fout.close()


def printB(c_index, m_index, matrix, real_chords, fname):
  fout = open(fname,"w")
  print "\nMATRIX B\n"
  # there's no point in actually printing the matrix. give the number, average, 
  # std, highest, and lowest melody for each chord.
  #pairs = [(c_index[c], decrypt(m_index[m],chd_sym, real_notes)) for c,m in matrix.keys()]
  chords = {}
  for c,m in matrix.keys():
    if c not in chords:
      chords[c] = list()
    chords[c].append(m)
  print "\t" + "Contr" + "\t" + "Avg" + "\t" + "Best" + "\t" + "Worst"
  allTotal = 0

  for c in chords.keys():
    chord = c_index[c]
    data = [matrix[(c,m)] for m in chords[c]]
    allTotal += len(data)

  
  for c in chords.keys():
    chord = c_index[c]
    line = real_chords[chord]
    data = [matrix[(c,m)] for m in chords[c]]
    total = len(data)
    avg = sum(data) / float(total)
    best = max(data)
    worst = min(data)
    line += '\t' + str("%.2f"%(float(total)/allTotal)) + '\t' + str("%.2f"%avg) + '\t' + \
    str("%.2f"%best) + '\t' + str("%.2f"%worst) + '\t'
    print line

  fout.write("graph G{\n")
  global chd_sym
  for c in chords.keys():
    fout.write(real_chords[c_index[c]] + "[weight = 10];\n")
  melodies = set([m for c,m in matrix])
  for m in melodies:
    trans = [chd_sym[v] for v in m_index[m]]
    fout.write(''.join(trans) + " [label = \"\", weight = 1 ];\n")
  for c,m in matrix:
    trans = [chd_sym[v] for v in m_index[m]]
    fout.write(real_chords[c_index[c]] + " -> " + ''.join(trans) + \
               " [weight = 1];\n")
    
  fout.write("}")
  fout.close()


#printA(c_indexSongRev, song_A, real_chords, "vocalA.dot")
#printB(c_indexSongRev, m_indexSongRev, song_B, real_chords, "vocalB.dot")
#printA(c_indexIntRev, int_A, real_chords, "instrA.dot")
#printB(c_indexIntRev, m_indexSongRev, int_B, real_chords, "instrB.dot")



printInit(c_indexIntRev, int_init,  real_chords)
printInit(c_indexSongRev, song_init,  real_chords)
