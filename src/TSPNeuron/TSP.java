package TSPNeuron;
import java.util.Random;

/*
 * TSP definition
 */
public class TSP {
  int pointNumber;
  int size;
  Point[] p;

  public TSP(int pointNumber, int size, String source) {
    this.pointNumber = pointNumber;
    this.size = size;
    if(source == "random")
      this.p = initializeRandom();
    else if (source == "grid")
       p = initializeGrid(4.0);
    else if (source == "zero")
    	p = initializeZero();
  }

  Point[] initializeRandom() {
    Point[] p = new Point[this.pointNumber];
    Random rand = new Random();
    for (int i = 0; i < this.pointNumber; i++) {
      double x = rand.nextDouble() * this.size;
      double y = rand.nextDouble() * this.size;
      p[i] = new Point(x,y);
    }
    return p;
  }
  
  Point[] initializeGrid(double d) {
	  
	  int n=(int)(size/d)-1;
	  pointNumber = n*n;
	  p = new Point[pointNumber];
	  for (int i=0; i<n; i++) {
		  double y = i*d+d/2.0;
		  for (int j=0; j<n; j++) {
			  double x = j*d+d/2.0;
			  p[i*n+j] = new Point(x,y);
		  }
	  }
	  //switchNodes(0,22);
	  //pop(200);
	  
	  return p;
  }
  
  Point[] initializeZero() {
	  
	  p = new Point[pointNumber];
	  for (int i=0; i<pointNumber; i++)
		  p[i] = new Point(0.0,0.0);
	  
	  return p;
  }
  
  void switchNodes(int p1, int p2) {
	  
	  Point temp = new Point(p[p1].x, p[p1].y);
	  p[p1].set(p[p2]);
	  p[p2].set(temp);
  }
  
  void pop(int index) {
	  
	  for (int i=index; i<pointNumber-1; i++) {
		  p[i].set(p[i+1]);
	  }
	  pointNumber--;
  }
}
