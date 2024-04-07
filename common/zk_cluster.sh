#!/bin/bash

hosts=("hadoop01" "hadoop02" "hadoop03")

case $1 in
"start") {
    for i in ${hosts}
    do
        ssh $i "zkServer.sh start"
    done
};;
"stop") {
    for i in ${hosts}
    do
        ssh $i "zkServer.sh stop"
    done
};;
"status") {
    for i in ${hosts}
    do
        ssh $i "zkServer.sh status"
    done
};;