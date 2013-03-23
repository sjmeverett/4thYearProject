#!/bin/bash
for f in "$@"
do
	echo $f
    ./run.sh $f 20
done

