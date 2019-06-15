#include <fstream>
#include <iostream>
#include <sstream>
#include <list>
#include <math.h>
#include <stdlib.h>
#include <string>

using std::list;
using namespace std;

class NonZero {
   
  private: 
     int colPos; 
     double value;

  public:
    NonZero(int c,double v) {
	colPos = c;
	value = v;
    }
    NonZero() {
	colPos = 0;
	value = 0.0;
    }

    void setValues(int c, double v) {
	colPos = c;
	value = v;
    }
    int getColPos() {return colPos;}
    double getValue() {return value;}

};

void readMat();
void printMat();

double epsilon = 0;
double negEpsilon = 0;
bool e = false;

list<list<NonZero> > rows;

int main(int argc, char *argv[])
{
  if (argc > 1 && string(argv[1]) == "-e") {
    epsilon = fabs(strtod(argv[2], 0));
    negEpsilon -= epsilon;
    e = true;
  }

  readMat();
  printMat();

}

void readMat() {
  string line;
  while (getline(cin, line))
    {
      istringstream lstream(line) ;
      double val;
      int count = 1;
      list<NonZero> row;

      // peel off values in this line, one at a time
      while (lstream>> val)
	{
	  // check if val is 0, etc.	  
	  if(val != 0 && !e) {
	    row.push_back(NonZero(count, val));
	  }
	  
	  if (e && (val > epsilon || val < negEpsilon)) {
	    row.push_back(NonZero(count, val));
	  }
	  count++;
	}
      rows.push_back(row);
      row.clear();
    }
}

void printMat() {
	list< list<NonZero> >::iterator i;
	for(i = rows.begin(); i != rows.end(); ++i)
	{
	  list<NonZero> row = *i;
	  list<NonZero>::iterator x;
	  ostringstream sstream;
	
		for(x=row.begin(); x != row.end(); ++x)
		{
		  sstream << x -> getColPos() << " " << x -> getValue() << " "; 
		}
		cout << sstream.str() << endl;
		sstream.clear();
		sstream.seekp(0);
	}
}
