import pdb

#process an inputted song. input the song as the lines of the file
def convert_seq(seq,chd_sym, z, noScale):
      if noScale == True:
        good_seq = ""
        for char in seq:
          if char.isdigit():
            continue

          if char not in chd_sym:
            chd_sym[char] = chr(z)
            z += 1
          good_seq += chd_sym[char]
        return good_seq, chd_sym, z
        #return [item for item in seq if item.isdigit() == False]
   
      # first, recognize the progressions of everything.
      octave = ""
      new_seq = ""
      for char in seq:
        if char.isdigit():
          octave = char
        elif char == 'R': #special case
          new_seq += char + '0'
        else:
          new_seq += char + octave
      # next, convert the notes into symbols. 
      pairs = [''.join(pair) for pair in zip(new_seq[0::2], new_seq[1::2])]
      good_seq = ""
      for pair in pairs:
        if pair not in chd_sym:
          try:
            chd_sym[pair] = chr(z)
          except:
            pdb.set_trace()
          z += 1
        good_seq += chd_sym[pair]
      return good_seq, chd_sym, z


def process(lines, chd_sym, z, noScale):
  melody, chords, cnt  = {},{},{}
  npm = int(lines[0].strip().split()[0])
        
  # the base note of the recordings are can vary, but when analyzing it must be 
  # consistent. Everything must be in 16th notes, so a value of 8.
  mult = 8 / npm
        
  # parse the information from your documents. 
  for line in lines[1:]:
    phrase, typ, seq = line.split()
    #melodies require further processing
    if typ == "M":
      seq = ''.join([item*mult for item in seq])
      new_seq, chd_sym, z = convert_seq(seq, chd_sym, z, noScale)
      assert(len(new_seq) == len([i for i in seq if i.isdigit()==False]))
      melody[phrase] = new_seq
    elif typ == "C":
      chords[phrase] = seq
    else:
      cnt[phrase] = int(seq)

    # check for proper formatting
    for phrase in chords:
      if 8 * len(chords[phrase]) != len(melody[phrase]):
        raise ValueError("bad formatting on phrase " + phrase + \
          " in " + song + " by " + artist + ": " + str(len(chords[phrase])) + \
          " chords and " + str(len(melody[phrase])) + " notes")
  
  return melody, chords, cnt, chd_sym, z
