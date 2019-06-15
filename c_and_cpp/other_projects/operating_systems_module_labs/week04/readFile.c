#include <stdio.h>
#include <stdlib.h>

int main()
{
  char c;
  FILE *from;

  from = fopen("file.txt", "r");
  if (from == NULL)
  {
    perror("file.txt");
    exit(1);
  }

  /* file exists, so start reading */
  while ((c = getc(from)) != EOF)
    putc(c, stdout);

  fclose(from);

  exit(0);
}
