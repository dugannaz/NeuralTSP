package TSPNeuron;

public class Sorter {

  public static double[] sorted;
  public static int[] index;

  public static void sort(int numberOfItems, double[] source) {
    boolean sort = false;
    int i,temp;
    double tempd;
    int[] sourceOrder = new int[numberOfItems];
    for (i=0; i < numberOfItems; i++)
      sourceOrder[i] = i;
    while (sort == false) {
      sort = true;
      for (i=0; i < numberOfItems - 1; i++)
        if (source[i] > source[i+1]) {
          sort = false;
          tempd = source[i];
          source[i] = source[i+1];
          source[i+1] = tempd;
          temp = sourceOrder[i];
          sourceOrder[i] = sourceOrder[i+1];
          sourceOrder[i+1] = temp;
        }
    }
    sorted = source;
    index = sourceOrder;
  }
  
  public static void sortKeepOriginal(int numberOfItems, double[] source1) {
	  
	  	double[] source = new double[numberOfItems];
	  	for (int i=0; i<numberOfItems; i++)
	  		source[i]=source1[i];
	  
	    boolean sort = false;
	    int i,temp;
	    double tempd;
	    int[] sourceOrder = new int[numberOfItems];
	    for (i=0; i < numberOfItems; i++)
	      sourceOrder[i] = i;
	    while (sort == false) {
	      sort = true;
	      for (i=0; i < numberOfItems - 1; i++)
	        if (source[i] > source[i+1]) {
	          sort = false;
	          tempd = source[i];
	          source[i] = source[i+1];
	          source[i+1] = tempd;
	          temp = sourceOrder[i];
	          sourceOrder[i] = sourceOrder[i+1];
	          sourceOrder[i+1] = temp;
	        }
	    }
	    sorted = source;
	    index = sourceOrder;
	  }
}
