#!/bin/bash

# column description
echo -e " PPID\t PID\t VSZ\t \t RSS\t TTY\t STAT\t START\t U_TIME\t OPEN_FILES \tNAME \n"

for pid in /proc/* 
do
    if [[ -d "$pid" ]]; then
        base=$(basename $pid)

        if [ -n "$base" ] && [ "$base" -eq "$base" ] 2>/dev/null; then

            name=$(awk '{print $2}' /proc/${base}/stat)
            if [ -z "$name" ];
                then name='?'
            fi
            
            ppid=$(awk '{print $4}' /proc/${base}/stat)
            if [ -z "$ppid" ]; 
                then ppid='?' 
            fi
            
            pid=$(awk '{print $1}' /proc/${base}/stat)
            if [ -z "$pid" ]; then 
                pid='?'
            fi;
            
            vsz=$(awk '{print $23}' /proc/${base}/stat)
            if [ -z "$vsz" ]; then 
                vsz='?' 
            fi
            
            if [ $vsz -eq 0 ]; then 
                vsz="${vsz}        " 
            fi
            
            rss=$(awk '{print $24}' /proc/${base}/stat)
            if [ -z "$rss" ]; then 
                rss='?' 
            fi
            
            tty=$(awk '{print $7}' /proc/${base}/stat)
            if [ -z "$tty" ]; then 
                tty='?'
            fi
            
            stat=$(awk '{print $3}' /proc/${base}/stat)
            if [ -z "$stat" ]; then 
                stat='?' 
            fi
        
            starttime=$(awk '{print $22}' /proc/${base}/stat)
            if [ -z "$starttime" ]; then 
                starttime='?' 
            fi
            
            utime=$(awk '{print $14}' /proc/${base}/stat)
            if [ -z "$utime" ]; then 
                utime='?' 
            fi
            
            openfiles=$(sudo ls -l /proc/${base}/fd | wc -l)
            if [ -z "$openfiles" ]; then 
                openfiles='?' 
            fi

            echo -e " $ppid\t $pid\t $vsz\t $rss\t $tty\t $stat\t $starttime\t $utime\t $openfiles\t \t$name"
        fi
    fi
done