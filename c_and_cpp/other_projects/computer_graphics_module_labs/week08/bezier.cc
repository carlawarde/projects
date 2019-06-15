#include <GL/glut.h>
#include <stdlib.h>
#include <math.h>
#include <iostream>

#if !defined(GLUT_WHEEL_UP)
#  define GLUT_WHEEL_UP   3
#  define GLUT_WHEEL_DOWN 4
#endif
using namespace std;
/*  Set initial size of the display window.  */
GLsizei winWidth = 600, winHeight = 600;
GLsizei newWidth, newHeight;

/*  Set size of world-coordinate clipping window.  */
GLfloat xwcMin = 0.0, xwcMax = 0.0;
GLfloat ywcMin = 0.0, ywcMax = 0.0;

int clickX, clickY,point = 5;
bool leftButton = false, nearPoint = false;;
float zoom = 1.0;
GLdouble wx, wy, wz;

class wcPt3D {
   public:
      GLfloat x, y, z;
};

GLint nCtrlPts = 6;
wcPt3D ctrlPts [6] = { {0.0, 0.0, 0.0}, { 30.0, 50.0, 0.0},
                 {60.0, 30.0, 0.0}, {80.0, 80.0, 0.0}, {90.0,20.0,0.0}, {110.0,70.0,0.0} };

void init (void)
{
   /*  Set color of display window to white.  */
   for(int i = 0; i < nCtrlPts; i++) {
	if(ctrlPts[i].x > xwcMax) {
		xwcMax = ctrlPts[i].x;
	}
	if(ctrlPts[i].y > ywcMax) {
		ywcMax = ctrlPts[i].y;
	}
	if(ctrlPts[i].x < xwcMin) {
		xwcMin = ctrlPts[i].x;
	}
	if(ctrlPts[i].y < ywcMin) {
		ywcMin = ctrlPts[i].y;
	}
   }
	
   glClearColor (1.0, 1.0, 1.0, 0.0);
}

void zoomFunc(void)
{
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
	gluOrtho2D (xwcMin*zoom, xwcMax*zoom, ywcMin*zoom, ywcMax*zoom);

   glutPostRedisplay();
}

void plotPoint (wcPt3D bezCurvePt)
{
    glBegin (GL_POINTS);
        glVertex2f (bezCurvePt.x, bezCurvePt.y);
    glEnd ( );
}

/*  Compute binomial coefficients C for given value of n.  */
void binomialCoeffs (GLint n, GLint * C)
{
   GLint k, j;

   for (k = 0;  k <= n;  k++) {
      /*  Compute n!/(k!(n - k)!).  */
      C [k] = 1;
      for (j = n;  j >= k + 1;  j--)
        C [k] *= j;
      for (j = n - k;  j >= 2;  j--)
        C [k] /= j;
   }
}

void computeBezPt (GLfloat t, wcPt3D * bezPt, GLint nCtrlPts,
                    wcPt3D * ctrlPts, GLint * C)
{
   GLint k, n = nCtrlPts - 1;
   GLfloat bezBlendFcn;

   bezPt->x = bezPt->y = bezPt->z = 0.0;

   /*  Compute blending functions and blend control points. */
   for (k = 0; k < nCtrlPts; k++) {
      bezBlendFcn = C [k] * pow (t, k) * pow (1 - t, n - k);
      bezPt->x += ctrlPts [k].x * bezBlendFcn;
      bezPt->y += ctrlPts [k].y * bezBlendFcn;
      bezPt->z += ctrlPts [k].z * bezBlendFcn;
   }
}

void bezier (wcPt3D * ctrlPts, GLint nCtrlPts, GLint nBezCurvePts)
{
   wcPt3D bezCurvePt;
   GLfloat t;
   GLint *C;

   /*  Allocate space for binomial coefficients  */
   C = new GLint [nCtrlPts];

   binomialCoeffs (nCtrlPts - 1, C);
   for (int i = 0;  i <= nBezCurvePts;  i++) {
      t = GLfloat (i) / GLfloat (nBezCurvePts);
      computeBezPt (t, &bezCurvePt, nCtrlPts, ctrlPts, C);
      plotPoint (bezCurvePt);
   }
   delete [ ] C;
}

