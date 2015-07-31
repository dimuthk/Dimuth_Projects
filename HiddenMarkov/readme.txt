Hidden Markov Model (HMM) harmonization using the Viterbi algorithm. 

-Report: hmm.pdf

compiler.py :: takes song data from songs/ or songs_trans/ and constructs transition/emission matrices

debug.py :: allows you to look at song data more carefully; it can be easy to make mistakes when annotating

parsesong.py :: parsing method used in both compiler.py and run.py

prettyprint.py :: prints transition/emission matrices 

run.py :: using K-fold validation, runs compiler.py on dev set, then viterbi.py on test set, outputs accuracy

transpose.py :: song data is pretransposed to C maj in songs/; run transpose.py to write song data in original keys in songs_trans/

viterbi.py :: my implementation of viterbi algorithm
