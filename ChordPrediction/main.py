# after getting chord-lyric pairs, do all the main stuff
# here. 
import pdb
#import numpy as np
#from nltk.stem.wordnet import WordNetLemmatizer
#from nltk.stem import PorterStemmer
#from nltk.corpus import movie_reviews
from collections import Counter
from chordworkshop import ChordWorkshop
#import nltk
import re
import random
import marshal
import os
from sklearn import svm

cw = ChordWorkshop()
song_lyr = {}
for line in open("lyrics.txt","r"):
  artist, song, lyrics = line.split("<>")
  if lyrics[:-1] != "['N/A']":
    song_lyr[(artist,song)] = lyrics[:-1]

pairs = list()
pairToArtist = {}
for line in open("all_songs.txt","r"):
  artist, song, chords = line.split(";")
  if (artist,song) in song_lyr:
    pairs.append((song_lyr[(artist,song)], chords[:-1]))
    pairToArtist[song_lyr[(artist,song)]] = (artist,song)

pairs = [(lyrs,cw.fixChords(chords)) for lyrs,chords in pairs]
#you now have the raw lyric-chord pairs.

#get sentiment words from ashwin


class ChordFeats():
  def __init__(self):
    self.cw = ChordWorkshop()

  #BINARY CLASSIFIERS - classify between happy and sad

  #if percentage of minor chords exceeds desired threshold,
  #song is considered sad
  def numMinorChords(self,chords):
    minCnt = 0
    majCnt = 0
    for chord in chords:
      if cw.isMinor(chord):
        minCnt += 1
      else:
        majCnt += 1
    if float(minCnt)/(minCnt+majCnt) < .30:
      return 'happy'
    return 'sad'

  #if the base key of the song is minor, song is sad
  def baseKey(self,chords):
    base_key = self.cw.guessBaseKey(chords)
    if cw.isRegMinor(base_key):
      return 'sad'
    return 'happy'

  #TERTIARY CLASSIFIERS - classify between sad, cool, happy
  def numJazzMinorChords(self,chords):
    minCnt, majCnt, coolCnt, creepyCnt = 0,0,0,0
    for chord in chords:
      if cw.isJazzChord(chord):
        coolCnt += 1
      elif cw.isRegMinor(chord):
        minCnt += 1
      #elif cw.isCreepy(chord):
      #  creepyCnt += 1
      else:
        majCnt += 1

    #if float(creepyCnt)/len(chords) > .15:
   #   return 'creepy'
    if float(coolCnt)/len(chords) > .25:
      return 'cool'
    elif float(minCnt)/len(chords) > .25:
      return 'sad'
    else:
      return 'happy'

class LyricFeats():
  def __init__(self,song_lyr):
    negids = movie_reviews.fileids('neg')
    posids = movie_reviews.fileids('pos')
    self.negfeats = [movie_reviews.words(fileids=[f]) for f in negids]
    self.posfeats = [movie_reviews.words(fileids=[f]) for f in posids]
    self.stopwords = set(nltk.corpus.stopwords.words('english'))
    if not os.path.isfile('sentiment_dict'):
      print 'run sentiment.py first'
      exit(0)
    #self.sentiment = marshal.load(open('sentiment_dict','rb'))

    self.sentiment = {}
    for line in open("ashwin.txt","r"):
     artist, song, sentiment = line.split("<>")
     self.sentiment[(artist,song)] = sentiment


    self.song_lyr = song_lyr
	
  #we can get sentiment values for words. should we care about the
  #average sentiment, best, or worst? let's do all three. do them 
  #in gradients rounded to the nearest tenth.
  def sentiments(self,lyrics):
    
    return 0  

#baseline feature; return occurence of each non stopword.
  def bagOfWords(self,lyrics):
    words = lyrics[1:-1].split()
    lmtzr = WordNetLemmatizer()
    #cleanup
    words = [re.sub(r'\W+','', word).lower() for word in words]
    stemmer = PorterStemmer()
    words = set([stemmer.stem(word) for word in words])
    words = filter(lambda word: word not in self.stopwords, words)
    return Counter(words)

  def combined(self,lyrics):
    #define your features here
    features = [self.sentiments]
    final = list()
    for feat in features:
      final += feat(lyrics).items()
    return dict(final)


 

#SELECT DESIRED FEATURES
chordFeatures = ChordFeats().numMinorChords
lyricFeatures = LyricFeats(song_lyr).combined

f_out = open("song_classifications.txt","w")
pairs_c = list()
for i in range(len(pairs)):
  lyrics, chords = pairs[i]
  newPair = (lyricFeatures(lyrics), chordFeatures(chords))
  #retain artist info
  pairs_c.append(newPair)
  f_out.write(str(pairToArtist[lyrics]) + '\t' + newPair[1] + '\n')
f_out.close()

random.shuffle(pairs_c)
classifier = nltk.NaiveBayesClassifier.train(pairs_c[:5*len(pairs_c)/6])
res = [classifier.classify(lyr) for lyr,_ in pairs_c]
print nltk.classify.accuracy(classifier, pairs_c[5*len(pairs_c)/6:])
classifier.show_most_informative_features()

print 'classification ratios:'
cnter = Counter()
for _,chord in pairs_c:
  cnter[chord] += 1
print cnter

clf = svm.SVC()

pdb.set_trace()
