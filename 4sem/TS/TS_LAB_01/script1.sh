#!/bin/bash

avg_times=""
TTLs=""

current=""
write_ttl="write"


for i in {1..1000..10}
do
  echo "$i"
  ping -n 3 -l $i $1 | sed 1d | \
  while read line 
  do
    current=$(echo "line: " $line | awk '{print $NF}')
    
    if [[ "$current" == *"TTL"* && $write_ttl == "write" ]]; then
      current=$(echo "$current" | tr -dc '0-9')
      echo "$current," >> ttls.csv
      write_ttl="stop"
    fi 

    if [[ "$current" == *"ms"* && "$current" != *"time"* ]]; then
      current=$(echo "$current" | tr -dc '0-9')
      echo "$current," >> avg_times.csv
    fi

  done
  write_ttl="write"
done