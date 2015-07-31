# aggregate run of the main file
import analyze
import os
import progs
import pdb
import operator
import sys
import random

cnt = 0
tot = 0

# hybrid. match all "good" root scores (<= 3)
def hybrid(chord_dat, prog_dict, _):
  roots = ('C','D','E','F','G','A','B')
  for bar in chord_dat:
    if len(chord_dat[bar]) > 1 and len(chord_dat[bar]) < 6:
      possible_progs = prog_dict[len(chord_dat[bar])].keys()
      possible_progs = [prog for prog in possible_progs if len(prog) == len(chord_dat[bar])]

      progTemplate = [0 for _ in range(len(chord_dat[bar]))]
      # fix "sure" pieces of bar, those with minRoot <=3
      for i in range(len(chord_dat[bar])):
        minRoot = min(chord_dat[bar][i]['roots'])
        if minRoot <= 3:
          progTemplate[i] = roots[chord_dat[bar][i]['roots'].index(minRoot)]
      
      # reduce possible prog samples to those which align with the template. 
      fewer_progs = filter(lambda vec: reduce(lambda a,b: a and b, [progTemplate[j] == vec[j] or progTemplate[j] == 0 for j in range(len(vec))], True), possible_progs)
      if len(fewer_progs) != 0:

        # use some measure to select the best prog out of these. 
        best_prog = max(fewer_progs, key = lambda key: prog_dict[len(chord_dat[bar])][key])


        for j in range(len(chord_dat[bar])):
          chord = best_prog[j]
          if chord == "C":
            chord_dat[bar][j]["roots"] = (0,1,1,1,1,1,1)
          if chord == "D":
            chord_dat[bar][j]["roots"] = (1,0,1,1,1,1,1)
          if chord == "E":
            chord_dat[bar][j]["roots"] = (1,1,0,1,1,1,1)
          if chord == "F":
            chord_dat[bar][j]["roots"] = (1,1,1,0,1,1,1)
          if chord == "G":
            chord_dat[bar][j]["roots"] = (1,1,1,1,0,1,1)
          if chord == "A":
            chord_dat[bar][j]["roots"] = (1,1,1,1,1,0,1)
          if chord == "B":
            chord_dat[bar][j]["roots"] = (1,1,1,1,1,1,0)
      
      # no matches given the "sure" template. leave progression as is.
      else:
        print "SKIP"


# No merging at all. Works very well...85%
def doNothing(chord_dat, prog_dict, _):
  return


# The merge causes all slices to be selected as the chord C. Obviously does nothing 13%
def onlyC(chord_dat, prog_dict, _):
  for bar in chord_dat:
    for i in range(len(chord_dat[bar])):
      chord_dat[bar][i]["roots"] = (0,1,1,1,1,1,1)





# the best possible progression obtained from the prog_dict
def bestProg(chord_dat, prog_dict, answers):
  for bar in chord_dat:
    if len(chord_dat[bar]) > 1 and len(chord_dat[bar]) < 6:
      possible_progs = prog_dict[len(chord_dat[bar])].keys()
      possible_progs = [prog for prog in possible_progs if len(prog) == len(chord_dat[bar])]

      score = {}
      for prog in possible_progs:
        progScore = float(sum([1 if prog[i] == answers[bar][i][0] else 0 for i in range(len(prog))])) / len(prog)
        score[prog] = progScore


      best_prog = max(score.keys(), key=lambda x: score[x])
      
      for i in range(len(chord_dat[bar])):
        chord = best_prog[i]
        if chord == "C":
          chord_dat[bar][i]["roots"] = (0,1,1,1,1,1,1)
        if chord == "D":
          chord_dat[bar][i]["roots"] = (1,0,1,1,1,1,1)
        if chord == "E":
          chord_dat[bar][i]["roots"] = (1,1,0,1,1,1,1)
        if chord == "F":
          chord_dat[bar][i]["roots"] = (1,1,1,0,1,1,1)
        if chord == "G":
          chord_dat[bar][i]["roots"] = (1,1,1,1,0,1,1)
        if chord == "A":
          chord_dat[bar][i]["roots"] = (1,1,1,1,1,0,1)
        if chord == "B":
          chord_dat[bar][i]["roots"] = (1,1,1,1,1,1,0)


