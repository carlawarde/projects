
NAME=findvals
LIBS=
SOURCES=\
	findvals.c utils.c


CC=gcc
CFLAGS=-g -Wall -fopenmp

CXX=g++
CXXFLAGS=$(CFLAGS)

LD=gcc
LDFLAGS= -fopenmp


OBJECTS=${SOURCES:.c=.o}


.PHONY: clean
.SUFFIXES: .o .c .cc .cpp


all: program

clean:
	$(RM) $(OBJECTS)
	$(RM) $(NAME)

.c.o:
	$(CC) -c $(CFLAGS) -o $@ $<

.cc.o:
	$(CXX) -c $(CXXFLAGS) -o $@ $<

.cpp.o:
	$(CXX) -c $(CXXFLAGS) -o $@ $<


program: $(NAME)
$(NAME): $(OBJECTS)
	$(LD) $(LDFLAGS) $(LIBS) -o $@ $^

