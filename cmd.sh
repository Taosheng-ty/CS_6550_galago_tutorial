#!/bin/bash
wget https://master.dl.sourceforge.net/project/lemur/lemur/galago-3.18/galago-3.18-bin.tar.gz
tar -xzf ./galago-3.18-bin.tar.gz
mv galago-3.18-bin galago
wget https://download.java.net/openjdk/jdk8u41/ri/openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz
tar xvf openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz
SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
export PATH=$SCRIPTPATH/java-se-8u41-ri/bin:$PATH
# ./galago/bin/galago build  build.json 
./galago/bin/galago build --inputPath=trecText_toy.gz --indexPath=./index/ --filetype=trectext --tokenizer/fields+text
