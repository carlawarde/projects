#
# 
# Makefile  for CS4815 lab 
#
# Copyright (C) 2008 Jacek Rosik <jacek.rosik@ul.ie>
# 


PROGRAMS=\
	bezier\
	viewer


bezier_SOURCES=\
	bezier.cc

viewer_SOURCES=\
	viewer.cc


CC=gcc
CFLAGS=-g -Wall

CXX=g++
CXXFLAGS=$(CFLAGS)

LD=g++
LDFLAGS=
LIBS=-lglut -lGLU -lGL




all: $(PROGRAMS)

PHONY: clean
.SUFFIXES: .o .c .cc .cpp


define PROGRAM_template
  $(1)_OBJECTS = $${$(1)_SOURCES:.cc=.o}
  OBJECTS += $$($(1)_OBJECTS)

  $(1): $$($(1)_OBJECTS)
endef

$(foreach program, $(PROGRAMS), $(eval $(call PROGRAM_template,$(program))))


clean:
	$(RM) $(OBJECTS)
	$(RM) $(PROGRAMS)

.c.o:
	$(CC) -c $(CFLAGS) -o $@ $<
.cc.o:
	$(CXX) -c $(CXXFLAGS) -o $@ $<
.cpp.o:
	$(CXX) -c $(CXXFLAGS) -o $@ $<

$(PROGRAMS):
	$(LD) $(LDFLAGS) $(LIBS) -o $@ $^ 


