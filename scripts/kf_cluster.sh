#!/bin/bash

hosts=("hadoop01" "hadoop02" "hadoop03")

case $1 in
"start") {
    for i in ${hosts}
    do
        echo "-------------- [$i] start Kafka -----------------"
        ssh $i "kafka-server-start.sh $KAFKA_HOME/config/server.properties"
    done
};;
"stop") {
    for i in ${hosts}
    do
        echo "-------------- [$i] stop Kafka -----------------"
        ssh $i "kafka-server-stop.sh"
    done
};;
"status") {
    for i in ${hosts}
    do
        ssh $i "zkServer.sh status"
    done
};;