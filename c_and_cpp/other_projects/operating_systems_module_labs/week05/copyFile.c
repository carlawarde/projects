#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"
int main(int argc, char* argv[])
{
  char c;
  FILE *from;
  FILE *to;

  from = fopen(argv[2], "r");
  to = fopen(argv[3], "w");
  if (from == NULL)
  {
    perror("file.txt");
    exit(1);
  }
  int flipping = (strcmp(argv[1], "-f")==0);
		  
  /* file exists, so start reading */
  while ((c = getc(from)) != EOF){
    if(flipping)
      putc(flipChar(c), to);
    else
      putc(c, to);
  }

  fclose(from);
  fclose(to);

  exit(0);
}

