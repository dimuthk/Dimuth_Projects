# you need to figure a way to get lyrics for the songs.

from bs4 import BeautifulSoup as bs
import urllib2
import pdb
import os

misses = 0

def get_lines(artist,song):
    global misses

    #process artist, song names
    old = (artist,song)
    artist = "-".join(artist.lower().split())
    song = "-".join(song.lower().split())
    url = "http://www.metrolyrics.com/" + song + "-lyrics-" + artist + ".html"
    #url = "http://www.metrolyrics.com/grenade-lyrics-bruno-mars.html"
    try:
      response = urllib2.urlopen(url)
    except:
      misses += 1
      return ["N/A"]
    soup = bs(response.read())
    
    lyrics_dat = soup.findAll('div', id="lyrics-body")
    if len(lyrics_dat) == 0:
      misses += 1
      return ["N/A"]

    song_lines = list()
    for verse in lyrics_dat[0].findAll('p'):
      verse = str(verse)
      if "<p class=\"writers\">" in verse:
        continue
      verse = verse.replace("<br/>","").replace("<p class=\"verse\">","").replace("</p>","")
      song_lines += verse.split('\n')

    return song_lines

# don't reprocess old songs.
finished = set()
good = 0

if os.path.isfile("lyrics.txt") == False:
  f = open("lyrics.txt","w")
  f.close()

for line in open("lyrics.txt","r+"):
  artist, song, lyrics = line.split("<>")
  finished.add((artist,song))
  if lyrics != "[\'N/A\']\n":
    good += 1

newSongs = list()
for line in open("all_songs.txt","r"):
  artist, song, _ = line.split(';')
  if (artist,song) not in finished:
    newSongs.append((artist,song))

f_out = open("lyrics.txt","a")
for artist, song in newSongs:
  lines = get_lines(artist, song)
  if lines == ["N/A"]:
    print "missed " + song + " by " + artist
  else:
    print "got " + song + " by " + artist
  f_out.write(artist + "<>" + song + "<>" + str(lines) + "\n")

f_out.close()
print "processed", len(newSongs), "new songs"
print misses, "new songs resulted in N/A"
print "we have",good + len(newSongs) - misses,"usable songs altogether"
