Chord prediction using lyric sentiments. Tries to find correlation between lyrical sentiment and sentimentally driven chord progressions. 

-Report: lyrics.pdf

chord_grab.py :: For a given chord progression, retrieve matching songs from chordhunter.com

chordworkshop.py :: various functions for working with chords in main.py

combine_data.py :: merge data collected from chord_grab.py and get_lyrics.py

extract_prog.py :: extracts the dominant chord progression from a set of chords

get_lyrics.py :: For a set of songs collected from chord_grab.py, retrieve associated lyrics from metrolyrics.com

main.py :: Runs naive bayes classifier using k-fold validation on collected data. classifier modules are also defined here.

sentiment.py :: word sentiment processor using movie corpus from nltk movie reviews. used in main.py
