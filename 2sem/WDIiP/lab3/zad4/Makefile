# Makefile
all: main

main: main.o f.o g.o h.o i.o j.o k.o
	clang -o main main.o f.o g.o h.o i.o j.o k.o -lm

main.o: main.c
	clang -c main.c -lm

f.o: f.c
	clang -c f.c 

g.o: g.c
	clang -c g.c

h.o: h.c
	clang -c h.c

i.o: i.c
	clang -c i.c

j.o: j.c
	clang -c j.c

k.o: k.c
	clang -c k.c

clean:
	rm -f main *.o *~