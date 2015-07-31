# perform linear regression and see what happens. 
# the data is already labelled, just plug everything in.
import os
import nltk
import pdb
import random

from markov import Markov
from cof import COF

from nltk.classify.scikitlearn import SklearnClassifier


# fname -> List of labelled pairs
def getPairs(file_name):
  fname = "chord_data/" + file_name
  pairs = {}

  # the label including #/b. used later
  mainLabel = {}

  for line in open(fname,"r").readlines()[1:]:
    contents = line.split()


    # this line will take into account duplicates. aka two quarter note C's != one half note C
    cnt = int(contents[4])
    for i in range(cnt):
      bar = contents[0]
      pitches = [pitch for pitch in contents[15:]]
      data = contents[4:12] + [contents[13]] + [pitches] + [float(contents[3])/cnt]
      
      # ignore #/b
      label = contents[12][0]

      mainLabel[str((data,label))] = contents[12]

      pairs[file_name + "_" + bar] = pairs.get(file_name + "_" + bar, list()) + [(data,label)]
  
  return mainLabel, pairs

# dictionary of pairs -> feature list
def featureList(pairs):
  features = list()

  markov = Markov()

  # go from the feature representation of the data (features) to the original data (pairs)
  dataFeatToData = {}

  for bar in pairs:
    seq = pairs[bar]
    isoFeatures = {}
    isoFeaturesOrig = {}
    rootScores = {}

    # each data element should have as a features all the features of the previous and subsequent tslices
    for i in range(len(seq)):
      data,label = seq[i]
      chordKey = data[8]

      # isolated features
      isoFeatures[i] = (getIsoFeatures(data),label)
      isoFeaturesOrig[i] = isoFeatures[i]
      rootScores[i] = [int(dat) for dat in data[:7]]
  
    for i in range(len(seq)):
      
      # subsequent measure
      subBar = bar.split('_')[0] + str(int(bar.split('_')[1])+1)
      if i < len(seq) - 1:
        subFeats = dict([('S_' + key, val) for (key,val) in isoFeaturesOrig[i+1][0].items()])
        isoFeatures[i] = (dict(isoFeatures[i][0].items() + subFeats.items()) , isoFeatures[i][1])
       
      
      elif subBar in pairs:
        subFeats = dict([('S_' + key, val) for (kev,val) in getIsoFeatures(pairs[subBar][0][0]).items()])
        isoFeatures[i] = (dict(isoFeatures[i][0].items() + subFeats.items()) , isoFeatures[i][1])

      # prev measure
      prevBar = bar.split('_')[0] + str(int(bar.split('_')[1])-1)
      if i > 0:
        prevFeats = dict([('P_' + key, val) for (key,val) in isoFeaturesOrig[i-1][0].items()])
        #markovPred = markov.predict(chordKey, tuple([rootScores[i-1]]), markov.markov2)
        markovFeats = [('markov2', markov.predict(chordKey, tuple([rootScores[i-1]]), markov.markov2))]

        if i > 1:
          markovFeats += [('markov3', markov.predict(chordKey, tuple([rootScores[i-2], rootScores[i-1]]), markov.markov3))]

        if i > 2:
          markovFeats += [('markov4', markov.predict(chordKey, tuple([rootScores[i-3], rootScores[i-2], rootScores[i-1]]), markov.markov4))]

        isoFeatures[i] = (dict(isoFeatures[i][0].items() + prevFeats.items() + markovFeats) , isoFeatures[i][1])
        #isoFeatures[i] = (dict(isoFeatures[i][0].items() + prevFeats.items()) , isoFeatures[i][1])

      elif prevBar in pairs:
        prevFeats = dict([('P_' + key, val) for (kev,val) in getIsoFeatures(pairs[prevBar][-1][0]).items()])
        isoFeatures[i] = (dict(isoFeatures[i][0].items() + subFeats.items()) , isoFeatures[i][1])
    
      dataFeatToData[str(isoFeatures[i])] = seq[i]
    
    features += isoFeatures.values()

  
  return dataFeatToData, features



# features which don't take into account other data elements in the song
def getIsoFeatures(data):
  labels = [('Count', data[0]), ('key', data[8])]
  #labels += [('S'+str(i-1), int(data[i])) for i in range(1,8)]

  # pitches (no count)
  labels += [(pitch[1:], True) for pitch in data[9]]

  # pitches (> 1)
  labels += [('Strong-' + pitch[1:], True) for pitch in data[9] if int(pitch[0])>1]
  
  # bestRoot (doNothing equiv)
  roots = ('C','D','E','F','G','A','B')
  rootScores = [int(n) for n in data[1:8]]
  bestRoot = roots[rootScores.index(min(rootScores))]
  labels += [('best-root', bestRoot)]

  rootScores[rootScores.index(min(rootScores))] = 100
  sndBestRoot = roots[rootScores.index(min(rootScores))]
  labels += [('snd-best-root', sndBestRoot)]

  rootScores[rootScores.index(min(rootScores))] = 100
  thdBestRoot = roots[rootScores.index(min(rootScores))]
  labels += [('thd-best-root', thdBestRoot)]
  
  #COF
  cof = COF()
  labels += [('COF', cof.guessChord(data[8], [pitch[1:] for pitch in data[9]]))]

  labels += [('snd-COF', cof.guessChord2(data[8], [pitch[1:] for pitch in data[9]]))]
  return dict(labels)

pairs = {}
mainLabel = {}


for fname in os.listdir("chord_data/"):
  mainLabel_, pairs_ = getPairs(fname)
  mainLabel = dict(mainLabel.items() + mainLabel_.items())
  pairs = dict(pairs.items() + pairs_.items())


dataFeatToData, pairs = featureList(pairs)


random.shuffle(pairs)


split = 9*len(pairs)/10
(train, test) = (pairs[:split], pairs[split:])

# run markov analysis on training set
  


classifier = nltk.NaiveBayesClassifier.train(train)


print "REGRESSION ACCURACY:", nltk.classify.accuracy(classifier, test)

cnt, tot = 0,0

fC = open("right.csv","w")
fW = open("wrong.csv","w") 

fW.write("Prediction,Actual,Key,COF,Pitches\n")

for point in test:
  predict = classifier.classify(point[0])
  
  data = dataFeatToData[str(point)]
  label = mainLabel[str(data)]
  
  # decide on whether to add sharp, flat, or natural
  pitches = [pitch[1:].upper() for pitch in data[0][9]]
  for pitch in pitches:
    if predict in pitch:
      predict = pitch
  
  tot += 1
  
  if predict == label:
    cnt += 1
    fC.write(predict + "," + label + "," + data[0][8] + "," + str(data[0][9]) + "\n")
  else:
    fW.write(predict + ":" + label + ":" + data[0][8] + ":" + str(data[0][9]) + "\n")

print "FINAL ACCURACY:", float(cnt) / tot

fW.close()
fC.close()

cnt,tot = 0,0

for line in open("wrong.csv","r").readlines()[1:]:
  contents = line.split(",")
  if contents[0][0] == contents[1][0]:
    cnt += 1
  tot += 1

print "SHARP/FLAT ERRORS:", float(cnt)/tot
classifier.show_most_informative_features(20)
