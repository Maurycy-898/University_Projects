CXX = g++

OBJECTS = main.o GF.o

main: $(OBJECTS)
	$(CXX) -o $@ $^

$(OBJECTS): GF.hpp

clean:
	rm *.o
