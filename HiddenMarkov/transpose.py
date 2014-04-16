from chordworkshop import ChordWorkshop
import pdb
import os
trans = ['C','C#','D','Eb','E','F','F#','G','Ab','A','Bb','B']
cnt = 0
for artist in os.listdir("songs/"):
    
    for song in os.listdir("songs/" + artist + "/"):
        if '.swp' in song:
          continue
        fout = open("songs_trans/" + artist + "/" + song,"w")
        lines = open("songs/" + artist + "/" + song,"r").readlines()

        print artist,song
        tab,inter = lines[0].split()
        inter = int(inter)


        fout.write(tab + " " + trans[inter] + "\n")
        
        cw = ChordWorkshop()
        for line in lines[1:]:
          mark, type_, notes = line.split()
          if type_ == "M":
            notes = list(notes)
            newNotes = ''.join(cw.transposeNotes(notes,inter))
            fout.write(mark + "\t" + type_ + "\t" + newNotes + "\n")

          elif type_ == "C":
            chords = list(notes)
            marks = list()
            for i in range(len(chords)):
              if chords[i].islower():
                chords[i] = chords[i].upper()
                marks.append(i)

            newChords = cw.transpose(chords,inter)
            for i in marks:
              newChords[i] = newChords[i].lower()
            
            newChords = ''.join(newChords)
            fout.write(mark + "\t" + type_ + "\t" + newChords + "\n")

          else:
            fout.write(line)
        fout.close()
