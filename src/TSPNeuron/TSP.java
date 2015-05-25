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
}
