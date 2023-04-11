# Makefile
all: main

main: main.o funkcja.o
	clang -o main main.o funkcja.o

main.o: main.c
	clang -c main.c

funkcja.o: funkcja.c
	clang -c funkcja.c

clean:
	rm -f main *.o *~
