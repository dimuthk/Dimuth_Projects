# analyze wrong.csv 

cnt,tot = 0,0

for line in open("wrong.csv","r").readlines():
  contents = line.split(":")
  if contents[0][0] == contents[1][0]:
    cnt += 1
  tot += 1

print "NON SHARP/FLAT ERRORS:", float(cnt)/tot


