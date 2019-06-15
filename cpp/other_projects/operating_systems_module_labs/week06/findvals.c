#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "utils.h"

int main(int argc, char **argv) {
  char *referenceString, *toleranceString;
  
  for(int i = 1; i < argc; i++) {
    if(strcmp(argv[i],"-r") == 0) { referenceString = argv[i+1]; i++;}
    if(strcmp(argv[i],"-t") == 0) { toleranceString = argv[i+1]; i++;}
  }

  if(argc != 5 || referenceString == NULL || toleranceString == NULL) {
    fprintf(stderr,"Usage: %s -r referenceValue -t toleranceValue\n",argv[0]);
    exit(1);
  }

  struct tm *local;
  time_t start, end;

  time(&start);
  local = localtime(&start);
  printf("# Start time and date: %s", asctime(local));

  float reference = strtof(referenceString, 0);
  float tolerance = strtof(toleranceString, 0);

  if(tolerance < 0.0)
    tolerance *= -1;

  int rct, cct;
  fscanf(stdin, "%d", &rct);
  fscanf(stdin, "%d", &cct);

  float** rows = (float**) malloc(rct * sizeof(float *));
  if(rows == 0) {
    fprintf(stderr, "Sufficent space cannot be allocated.\n");
    exit(1);
  }
  for(int i = 0; i < rct; i++) {
    float* row = (float *) malloc(cct * sizeof(float));
    if( row == 0) {
      fprintf(stderr, "Sufficient space cannot be allocated.\n");
      exit(1);
    }
    rows[i] = row;
  }

  int r,c;
  for( r = 0; r < rct; r++) {
    for( c = 0; c < cct; c++)
      fscanf(stdin, "%f", &rows[r][c]);
  }

  int count = 0;
  for(r = 0; r < rct; r++) {
    for(c = 0; c < cct; c++) {
      if(approxEqual(rows[r][c], reference, tolerance) == 1) {
	fprintf(stdout, "r=%d, c=%d: %.6f\n", r, c, rows[r][c]);
	count++;
      }
    }
  }

  fprintf(stdout, "Found %d approximate matches.\n", count);

  time(&end);
  local = localtime(&end);
  printf("# End time and date: %s", asctime(local));

  exit(0);
}
