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

typedef list<NonZero> row;
typedef list<row> mat;

int readMat(mat&);
void transMat(mat,mat&,int);
void printMat(mat);

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
  int maxColSize = 0;
  mat startMat;
  mat tMat;

  maxColSize = readMat(startMat);
  transMat(startMat,tMat,maxColSize);
  printMat(tMat);
}

int readMat(mat &rows) {
  string line;
  int maxCol;
  while (getline(cin, line))
    {
      istringstream lstream(line) ;
      double val;
      int colNum;
      list<NonZero> row;

      // peel off values in this line, one at a time
      while (lstream>> colNum >> val)
	{
	  // check if val is 0, etc.	  
	  if(val != 0 && !e) {
	    row.push_back(NonZero(colNum, val));
	  }
	  
	  if (e && (val > epsilon || val < negEpsilon)) {
	    row.push_back(NonZero(colNum, val));
	  }

	   if(colNum  > maxCol)
		maxCol = colNum;
	}
      rows.push_back(row);
      row.clear();
    }
  return maxCol;
}

void transMat(mat rows,mat &trans,int maxRowSize)
{
	int rowCounter = 0;
	mat::iterator rowIt;
	
	for(unsigned int r = 0; r < maxRowSize; r++) {
		int columnPos = 1;
		row tempRow;
		for(rowIt = rows.begin(); rowIt != rows.end(); ++rowIt)
		  {
				
 			row row = *rowIt;
 			
 			row::iterator columnIt;
 			bool found = false;
 			
 			for(columnIt = row.begin(); columnIt != row.end() && !found; ++columnIt) {
				if(columnIt->getColPos() == rowCounter+1) {
					tempRow.push_back(NonZero(columnPos,columnIt->getValue()));
					found = true;
				}
			}
			columnPos++;
		}
		
		trans.push_back(tempRow);
		rowCounter++;	
	}
}

void multMat(const mat  maxtrixOne, const mat matrixTwo, mat& result)
{
  res.clear();
  for (unsigned int c = 0; c < m1.size(); c++)
  {
    sparseRow row;
    res.push_back(row);	
  }
 vector<sparseRow> transp; // new vector for transpose spars matrix
 transpMat(m2, transp);  // transpose matrix
 for (unsigned int c = 0; c < m2.size(); c++){
      sparseRow row = transp[c];
      sparseRow::iterator nonZeroObj;
      for (nonZeroObj=row.begin(); nonZeroObj !=row.end(); ++nonZeroObj){
          cout<<nonZeroObj->colPos<<" "<<nonZeroObj->colVal<<" ";
      }
      cout<<"\n";
  }
   row r;

  for (unsigned int c = 0; c < matrixOne.size(); c++)
  {
    NonZero nz;

    sparseRow mat  = m1[c];
    double val = dotProd(mat,transp);
    nz.setColPos(c);
    nz.setColVal(val);
    row.push_back(nz);
  }

  res.push_back(row);
  row.clear();
}

double dotProd(row rowOne, mat matrixOne)
{
  
  double value =0;
  row::iterator rowMatB;
  NonZeroe nzOne;
  row::iterator rowMatA;
  int countOne=0;
  int count=0;
   for (int i = 0;i< rOne.size();i++)
   {
     nz1.setValues(rowMatA->getcolPos(),rowMatA->getColVal());
    
     for (rowMatB = matrixOne.begin(); rowMatB != matrixOne.end(); rowMatB++)
     {
       if(nz1.getcolPos() == rowMatB->getcolPos()){
         val = val + nz1.getColVal() * rowMatB->getColVal();
       
       }   
       countOne++;  
     }

     count++;
  }
  return val;
}

void printMat(mat rows)
{
	mat::iterator i;
	for(i = rows.begin(); i != rows.end(); ++i) {
		  row row = *i;
		  row::iterator x;
		  ostringstream sstream;
	
		  for(x=row.begin(); x != row.end(); ++x) {
		    	sstream << x -> getColPos() << " " << x -> getValue() << " "; 
		  }
		  cout << sstream.str() << endl;
		  sstream.clear();
	}
}
