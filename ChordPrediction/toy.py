import pdb
import matplotlib.pyplot as plt

chords = ['A','B','C','D','E','F','G']
triads = {'C':['C','E','G'],'D':['D','F','A'],'E':['E','G','B'],'F':['F','A','C'],
  'G':['G','B','D'],'A':['A','C','E'],'B':['B','D','F']}

chordRef = {}
chordRef2 = {}
for chord in chords:
  chordRef[chord] = {}
  chordRef2[chord] = list()

for line in open("wrong.csv","r").readlines()[1:]:
  pred,actual,key,pitches = line.split(':')
  actual = actual[0]
  pred = pred[0]
  actual = pred
  
  pitches = [pitch.strip()[1:-1] for pitch in pitches[1:-2].split(',')]
  for pitch in pitches:
    try:
      chordRef[actual][pitch[1:]] = chordRef[actual].get(pitch[1:],0) + int(pitch[0])
      chordRef2[actual] = pred
    except:
      pdb.set_trace()

for chord in chords:
  if chordRef[chord] == {}:
    del chordRef[chord]

keys = chordRef.keys()
fout = open("wrongOut.csv","w")

plt.figure(figsize=(20,10))
i_=0
for key in chordRef:
  i_+=1
  line = ""
  fout.write(key + "\n")
  kk = sorted([k.upper() for k in chordRef[key].keys()])

  fout.write(','.join(kk) + "\n")
  values = list()
  for chord in kk:
    chord = chord.lower()
    if chord in chordRef[key]:
      values += [chordRef[key][chord]]
      line += str(chordRef[key][chord]) + ","
  
  plt.subplot(len(keys)/2,3,i_)
  plt.title(key)
  barlist = plt.bar(range(len(values)), values, align="center", color="k")
  plt.ylim([0,25])

  actualInd = set([i for i in range(len(kk)) if kk[i][0] in triads[key]])
  #predInd = set([i for i in range(len(kk)) if kk[i][0] in triads[chordRef2[key]]])
  #intersect = actualInd.intersection(predInd)
  #actualInd -= intersect
  #predInd -= intersect

  #for index in predInd:
  #  barlist[index].set_color('r')
  for index in actualInd:
    barlist[index].set_color('r')
  #for index in intersect:
  #  barlist[index].set_color('g')

  plt.xticks(range(len(values)),kk)
  line = line[:-1] + "\n"
  fout.write(line)
fout.close()
      
print chords   
plt.savefig('test.png')
  
