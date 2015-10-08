package TSPNeuron;

public class Point {
  public double x;
  public double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  void set(double x, double y) {
	  this.x = x;
	  this.y = y;
  }
  
  void set(Point p) {
	  this.x = p.x;
	  this.y = p.y;
  }
  
  public static double calculateDistance(Point a,Point b) {
	    double dist;
	    dist = Math.sqrt(Math.pow(a.x - b.x, 2)
	                     + Math.pow(a.y - b.y, 2));
	    return dist;
	  }
}
