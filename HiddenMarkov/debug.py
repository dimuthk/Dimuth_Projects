# debug chord/melody pairs because it can be tough to figure out sometimes
import sys

path = "songs/"

if len(sys.argv) == 1:
  print "use case: <artist/song_path> <specific_part (optional)>"
  exit(1) 

f_name = sys.argv[1]

lines = open(path+f_name,"r").readlines()

npm = int(lines[0].split()[0])
chords = {}
melodies = {}

def remove_nums(seq):
  new_seq = ""
  for char in seq:
    if not char.isdigit():
      new_seq += char
  return new_seq


for line in lines[1:]:
  name, typ, seq = line.strip().split()
  if typ == "M":
    melodies[name] = remove_nums(seq)
  elif typ == "C":
    chords[name] = seq


if len(sys.argv) > 2:
  query = sys.argv[2]
  chords = {query: chords[query]}

for name in chords:
  print name
  for i in range(len(chords[name])):
    print chords[name][i] + " -> " + melodies[name][i*npm:(i+1)*npm]
    
  
      

  
