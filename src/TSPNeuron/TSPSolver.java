package TSPNeuron;

/*
 * Solves a TSP instance
 */
public class TSPSolver {
  double totalLength = 0;
  int connectionNumber;
  int maxBridgeDepth = 0;
  TSP tsp;
  Neuron[] neuron;
  
  public TSPSolver(TSP tsp) {
	  this.tsp=tsp;
  }
  
  public static void main(String[] args) {
	TSP tsp = new TSP(100,100,"random");
    TSPSolver tspSolver = new TSPSolver(tsp);
    tspSolver.connectionNumber = 5;
    
    System.out.println("Size: " + tspSolver.tsp.pointNumber);
    tspSolver.neuron = tspSolver.createNeurons(tspSolver);
    tspSolver.solve();
    Plotter plotter = new Plotter(tspSolver);
  }
  
  void solve() {
    neuron[0].makeConnections(tsp.pointNumber, connectionNumber);
    neuron[0].buildBridge(-1, -1);
    System.out.println("Max bridge depth: " + maxBridgeDepth);
    System.out.println("Neuron Solver - Length: " + totalLength);
    
    //optimize
    for (int ii=0; ii<0; ii++)
    for (int i=0; i<tsp.pointNumber; i++) {
    	neuron[i].optimize(3);
    	neuron[0].fixOrder(0, neuron[0].bridge[0], 0);
    }
    //this.maxBridgeDepth++;
    //neuron[0].update(0);
  }
  
  Neuron[] createNeurons(TSPSolver tspSolver) {
    Neuron[] neuron = new Neuron[tsp.pointNumber];
    Neuron.parent = tspSolver;
    for(int i=0; i < tsp.pointNumber; i++)
      neuron[i] = new Neuron(tsp.p[i], i, connectionNumber);
    return neuron;
  }
}

