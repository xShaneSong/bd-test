#!/bin/bash

hosts=(hadoop01 hadoop02 hadoop03)

user=`whoami`

case $1 in
"start") {
    for host in ${hosts[@]}
    do
        echo "----------------- $host ---------------"
        echo 1 > ${PF_HOME}/data/zookeeper/zkdata
        ssh $user@$host "${ZOOKEEPER_HOME}/bin/zkServer.sh start"
    done
};;
"stop") {
    for host in ${hosts[@]}
    do
        echo "----------------- $host ---------------"
        ssh $user@$host "${ZOOKEEPER_HOME}/bin/zkServer.sh stop"
    done
};;
"status") {
    for host in ${hosts[@]}
    do
        echo "----------------- $host ---------------"
        ssh $user@$host "${ZOOKEEPER_HOME}/bin/zkServer.sh status"
    done
};;
esac
