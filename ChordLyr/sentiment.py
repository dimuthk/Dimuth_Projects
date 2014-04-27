
#build a simple sentiment processor from the movie corpus.
#for a given word, give a ratio of it's positivity, aka
#number of positive occurences over total occurences. 

from nltk.corpus import movie_reviews
from nltk.stem import PorterStemmer
from collections import Counter
import pdb
import marshal

negids = movie_reviews.fileids('neg')
posids = movie_reviews.fileids('pos')

posCnt = Counter()
totCnt = Counter()
positivity = {}
stemmer = PorterStemmer()

for pid in posids:
  words = movie_reviews.words(pid)
  words = [stemmer.stem(word) for word in words]
  for word in words:
    posCnt[word] += 1
    totCnt[word] += 1

for nid in negids:
  words = movie_reviews.words(nid)
  words = [stemmer.stem(word) for word in words]
  for word in words:
    totCnt[word] += 1

for word in totCnt:
  positivity[word] = float(posCnt[word]) / totCnt[word]

marshal.dump(positivity, open('sentiment_dict','wb'))
