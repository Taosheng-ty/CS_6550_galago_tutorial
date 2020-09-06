#!/bin/bash
wget https://master.dl.sourceforge.net/project/lemur/lemur/galago-3.18/galago-3.18-bin.tar.gz
tar -xzf ./galago-3.18-bin.tar.gz
mv galago-3.18-bin galago
# ./galago/bin/galago build  build.json 
./galago/bin/galago build --inputPath=trecText_toy.gz --indexPath=./index/ --filetype=trectext --tokenizer/fields+text