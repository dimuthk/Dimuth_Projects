# this is the framework. take a single chord file and process it. 
import pdb

def run(file_name, merge, progs_dict, fout):
    fname = "chord_data/" + file_name

    chord_dat = {}
    answers = {}

    for line in open(fname, "r").readlines()[1:]:
      contents = line.split()
      contents[:12] = [float(val) for val in contents[:12]]
      chord_dat[contents[0]] = chord_dat.get(contents[0], list()) + [{"pos":contents[1:5], "roots":contents[5:12], "notes":contents[15:]}]
      answers[contents[0]] = answers.get(contents[0], list()) + [contents[12] + contents[14]]

    # data collected. are we gonna build a graph? what are we gonna do? 
    # we have a primary set of nodes. start by just merging bars. 
    # when I say merge I mean: modify the individual root scores based on
    # progression probabilities. 

    roots = ('C','D','E','F','G','A','B')
    prediction = {}

    #perform all merges
    merge(chord_dat, progs_dict, answers)

    #make root predictions based on minimum counts
    for bar in chord_dat:
      for tslice in chord_dat[bar]:
        prediction[bar] = prediction.get(bar, list()) + [roots[tslice["roots"].index(min(tslice["roots"]))]]

    matches = 0
    total = 0


    #measure accuracy
    for bar in prediction:
      matches_in = 0
      total_in = 0
      for i in range(len(prediction[bar])):
        if prediction[bar][i] == answers[bar][i][0]:
          matches += 1
          matches_in += 1

        total += 1
        total_in += 1

      ans = [v[0] for v in answers[bar]]
      #isCorrect = reduce(lambda x,y: x and y, [ans[j] == prediction[bar][j] for j in range(len(ans))])
      minArr = [min(chord_dat[bar][j]['roots']) for j in range(len(chord_dat[bar]))]
      fout.write(str(ans) + ":" + str(prediction[bar]) + ":" + str(float(matches_in)/total_in) + ":" + str(minArr) + "\n")
      
    ftemp = open("TEST.csv", "w")
    for bar in chord_dat:
      for j in range(len(chord_dat[bar])):
        ftemp.write(str(min(chord_dat[bar][j]["roots"])) + ":" + str(1 if prediction[bar][j] == answers[bar][j][0] else 0)  + ":" + prediction[bar][j] + ":" + answers[bar][j][0] + "\n")

    return float(matches)/total
