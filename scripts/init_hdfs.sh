#!/bin/bash

ssh hadoop01 "rm -rf /home/hadoop/workspace/app/bd-pf/data/hdfs/*"
ssh hadoop02 "rm -rf /home/hadoop/workspace/app/bd-pf/data/hdfs/*"
ssh hadoop03 "rm -rf /home/hadoop/workspace/app/bd-pf/data/hdfs/*"
sleep 0.5

ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon start journalnode"
ssh hadoop02 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon start journalnode"
ssh hadoop03 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon start journalnode"
sleep 0.5


ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs namenode -format"
sleep 0.5


ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon start namenode"
ssh hadoop02 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs namenode -bootstrapStandby"
#ssh hadoop02 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon start namenode"
ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon stop namenode"
sleep 0.5

ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs zkfc -formatZK"
sleep 0.5


ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon stop journalnode"
ssh hadoop02 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon stop journalnode"
ssh hadoop03 "/home/hadoop/workspace/app/bd-pf/hadoop/bin/hdfs --daemon stop journalnode"
sleep 0.5

ssh hadoop01 "/home/hadoop/workspace/app/bd-pf/hadoop/sbin/start-dfs.sh"