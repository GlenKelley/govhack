#!/bin/sh

i=$1
shift
for f in $*
do
  dir=`dirname $f`
  fname=`printf "%08d.jpg" $i`
  result="$dir/$fname"
  if [ -f $result ]
  then
    echo $result 'exists!'
    exit 1
  fi
  echo $f "->" $result
  mv $f $result
  i=`expr $i + 1`
done
