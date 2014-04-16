# intermediate step; make a file that combines all the data from raw_data.
import os
import pdb

f_out = open("all_songs.txt","w")

path = "raw_dat/"
songs = list()
for f in os.listdir(path):
  lines = open(path+f,"r").readlines()
  
  for line in lines:
    artist, song, chords = line.split(";")
    songs.append((artist,song,chords))

# check
check_songs = set()
for artist, song, _ in songs:
  check_songs.add((artist, song))
assert(len(check_songs) == len(set(songs)))

for artist, song, chords in set(songs):
  f_out.write(artist + ";" + song + ";" + chords)

f_out.close()

print "unique: ", len(set(songs))
print "repeats: ", len(songs) - len(set(songs))
