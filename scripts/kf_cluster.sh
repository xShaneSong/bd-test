#!/bin/bash

hosts=(hadoop01 hadoop02 hadoop03)

user=`whoami`

case $1 in
"start") {
    for host in ${hosts[@]}
    do
        echo "----------------- $host ---------------"
        ssh $user@$host "${KAFKA_HOME}/bin/kafka-server-start.sh -daemon ${KAFKA_HOME}/config/server.properties"
    done
};;
"stop") {
    for host in ${hosts[@]}
    do
        echo "----------------- $host ---------------"
        ssh $user@$host "${KAFKA_HOME}/bin/kafka-server-stop.sh"
    done
};;
esac

# ${KAFKA_HOME}/bin/kafka-topics.sh --bootstrap-server hadoop01:9092 --create --topic djt --replication-factor 3 --partitions 3
# ${KAFKA_HOME}/bin/kafka-topics.sh --bootstrap-server hadoop01:9092 --list
# ${KAFKA_HOME}/bin/kafka-console-consumer.sh --bootstrap-server hadoop01:9092 --topic djt
# ${KAFKA_HOME}/bin/kafka-console-producer.sh --bootstrap-server hadoop01:9092 --topic djt