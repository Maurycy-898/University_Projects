#!/bin/bash

mkdir -p site_changelog
count=1
cd site_changelog
lynx --dump $1 > file1 

while true
do
    sleep $2
    lynx --dump $1 > file2
    difference=$(diff file1 file2) 

    if [ -n "$difference" ]; then
        echo $difference > change$count.txt
        echo $difference
        let count++
        cat file2 > file1
    fi
done