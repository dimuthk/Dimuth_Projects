import viterbi
from collections import Counter
import marshal
import numpy as np
import pdb
import sys
import compiler_measure
import os
from parsesong import process

# this is the main model. build the viterbi predictor, and
# perform K-fold cross-validation on the songs.
# this would require communication with the compiler. take as
# input the K value. 

#if len(sys.argv) == 1:
#  print "use case: <K> <noScale (optional)> <compile_only (optional)>"
#  exit(1)

#K = int(sys.argv[1])
K = 10


if len(sys.argv) > 2:
  noScale = True
else:
  noScale = False

noScale = False
song_path = "songs/"

res = compiler_measure.compile(K, noScale, song_path)
if res != 0:
  exit(1)
if len(sys.argv) > 3:
  exit(0)


songs = list()

# retrieve the matrices from the compiler, and
# build the Viterbi model
data_path = "transitions/"
chd_sym = marshal.load(open(data_path+"chd_sym","rb"))
int_A = np.matrix(marshal.load(open(data_path+"int_A","rb")))
int_B = marshal.load(open(data_path+"int_B","rb"))
c_indexInt = marshal.load(open(data_path+"c_indexInt","rb"))
m_indexInt = marshal.load(open(data_path+"m_indexInt","rb"))
initInt = marshal.load(open(data_path+"initInt","rb"))

song_A = np.matrix(marshal.load(open(data_path+"song_A","rb")))
song_B = marshal.load(open(data_path+"song_B","rb"))
c_indexSong = marshal.load(open(data_path+"c_indexSong","rb"))
m_indexSong = marshal.load(open(data_path+"m_indexSong","rb"))
initSong = marshal.load(open(data_path+"initSong","rb"))

int_S = sorted(c_indexInt.keys(), key=lambda k:c_indexInt[k])
int_O = sorted(m_indexInt.keys(), key=lambda k:m_indexInt[k])
int_O = [Counter(seq) for seq in int_O]
int_V = viterbi.ViterbiAlg(int_O, int_S, int_A, int_B, initInt)

song_S = sorted(c_indexSong.keys(), key=lambda k:c_indexSong[k])
song_O = sorted(m_indexSong.keys(), key=lambda k:m_indexSong[k])
song_O = [Counter(seq) for seq in song_O]
song_V = viterbi.ViterbiAlg(song_O, song_S, song_A, song_B, initSong)

# retrieve z
z = max([ord(item) for item in chd_sym.values()])
int_total = 0
int_correct = 0
song_total = 0
song_correct = 0
int_total_nonc = 0
int_correct_nonc = 0
song_total_nonc = 0
song_correct_nonc = 0
print "beginning to test..."


# process the test set
melodyListInt = list()
melodyListSong = list()



for artist in os.listdir(song_path):
    for song in os.listdir(song_path + artist + "/"):
        if song.endswith(".swp"):
          continue    
    
        lines = open(song_path + artist + "/" + song,"r").readlines()
        melody, chords, cnt, chd_sym, z = process(lines, chd_sym, z, noScale)
        for m in melody:
            if 'I' in m:
                melodyListInt.append((melody[m], chords[m], cnt[m]))
            elif 'S' in m:
                melodyListSong.append((melody[m], chords[m], cnt[m]))

sym_chd = dict([(v2,v1) for v1,v2 in chd_sym.items()])

meas_cnt = 0
for melody,chordProg,cnt in melodyListSong:
    meas_cnt += 1
    if meas_cnt % (K-1) != 0:
      continue

    solution = list(chordProg)
    inp = [melody[i:i+8] for i in range(len(melody))[0::8]]
    inp = [Counter(item) for item in inp]
    assert(len(inp) == len(solution))

    prediction = song_V.predict(inp)
    total_ = len(prediction)
    correct_ = len([i for i in range(len(prediction)) if prediction[i] == solution[i]])
    print "S", correct_, '/', total_
    print prediction, solution

    song_total += total_
    song_correct += correct_
    total_nonc = len([p for p in solution if p != 'C'])
    correct_nonc = len([i for i in range(len(prediction)) if prediction[i] == solution[i] and solution[i]!='C'])
    song_total_nonc += total_nonc
    song_correct_nonc += correct_nonc



meas_cnt = 0
for melody,chordProg,cnt in melodyListInt:
    meas_cnt += 1
    if meas_cnt % (K-1) != 0:
      continue

    solution = list(chordProg)
    inp = [melody[i:i+8] for i in range(len(melody))[0::8]]
    inp = [Counter(item) for item in inp]
    assert(len(inp) == len(solution))

    prediction = int_V.predict(inp)
    total_ = len(prediction)
    correct_ = len([i for i in range(len(prediction)) if prediction[i] == solution[i]])
    print "I", correct_, '/', total_
    print prediction, solution

    int_total += total_
    int_correct += correct_
    total_nonc = len([p for p in solution if p != 'C'])
    correct_nonc = len([i for i in range(len(prediction)) if prediction[i] == solution[i] and solution[i]!='C'])
    int_total_nonc += total_nonc
    int_correct_nonc += correct_nonc



print "interlude accuracy: ", float(int_correct)/int_total
print "singing accuracy: ", float(song_correct)/song_total
print "interlude accuracy noc: ", float(int_correct_nonc)/int_total_nonc
print "singing accuracy noc: ", float(song_correct_nonc)/song_total_nonc
