#!/bin/bash

rm -f out.txt;
make;
./zad1.exe < $1 >> out.txt;
make clean;
