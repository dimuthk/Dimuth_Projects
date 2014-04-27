# gets songs and their chord progressions from chordhunter.com. i hate parsing...

from bs4 import BeautifulSoup as bs
import urllib2
import pdb
import sys

if len(sys.argv) == 1:
  print "use case: <C1+C2+C3+...>"
  exit(0)

query = sys.argv[1]
url = "http://www.chordhunter.com/tune_search.php?q=" + query
response = urllib2.urlopen(url)
soup = bs(response.read())

path = "raw_dat/"
f_name = "query_" + '_'.join(query.split('+'))
f_out = open(path + f_name,"w")

tot = 0
# get the stuff. SO EASY! ignore first one.
for song_dat in soup.findAll('tr')[1:]:
  dat = song_dat.findAll('td')
  if len(dat) != 5:
    continue
  tot += 1
  artist, song, _, chords, _ = dat
  
  chords = str(chords)[10:-12].replace("[","").replace("]","").replace("\"","")
  chords = ','.join(chords.split())
  f_out.write(artist.string.strip() + ";" + song.string.strip() + ";" + chords.strip() + "\n")


#0,1,2,3,4 -> artist,song,link,chords,deltas
'''
i = 0
tot = 0
for td in soup.findAll('td'):
  if i == 0:
    artist = td.string.strip()
  elif i == 1:
    song = td.string.strip()
  elif i == 3:
    chords = str(td)[10:-12].replace("[","").replace("]","").replace("\"","").strip()
    f_out.write(artist + ";" + song + ";" + chords + "\n")
    tot += 1
  if i == 4:
    assert(td.string == "Deltas")
  i += 1
  if i == 5:
    i = 0
f_out.close()
'''
print "got ", tot, " songs"
