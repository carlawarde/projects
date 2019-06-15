#include <fstream>
#include <iostream>
#include <sstream>

#include <math.h>
#include <stdlib.h>
#include <string>

using namespace std;

double epsilon = 0;
double negEpsilon = 0;
bool e = false;

int main(int argc, char *argv[])
{
  if (argc > 1 && string(argv[1]) == "-e") {
    epsilon = fabs(strtod(argv[2], 0));
    negEpsilon -= epsilon;
    e = true;
  }
  

  string line;
  while (getline(cin, line))
    {
      istringstream lstream(line) ;
      double val;
      int count = 0;
      // peel off values in this line, one at a time
      while (lstream>> val)
	{
	  // check if val is 0, etc.	  
	  if(val != 0 && !e)
	    cout << count + 1 << " " << val << " ";
	  
	  if (e && (val > epsilon || val < negEpsilon))
	     cout << count + 1 << " " << val << " ";
	  
	  count++;
	
	}
      cout << endl;
    }
}
