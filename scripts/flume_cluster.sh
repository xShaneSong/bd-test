#!/bin/bash

hosts=("hadoop01" "hadoop02" "hadoop03")

case $1 in
"start") {
    for i in ${hosts}
    do
        echo "-------------- [$i] start Kafka -----------------"
        ssh $i "bin/flume-ng agent -n agent1 -c conf -f conf/taildir-file-selector-avro.properties -Dflume.root.logger=INFO,console"
    done
};;