#!/bin/bash
## using command line directly.
bash ../galago/bin/galago batch-search --showNoResults=true --defaultTextPart=postings.krovetz   --mu=2000 --scorer=dirichlet --verbose=false --requested=20 --index=../index/  --queryFormat=tsv  --queries=query.titles.tsv >galago_dirichelet.txt
## using json to specify parameters.
bash ../galago/bin/galago batch-search rm_model.json >galago_rm.txt
