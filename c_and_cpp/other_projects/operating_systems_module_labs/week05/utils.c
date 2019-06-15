#include <stdio.h>
#include <stdlib.h>

char flipChar(char c) {
  int diff = 0;
  if ('a' <= c && c <= 'z') {
    diff = c -'a';
    c = 'a'+(25-diff);
    return c;
      }
  else if ('A' <= c && c <= 'Z') {
    diff = c - 'A';
    c = 'A'+(25-diff);
    return c;
      }
  else if ('0' <= c && c <= '9') {
    diff = c - '0';
    c = '0' + (9 - diff);
    return c;
      }
  
  return c;
}
