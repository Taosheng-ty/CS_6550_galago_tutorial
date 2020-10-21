# CS_6550_galago_tutorial
This is the repo for CS_6550 galago tutorial. In this repo I provide a toy dataset "trecText_toy_.gz" for you to play with in order to be farmiliar with galago. I test the instruction below on ubuntu 18.4. If you are working on win or mac, you might need to change a little bit.
## First get the repo
    git clone https://github.com/Taosheng-ty/CS_6550_galago_tutorial.git
    cd CS_6550_galago_tutorial
You can build index with the scripts we provide,

    chmod +x cmd.sh
    ./cmd.sh
Or you can build steps by steps with below instructions.
## Then we need to download the galago, here I provide link to download the latest binary version. You may use other version or compile form the source file by yourselves. Detailed instruction for installation, please refer https://sourceforge.net/p/lemur/wiki/Galago%20Installation/ 
    wget https://master.dl.sourceforge.net/project/lemur/lemur/galago-3.18/galago-3.18-bin.tar.gz
Here I recommend to use JDK 8. I provide the following code to specify the JDK.  JDK 11 seems not working.

    wget https://download.java.net/openjdk/jdk8u41/ri/openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz
    tar xvf openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz
    SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
    export PATH=$SCRIPTPATH/java-se-8u41-ri/bin:$PATH

Afer finish downloading, we unzip galago and rename it as galago 

    tar -xzf ./galago-3.18-bin.tar.gz
    mv galago-3.18-bin galago
    chmod +x galago/bin/galago

Then, basically we can build index now. You have two choice. You can write a json file and build index. Here I give an example json file.
    
    ./galago/bin/galago build  build.json 
 
Or you can directly build index with command line,

    ./galago/bin/galago build --inputPath=trecText_toy.gz --indexPath=./index/ --filetype=trectext --tokenizer/fields+text
    
You can found more detail about how to build index from [here](https://sourceforge.net/p/lemur/wiki/Galago%20Indexing/)

## How to use galago api with java code?
I recommend you to use some IDE to help build it. For example you can use Eclipse ([download here](https://www.eclipse.org/downloads/packages/release/kepler/sr1/eclipse-ide-java-developers)) to link the library and compile. Below are the steps to use API with java code in eclipse,
1. Open eclipse and create a new java project, click File-->New--->Java Project

2. Then you can write you own java code now. If you are not farmiliar with java, you can paste the folder tutorial_1 we provide in this repos to src/ of your java project. It transform between Internal ID and External ID. It's an exmaple we borrowed from [here](https://github.com/jiepujiang/cs646_tutorials.git).

3. Select the file you want to compile in src/ and add galago library, click Project--->Properties--->Java Build Path---> add External JARs. Then you need to select all jar file in galago/lib/



