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

  public static void main(String[] args) {
    TSPSolver tspSolver = new TSPSolver();
    tspSolver.connectionNumber = 5;
    tspSolver.tsp = new TSP(150,100,"random");
    tspSolver.neuron = tspSolver.createNeurons(tspSolver);
    tspSolver.solve();
  }
  void solve() {
    neuron[0].makeConnections(tsp.pointNumber, connectionNumber);
    neuron[0].buildBridge(-1, -1);
    //this.maxBridgeDepth++;
    //neuron[0].update(0);
    Plotter plotter = new Plotter(this);
    System.out.println(totalLength);
    System.out.println(maxBridgeDepth);
  }
  Neuron[] createNeurons(TSPSolver tspSolver) {
    Neuron[] neuron = new Neuron[tsp.pointNumber];
    Neuron.parent = tspSolver;
    for(int i=0; i < tsp.pointNumber; i++)
      neuron[i] = new Neuron(tsp.p[i], i, connectionNumber);
    return neuron;
  }
}

