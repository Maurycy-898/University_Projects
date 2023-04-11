# Makefile
all: main

main: main.o f.o
	clang -o main main.o f.o

main.o: main.c
	clang -c main.c

f.o: f.c
	clang -c f.c

clean:
	rm -f main *.o *~