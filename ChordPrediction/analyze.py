# this is the framework. take a single chord file and process it. 


def run(file_name, merge, progs_dict):
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
    merge(chord_dat, progs_dict)

    #make root predictions based on minimum counts
    for bar in chord_dat:
      for tslice in chord_dat[bar]:
        prediction[bar] = prediction.get(bar, list()) + [roots[tslice["roots"].index(min(tslice["roots"]))]]

    matches = 0
    total = 0

    #measure accuracy
    for bar in prediction:
      for i in range(len(prediction[bar])):
        if prediction[bar][i] == answers[bar][i][0]:
          matches += 1
        total += 1
    
    print prediction
    return float(matches)/total
