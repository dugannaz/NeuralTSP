package TSPNeuron;

public class Util {

  public static int[] resize(int[] array, int newSize) {
    int resized[] = new int[newSize];
    System.arraycopy(array, 0, resized, 0, array.length);
    return resized;
  }
  
  public static double[] resize(double[] array, int newSize) {
    double resized[] = new double[newSize];
    System.arraycopy(array, 0, resized, 0, array.length);
    return resized;
  }
  
  public static double calculateDistance(Neuron a,Neuron b) {
    double dist;
    dist = Math.sqrt(Math.pow(a.position.x - b.position.x, 2)
                     + Math.pow(a.position.y - b.position.y, 2));
    return dist;
  }
}
