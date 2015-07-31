import os
import marshal
import numpy as np
from collections import Counter
import pdb
import sys
from parsesong import process

out_path = "transitions/"
sing_dat_B = {} #chord to melody
int_dat_B = {}
sing_dat_A = list() #chord to chord
int_dat_A = list()

def compile(K, noScale, path):
    # outfile: a dict object where for every chord, there's a sequence of notes

    # ALL SONGS MUST BE RECORDED IN THE FOLLOWING FORMAT:
    # 4/4 time, 2 chords recorded for every measure. also, a number at the beginning 
    # designating how many notes pertain to each chord. so intuitively, 4 -> notes are
    # recorded as eighth notes, 8 -> 16th notes, and so on. highest number is 8!

    # special chord mappings:
    # Ab -> Z
    # Bb -> X
    # Eb -> V
    # F# -> N
    # C# -> M

    # special note mappings:
    # Ab -> a
    # F# -> f
    # Eb -> e
    # C# -> c
    # Bb -> b

    # record the special chord mappings separately. these isn't used functionally,
    # just when you want to view the chords with a more intuitive label.
    real_chords = {"A":"A","B":"B","C":"C","D":"D","E":"E","F":"F","G":"G","Z":"Ab","X":"Bb","V":"Eb", \
                  "N":"F#","M":"C#","a":"Am","b":"Bm","c":"Cm","d":"Dm","e":"Em","f":"Fm","g":"Gm", \
                  "z":"Abm","x":"Bbm","v":"Ebm","n":"F#m","m":"C#m"}
    marshal.dump(real_chords, open(out_path+"real_chords","wb"))
    real_notes = {"c":"C#","d":"C#","e":"Eb","b":"Bb","f":"F#","g":"Ab","a":"Ab"}
    marshal.dump(real_notes, open(out_path+"real_notes","wb"))

    chd_sym = {} #chord to representative symbol
    z = 33
    def convert_seq(seq,z):
      if noScale == True:
        return [item for item in seq if item.isdigit() == False]
      
      print "TEST"
      # first, recognize the progressions of everything.
      octave = ""
      new_seq = ""
      for char in seq:
        if char.isdigit():
          octave = char
        else:
          new_seq += char + octave
      # next, convert the notes into symbols. 
      pairs = [''.join(pair) for pair in zip(new_seq[0::2], new_seq[1::2])]
      good_seq = ""
      for pair in pairs:
        if pair not in chd_sym:
          chd_sym[pair] = chr(z)
          z += 1
        good_seq += chd_sym[pair]
      return good_seq, z

    
    melodyListInt = list()
    melodyListSong = list()

    # compile data for every song, every artist, or based on K-fold
    for artist in os.listdir(path):
      for song in os.listdir(path + artist + "/"):
        if song.endswith(".swp"):
          continue

        
        
    
        lines = open(path + artist + "/" + song,"r").readlines()
        melody, chords, cnt, chd_sym, z = process(lines, chd_sym, z, noScale)
        for m in melody:
            if 'I' in m:
                melodyListInt.append((melody[m], chords[m], cnt[m]))
            elif 'S' in m:
                melodyListSong.append((melody[m], chords[m], cnt[m]))

    meas_cnt = 0
    for melody,chordProg,cnt in melodyListInt:
        meas_cnt += 1
        if meas_cnt % (K-1) == 0:
          continue

        for j in range(cnt):
              int_dat_A.append(chordProg)
              
        for i in range(len(chordProg)):
            seq = melody[i*8:i*8+8]
            chord = chordProg[i]
            if chord not in int_dat_B:
                int_dat_B[chord] = list()
            for j in range(cnt):
                int_dat_B[chord].append(seq)

    meas_cnt = 0
    for melody,chordProg,cnt in melodyListSong:
        meas_cnt += 1
        if meas_cnt % (K-1) == 0:
          continue
        
        for j in range(cnt):
              sing_dat_A.append(chordProg)
              
        for i in range(len(chordProg)):
            seq = melody[i*8:i*8+8]
            chord = chordProg[i]
            if chord not in sing_dat_B:
                sing_dat_B[chord] = list()
            for j in range(cnt):
                sing_dat_B[chord].append(seq)  


    # use the information collected about A and B to construct the real
    # matrices. 


    def chord_index(chords_list):
      allChords = ['A','X','B','C','M','D','V','E','F','N','G','Z']
      allChords += ['a','x','b','c','m','d','v','e','f','n','g','z']

      c_index = {}
      i = 0
      for chords in chords_list:
        for chord in chords:
          if chord not in c_index:
            c_index[chord] = i
            i += 1
      #for i in range(len(allChords)):
      #  c_index[allChords[i]] = i
      return c_index

    # build transition matrices from dict data
    def A_mat(chords_list, c_index):
      mat = A_cnt(chords_list, c_index)
      # normalize
      for i in range(len(mat)):
        mat[i] /= float(sum(mat[i]))

      return mat

    # for stats purposes
    def A_cnt(chords_list, c_index):
      mat = np.zeros((len(c_index),len(c_index)))
      # get transition chords
      for chords in chords_list:
        for i in range(len(chords)-1):
          mat[c_index[chords[i]], c_index[chords[i+1]]] += 1

      # smoothen
      for i in range(len(c_index)):
        for j in range(len(c_index)):
          mat[i,j] += 1

      return mat

    def melody_index(chords_dict):
      m_index = {}
      i = 0

      for chord in chords_dict:
        for seq in chords_dict[chord]:
          # turn the sequence into a counter vector
          seq_hashable = tuple(sorted(seq))
          if seq_hashable not in m_index:
            m_index[seq_hashable] = i
            i += 1

      return m_index


    def init_probs(chords_list, c_index):
      init = np.zeros(len(c_index))
      for chords in chords_list:
        start_chord = chords[0]
        init[c_index[start_chord]] += 1

      init /= float(sum(init))

      return init

    def B_cnt(chords_dict, c_index, m_index):
      mat = Counter()
      for chord in chords_dict:
        i = c_index[chord]
        for seq in chords_dict[chord]:
          seq_hashable = tuple(sorted(seq))
          j = m_index[seq_hashable]
          mat[(i,j)] += 1
      return mat

    # build chord guess matrices from dict data
    def B_mat(chords_dict, c_index, m_index):  
      # B cannot be a traditional matrix as it's too large and sparse;
      # instead, make a dictionary where dict[(i,j)] leads to the i,jth
      # entry, which gives the probability of melody[j] given chord[i].
      # you already have the chords. develop a melody permutation that lets
      # you automatically assign a melody (order doesn't matter) to an index.

      mat = B_cnt(chords_dict, c_index, m_index)
      # normalize
      totals = Counter()
      for i,j in mat:
        totals[i] += mat[(i,j)]

      finalMat = {}
      for i,j in mat:
        finalMat[(i,j)] = float(mat[(i,j)]) / totals[i]
      
      #return mat
      return finalMat

    c_indexInt = chord_index(int_dat_A)
    m_indexInt = melody_index(int_dat_B)
    int_A = A_mat(int_dat_A, c_indexInt)
    int_B = B_mat(int_dat_B, c_indexInt, m_indexInt)
    initInt = init_probs(int_dat_A, c_indexInt)

    c_indexSong = chord_index(sing_dat_A)
    m_indexSong = melody_index(sing_dat_B)
    song_A = A_mat(sing_dat_A, c_indexSong)
    song_B = B_mat(sing_dat_B, c_indexSong, m_indexSong)
    initSong = init_probs(sing_dat_A, c_indexSong)
    marshal.dump(c_indexInt, open(out_path + "c_indexInt","wb"))
    marshal.dump(m_indexInt, open(out_path + "m_indexInt","wb"))
    marshal.dump(int_A.tolist(), open(out_path + "int_A","wb"))
    marshal.dump(int_B, open(out_path + "int_B","wb"))
    marshal.dump(initInt.tolist(), open(out_path + "initInt","wb"))
    marshal.dump(c_indexSong, open(out_path + "c_indexSong","wb"))
    marshal.dump(m_indexSong, open(out_path + "m_indexSong","wb"))
    marshal.dump(song_A.tolist(), open(out_path + "song_A","wb"))
    marshal.dump(song_B, open(out_path + "song_B","wb"))
    marshal.dump(initSong.tolist(), open(out_path + "initSong","wb"))
    marshal.dump(chd_sym, open(out_path + "chd_sym","wb"))

    # print statistics
    #print "Trained on " + str(len(songs)) + " out of " + str(song_cnt) + " songs"
    print "INTERLUDE DATA, VOICE DATA"
    print "chords recognized:\t", len(c_indexInt.keys()), len(c_indexSong.keys())
    print "unique melodies:\t", len(m_indexInt.keys()), len(m_indexSong.keys())
    intDat = B_cnt(int_dat_B, c_indexInt, m_indexInt)
    songDat = B_cnt(sing_dat_B, c_indexSong, m_indexSong)
    print "total melodies:\t\t", sum(intDat.values()), sum(songDat.values())
    return 0
