# extract the dominant chord progression from a set of chords/
import pdb
import chordworkshop
#progression:: >= 3 chords

song_prog = {}
fname = "four_chords_trans.txt"

def extract(chords):
	progs = {}
	groups = chords

	'''
	for i in range(1,4):
		groups = zip(groups, chords[i:])
		for i in range(len(groups)):
			#flatten
			tmp = tuple()
			for chord in groups[i]:
				if type(chord) == tuple:
					tmp += tuple([el for el in chord])
				else:
					tmp += (chord,)
			groups[i] = tmp
			#groups[i] = tuple([el for tup in groups[i] for el in tup])
			progs[groups[i]] = progs.get(groups[i],0) + 1
	'''

	groups = zip(chords,chords[1:],chords[2:],chords[3:])
	for group in groups:
		group = tuple(group)
		progs[group] = progs.get(group,0)+1
	choices = {}
	for prog,cnt in progs.items():
		if cnt > 1:
			choices[cnt] = choices.get(cnt,list()) + [prog]
	
	if len(choices.items()) == 0:
		return ("",)
	choices_ord = sorted(choices.items(), key=lambda k:k[0], reverse=True)
	first = max(choices_ord[0][1], key=lambda prog:len(prog))
	for cnt,progs in choices_ord:
		best = max(progs, key=lambda prog: len(prog))
		if len(best) > 2:
			return best
	return first

fnew = open(fname,"w")
cw = chordworkshop.ChordWorkshop()
for line in open("all_songs.txt","r"):
	artist,song,chord_str = line.split(";")
	chords = cw.fixChords(chord_str[:-1])
	baseKey = cw.guessBaseKey(chords)
	interval = cw.interval('C', baseKey[0])
	prog = extract(chords)
	if '' in prog:
		continue
	prog = tuple(cw.transpose(prog, interval))
	song_prog[prog] = song_prog.get(prog,0) + 1
	prog = reduce(lambda x,y:x+','+y,prog)
	
	
	fnew.write(artist+";"+song+";"+prog+"\n")
fnew.close()
