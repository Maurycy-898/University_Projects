# Makefile
all: main

main: main.o f.o g.o
	clang -o main main.o f.o g.o -lm

main.o: main.c
	clang -c main.c -lm

f.o: f.c
	clang -c f.c -lm

g.o: g.c
	clang -c g.c -lm

clean:
	rm -f main *.o *~