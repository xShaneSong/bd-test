#!/bin/bash

hosts=(hadoop02 hadoop03)

pcount=$#
if ((pcount==0));then
    echo 'no args';
    exit;
fi

p1=$1
fname=`basename $p1`
echo fname=$fname

pdir=`cd -P $(dirname $p1); pwd`
echo pdir=$pdir

user=`whoami`

for i in ${hosts[@]}
do
    echo "----------------- $i ---------------"
    rsync -rvl $pdir/$fname $user@$i:$pdir
done