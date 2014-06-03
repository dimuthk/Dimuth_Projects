import pdb

def transpose(chords,key):
  chord_ref = ("A","B","C","D","E","F","G")
  numerals = ("I","II","III","IV","V","VI","VII")

  try:
    offset = chord_ref.index(key.upper()[0])
  except:
    pdb.set_trace()
  chords_trans = {}

  for chord_len in chords.keys():
    chords_trans[chord_len] = {}
    for prog in chords[chord_len]:
      try:
        new_prog = tuple()
        for chord in prog:
          chord = chord.split('[')[0]
          new_prog += (chord_ref[(numerals.index(chord) + offset) % len(numerals)],)
        #new_prog = tuple([chord_ref[(numerals.index(chord) + offset) % len(numerals)] for chord in prog])
        chords_trans[chord_len][new_prog] = chords[chord_len][prog]
      except:
        pdb.set_trace()

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
