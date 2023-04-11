#!/bin/bash

rm -f out.py;
make;
./zad2.exe < $1 >> out.py;
make clean;
