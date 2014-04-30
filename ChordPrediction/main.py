# aggregate run of the main file
import analyze
import os
import progs
import pdb

cnt = 0
tot = 0

# No merging at all. Works very well...85%
def doNothing(chord_dat, prog_dict):
  return


# The merge causes all slices to be selected as the chord C. Obviously does nothing 13%
def onlyC(chord_dat, prog_dict):
  for bar in chord_dat:
    for i in range(len(chord_dat[bar])):
      chord_dat[bar][i]["roots"] = (0,1,1,1,1,1,1)


# progression analysis. use prog data.
# C -> A,C
# C -> F,G,D,E :: 1
# C -> B :: 2
# (C:2,D:3,...) (C:5,D:4,...) => if I IV V has strength 2, and key is C, then half scores of C in 1st, F in 2nd, G in 3rd.
def basicProg(chord_dat, prog_dict):
  for bar in chord_dat:
    pdb.set_trace()

chord_progs = progs.getProgs("progs.txt")

for fname in os.listdir("chord_data/"):

  key = open("chord_data/" + fname, "r").readlines()[1].split()[13]
  chord_trans = progs.transpose(chord_progs, key)
  
  val = analyze.run(fname, basicProg, chord_trans)
  cnt += val
  tot += 1
  print val


print '\n'
print cnt/tot