# progression analysis. use prog data.
# C -> A,C
# C -> F,G,D,E :: 1
# C -> B :: 2
# (C:2,D:3,...) (C:5,D:4,...) => if I IV V has strength 2, and key is C, then half scores of C in 1st, F in 2nd, G in 3rd.
def basicProg(chord_dat, prog_dict, _):
  roots = {}
  roots['C'] = (3,7,11,15,12,9,6)
  roots['A'] = (7,11,15,12,9,6,3)
  roots['F'] = (11,15,12,9,6,3,7)
  roots['D'] = (15,12,9,6,3,7,11)
  roots['B'] = (12,9,6,3,7,11,15)
  roots['G'] = (9,6,3,7,11,15,12)
  roots['E'] = (6,3,7,11,15,12,9)
  
  #roots['C'] = (3,12,7,9,11,6,15)

  for bar in chord_dat:
    if len(chord_dat[bar]) > 1 and len(chord_dat[bar]) < 6:
      possible_progs = prog_dict[len(chord_dat[bar])].keys() 
      possible_progs = [prog for prog in possible_progs if len(prog) == len(chord_dat[bar])]
      
      
      dotProd = lambda vec1,vec2: reduce(operator.add, map(operator.mul, vec1, vec2))
      # you have a list of all possible progs of the proper length. 
      # you also have the root scores for the incoming measure. 
      # take possible prog P = (X,Y,Z).
      score = {}
      root_scores = [data['roots'] for data in chord_dat[bar]]
      for prog in possible_progs:
        score[prog] = 0
        for i in range(len(prog)):
          score[prog] += dotProd(roots[prog[i]], root_scores[i])

      # pick the max score
      best_prog = min(score.keys(), key=lambda x: score[x])
      for i in range(len(chord_dat[bar])):
        chord = best_prog[i]
        if chord == "C":
          chord_dat[bar][i]["roots"] = (0,1,1,1,1,1,1)
        if chord == "D":
          chord_dat[bar][i]["roots"] = (1,0,1,1,1,1,1)
        if chord == "E":
          chord_dat[bar][i]["roots"] = (1,1,0,1,1,1,1)
        if chord == "F":
          chord_dat[bar][i]["roots"] = (1,1,1,0,1,1,1)
        if chord == "G":
          chord_dat[bar][i]["roots"] = (1,1,1,1,0,1,1)
        if chord == "A":
          chord_dat[bar][i]["roots"] = (1,1,1,1,1,0,1)
        if chord == "B":
          chord_dat[bar][i]["roots"] = (1,1,1,1,1,1,0)
        
        #pdb.set_trace()

chord_progs = {}
chord_progs[2] = progs.getProgs("progs2.txt")
chord_progs[3] = progs.getProgs("progs3.txt")
chord_progs[4] = progs.getProgs("progs4.txt")
chord_progs[5] = progs.getProgs("progs5.txt")


if len(sys.argv) != 2:
  print "Usage: main.py <analysis method>"
  exit(1)

else:
  if sys.argv[1] == "doNothing":
    method = doNothing
  elif sys.argv[1] == "onlyC":
    method = onlyC
  elif sys.argv[1] == "basicProg":
    method = basicProg
  elif sys.argv[1] == "bestProg":
    method = bestProg
  elif sys.argv[1] == "hybrid":
    method = hybrid


outlog = open(sys.argv[1] + ".csv","w")
outlog.write("real:guess:accuracy (" + sys.argv[1] + ")\n")

for fname in os.listdir("chord_data/"):

  key = open("chord_data/" + fname, "r").readlines()[1].split()[13]
  chord_trans = progs.transpose(chord_progs, key)
 
  val = analyze.run(fname, method, chord_trans, outlog)
  
  cnt += val
  tot += 1
  print val

outlog.close()
print '\n'
print cnt/tot
