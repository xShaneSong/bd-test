#!/bin/bash

hosts=("hadoop01" "hadoop02" "hadoop03")

for i in ${hosts}
do
    echo "----------------- $i --------------"
    ssh $i "$*"
done