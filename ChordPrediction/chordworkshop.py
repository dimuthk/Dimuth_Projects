#make an intelligent base key guesser
import pdb

class ChordWorkshop():
#root everything in A major
  def __init__(self):
    self.keys = ['A','Bb','B','C','C#','D','Eb','E','F','F#','G','Ab']
    self.base_chords_major = ['A','B','C#','D','E','F#']
    self.base_chords_minor = ['A','C','D','E','F','G']
    self.addons_major = ['','m','m','','','m']
    self.addons_minor = ['m','','m','m','','']


    self.guesses = {}
    for i in range(len(self.keys)):
      key = self.keys[i]
      self.guesses[key] = self.shiftup(i, True)
      self.guesses[key +'m'] = self.shiftup(i, False)


  # keep the # and b notations consistent.
  def regulate(self, chord):
    if chord == 'A#':
      return 'Bb'
    elif chord == 'Db':
      return 'C#'
    elif chord == 'D#':
      return 'Eb'
    elif chord == 'Gb':
      return 'F#'
    elif chord == 'G#':
      return 'Ab'
    else:
      return chord

  # jazz chords are 7ths, 9sus4, 7sus9
  def isJazzChord(self, chord):
    return 'sus' in chord or '7' in chord or '9' in chord or 'add' in chord
     
    

  # return the interval distance between two notes
  def interval(self, note1, note2):
    return (self.keys.index(note1) - self.keys.index(note2)) % len(self.keys)  


  def shiftup(self,amount,major):
    new = list()
    chord_set = self.base_chords_major if major == True else self.base_chords_minor
    addons = self.addons_major if major == True else self.addons_minor
    for i in range(len(chord_set)):
      chord = chord_set[i]
      index = self.keys.index(self.regulate(chord))
      newIndex = (index + amount) % len(self.keys)
      new.append(self.keys[newIndex] + addons[i])
    return set(new)
   
  #get rid of all details on the chord except for key and if 
  #it's major or minor
  def removeExtra(self,chord):
    cutoff = 1
    if '#' in chord:
      cutoff += 1
    if 'b' in chord:
      cutoff += 1
    if 'm' in chord:
      cutoff += 1
    return chord[:cutoff]


  def transpose(self, chords, inter):
    #baseKey = self.guessBaseKey(chords)
    addons = list()
    for chord in chords:
	if 'm' in chords:
		addons.append('m')
	else:
		addons.append('')
    chords = [chord[0] for chord in chords]    
    # inter = self.interval('C', baseKey[0])    
    for i in range(len(chords)):
      chord = chords[i]
      index = self.keys.index(chord)
      newIndex = (index + inter) % len(self.keys)
      chords[i] = self.keys[newIndex] + addons[i]
      
    return chords


  #given all possible chord sets using the shiftup function,
  #find the most likely match for the chord set.
  def guessBaseKey(self, chords):
    cleaned_chords = set([self.removeExtra(chord) for chord in chords])
    return max(self.guesses.keys(), key = lambda k: len(cleaned_chords.intersection(self.guesses[k])))

  def isCreepy(self,chord):
    return 'm' in chord and '6' in chord

  def isRegMinor(self, key):
    if 'm' not in key:
      return False
    for i in key:
      if i.isdigit():
        return False
    return True

  def isMinor(self, key):
    return 'm' in key

  #chords may be badly formatted, clean them up. also, return
  #as list, not string.
  def fixChords(self,chords):
    chords = chords.split(',')
    goodChords = list()
    for chord in chords:
      start = chord[0].upper()
      if not (ord(start) >= 65 and ord(start) <= 71):
        continue
      
      chord = start + chord[1:].lower()
      if 'b' in chord or '#' in chord:
        chord = self.regulate(chord[:2]) + chord[2:]
      
      chord = chord.replace('maj','')
      chord = chord.replace('min','m')
      goodChords.append(chord)
    return goodChords
