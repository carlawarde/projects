#include <fstream>
#include <iostream>

using namespace std;

#include <math.h>
#include <stdlib.h>

int countNon0;

void readArr(int, int, double **);

int main(int argc, char *argv[])
{
  int n = atoi(argv[1]);
  int c = atoi(argv[2]);

  double **A = new double*[n];	// each el. of this points to a row of A
  for (int i = 0; i < n; i++)
    A[i] = new double[c];

  readArr(n, c, A);
  
  cout << n << endl;
  for(int i = 0; i < n; i++)
  {
    int not0PerRow = 0;
    for(int j = 0; j < c; j++)
     {
      if(A[i][j] != 0)
	{
	countNon0++;
	not0PerRow++;
	}
     }
    cout << not0PerRow << " ";
      for(int j = 0; j < c; j++)
	{
	if(A[i][j] != 0)
	  cout << j + 1 << " " <<  A[i][j] << " ";
	}
    cout << endl;
  }
  cout << countNon0 << endl;
}

void readArr(int r, int c, double **arr)
{
  // code for reading in r rows of c elements each goes here
  for(int i = 0; i < r; i++)
      for(int j = 0; j < c; j++)
	cin >> arr[i][j];
}
