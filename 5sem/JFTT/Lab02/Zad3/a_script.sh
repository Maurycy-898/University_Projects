#!/bin/bash

rm -f out.c;
make;
./zad3.exe < $1 >> out.c;
make clean;
