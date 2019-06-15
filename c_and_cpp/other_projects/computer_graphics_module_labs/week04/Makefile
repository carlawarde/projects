#
# 
# Makefile  for CS4815 lab 
#
# Copyright (C) 2008 Jacek Rosik <jacek.rosik@ul.ie>
# 


LIBS=-lglut -lGLU -lGL
SOURCES=\
	spin.cc


CC=gcc
CFLAGS=-g -Wall

CXX=g++
CXXFLAGS=$(CFLAGS)

LD=g++
LDFLAGS=


OBJECTS=${SOURCES:.cc=.o}


.PHONY: clean
.SUFFIXES: .o .c .cpp


all: spin glut7 glut8

clean:
	$(RM) $(OBJECTS) glut7.o glut8.o
	$(RM) spin glut7 glut8

.c.o:
	$(CC) -c $(CFLAGS) -o $@ $<

.cpp.o:
	$(CXX) -c $(CXXFLAGS) -o $@ $<

spin: $(OBJECTS)
	$(LD) $(LDFLAGS) $(LIBS) -o $@ $^

glut7: glut7.o
	$(LD) $(LDFLAGS) $(LIBS) -o $@ $^

glut8: glut8.o
	$(LD) $(LDFLAGS) $(LIBS) -o $@ $^

