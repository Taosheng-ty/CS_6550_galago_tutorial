#!/bin/bash
./../galago/bin/galago batch-search --showNoResults=true --defaultTextPart=postings.krovetz   --mu=2000 --scorer=dirichlet --verbose=false --requested=20 --index=../index/  --queryFormat=tsv  --queries=query.titles.tsv >galago_dirichlet.txt 2>&1

## Or you can specify parameter in json file.
./../galago/bin/galago batch-search rm_model.json >galago_rm.txt 2>&1