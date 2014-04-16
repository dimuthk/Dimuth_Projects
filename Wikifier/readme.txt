Wikify an input document. 

-Report: wikifier.pdf

RETRIEVING HYPERLINK INFORMATION FROM WIKIPEDIA.ORG

Step 1
strip_wikiset.py:     enwiki-latest-pages-articles.xml -> wiki_sub/*
get_redirects.py:     enwiki-latest-pages-articles.xml -> redirects

Step 2
get_anchors.py:       wiki_sub/*, redirects -> anchors_anum/*

Step 3
get_anchor_cnts.py:   wiki_sub/*, redirects, anchors_anum/* -> anchors_tally/*

Step 4
get_link_prob.py:     anchors_anum/*, anchors_tally/* -> anchors_link_prob

Check
confirm.py:           anchors_link_prob -> num errors / total


RUNNING DISAMBIGUATION ANALYSIS

get_anchors.py :: retrieve anchor texts from wikipedia links

get_anchor_cnts.py :: retrieve counts associated with each anchor text from wikipedia links

wikify.py :: Run disambiguation probabilities for given test set using anchor counts from get_anchor_cnts.py

build_maps.py :: build dictionaries for managing anchor mappings in wikify.py
