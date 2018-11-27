#
#My Makefile for Operating Systems Project 2017
#

#Compiler
GCC = gcc -std=gnu99

#Files
SOURCES = batch.c interactive.c main.c

TARGET = myshell

$(TARGET) : $(SOURCES)
	$(GCC) -o $@ $^

#Remove all compiled odjects
.PHONY: clean
clean:
	@rm -f $(TARGET)


