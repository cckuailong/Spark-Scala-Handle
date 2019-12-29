# /bin/bash

~/spark/bin/spark-submit --class sparkstreaming_action.wordfreq.main.WordFreq --num-executors 4 --driver-memory 1G --executor-memory 1g  --executor-cores 1 --conf spark.default.parallelism=1000 /root/code/wordFreqFileSpark/target/wordFreqFileSpark-0.1-jar-with-dependencies.jar
