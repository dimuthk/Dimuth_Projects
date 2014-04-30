import pdb

def transpose(chords,key):
  chord_ref = ("A","B","C","D","E","F","G")
  numerals = ("I","II","III","IV","V","VI","VII")

  offset = chord_ref.index(key)
  chords_trans = {}

  for prog in chords:
    try:
      new_prog = tuple([chord_ref[(numerals.index(chord) + offset) % len(numerals)] for chord in prog])
      chords_trans[new_prog] = chords[prog]
    except:
      a = 2

  return chords_trans

def getProgs(fname):
  numerals = ("I","II","III","IV","V","VI","VII")

  chords = {}

  for line in open(fname, "r"):
    contents = line.upper().split()
    prog = contents[1:]
    for i in range(len(prog)):
      chord = prog[i]

      pieces = chord.split("/")
      chord = pieces[0]
      for j in range(len(pieces))[1:]:
        rel = pieces[j]
        chord = numerals[(numerals.index(rel.upper()) + numerals.index(chord.upper())) % len(numerals)]
      
      if pieces[-1].islower():
        chord = chord.lower()

      prog[i] = chord

    chords[tuple(prog)] = chords.get(tuple(prog),0) + int(contents[0])

  return chords