void displayFcn (void)
{
   glClear (GL_COLOR_BUFFER_BIT);   //  Clear display window.

   /*  Set example number of control points and number of
    *  curve positions to be plotted along the Bezier curve.
    */
   GLint nBezCurvePts = 1000;

   glPointSize (4);
   glColor3f (1.0, 0.0, 0.0);      //  Set point color to red.

   bezier (ctrlPts, nCtrlPts, nBezCurvePts);

   glLineWidth(1.2);
   glBegin(GL_LINES);
	glColor3f(0.20,0.45,0.42);
	for(int i = 0; i < (nCtrlPts-1); i++) {
		glVertex2f(ctrlPts[i].x,ctrlPts[i].y);
		glVertex2f(ctrlPts[i+1].x,ctrlPts[i+1].y);
	}
   glEnd();
   glutSwapBuffers();
}

void winReshapeFcn (GLint newWidth, GLint newHeight)
{
   /*  Maintain an aspect ratio of 1.0.  */

  if(newWidth >= newHeight)
    glViewport(0,0, newHeight, newHeight);
  else
    glViewport(0,0, newWidth, newWidth);

   glMatrixMode (GL_PROJECTION);
   glLoadIdentity ( );

   gluOrtho2D (xwcMin*zoom, xwcMax*zoom, ywcMin*zoom, ywcMax*zoom);

   glutPostRedisplay();
}

void GetOGLPos(int x, int y)
{
  GLint viewport[4];
  GLdouble mvmatrix[16], projmatrix[16];
  GLint viewportY;
  
  glGetIntegerv (GL_VIEWPORT, viewport);
  glGetDoublev (GL_MODELVIEW_MATRIX, mvmatrix);
  glGetDoublev (GL_PROJECTION_MATRIX, projmatrix);

  viewportY = viewport[3] - (GLint) y - 1;
  gluUnProject ((GLdouble) x, (GLdouble) viewportY, 0.0,
		mvmatrix, projmatrix, viewport, &wx, &wy, &wz);
}

void MouseCallback(int button, int state, int x, int y)
{
  leftButton = ((button == GLUT_LEFT_BUTTON) && (state == GLUT_DOWN));

  GetOGLPos(x,y);
  clickX= wx;
  clickY= wy;
  
  for(int i = 0; i < nCtrlPts;i++) {
      if(sqrt(pow(((wx)-ctrlPts[i].x),2)+pow(((wy)-ctrlPts[i].y),2))<=(10*zoom)){
	  nearPoint = true;
	  point = i;
	  i = nCtrlPts;
	}	
    }

  int mod = glutGetModifiers();

  if(mod == GLUT_ACTIVE_CTRL && button == 3) {
     zoom = zoom / 1.2;
     zoomFunc();
  }
  else if(mod == GLUT_ACTIVE_CTRL && button == 4) {
    zoom = zoom * 1.2;
    zoomFunc();
  }
 
}

void MotionCallback(int x, int y)
 {

  if (leftButton) {
    
    if (nearPoint) {
	GetOGLPos(x,y);
	ctrlPts[point].x=ctrlPts[point].x+((clickX-wx)/-20);
	ctrlPts[point].y=ctrlPts[point].y+((clickY-wy)/-20);
	glutPostRedisplay();
      }
    else {
	for (int i = 0; i < nCtrlPts; i++) {
	    ctrlPts[i].x=ctrlPts[i].x+(clickX-wx)/20;
	    ctrlPts[i].y=ctrlPts[i].y+(clickY-wy)/-20;
	  }
	glutPostRedisplay();
      }
  }
}


void keyPress(unsigned char key, int x, int y) 
{
   
  if(key == 'z') {
      zoom = zoom / 1.2;
      zoomFunc();
    }
  else if(key == 'Z') {
      zoom = zoom * 1.2;
      zoomFunc();
    }
}

int main (int argc, char** argv)
{
   glutInit (&argc, argv);
   glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB);
   glutInitWindowPosition (50, 50);
   glutInitWindowSize (winWidth, winHeight);
   glutCreateWindow ("Bezier Curve");
   
   init ( );
   glutDisplayFunc (displayFcn);
   glutReshapeFunc (winReshapeFcn);

   glutKeyboardFunc(keyPress);
   glutMouseFunc(MouseCallback);
   glutMotionFunc(MotionCallback);

   glutMainLoop ( );
}
