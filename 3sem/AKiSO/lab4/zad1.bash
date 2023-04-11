#!/bin/bash

sctr_size=512

while sleep 1
do 
#set data + calculations
#disc i/o speed
wrt_sctr=$(awk 'FNR == 1 {print $10}' /proc/diskstats)
tm_wrt=$(awk 'FNR == 1 {print $11}' /proc/diskstats)

rd_sctr=$(awk 'FNR == 1 {print $6}' /proc/diskstats)
tm_rd=$(awk 'FNR == 1 {print $7}' /proc/diskstats)

wrt_spd=$(echo "(($wrt_sctr / $tm_wrt) * 512) / 1000" | bc)
rd_spd=$(echo "(($rd_sctr / $tm_rd) * 512) / 1000" | bc)

#battery
btt=$(echo "$(sed -n 12p /sys/class/power_supply/BAT0/uevent)%")
#time
tm_sec=$(echo $(awk '{print $1}' /proc/uptime))
tm_min=$(echo "scale=2; $(awk '{print $1}' /proc/uptime) / 60" | bc)
tm_hrs=$(echo "scale=2; $(awk '{print $1}' /proc/uptime) / 3600" | bc)

#refresh (clear + display)
clear
echo "DISC I/O SPEED (sda - bytes/mseconds)"
echo "disc write speed = ${wrt_spd}"
echo "disc read speed = ${rd_spd}"

echo -e "\nBATTERY:"
echo "${btt}"

echo -e "\nTIME:"
echo "seconds: ${tm_sec}" 
echo "minutes: ${tm_min}"
echo "hours: ${tm_hrs}"

echo -e "\nSYSTEM LOAD:"
cat /proc/loadavg

echo -e "\nMEMORY:"
sed -n -e 1p -e 2p -e 3p /proc/meminfo 

done