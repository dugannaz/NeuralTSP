package TSPNeuron;

public class Neuron {
  static TSPSolver parent;
  int index;
  int status;
  boolean queryStatus;
  int connectionNumber;
  Point position;
  int[] bridge = {-1,-1};
  int[] bridgeDepth = new int[2];
  int[] connection;
  double[] connectionDist;

  public Neuron(Point p, int index, int connectionNumber) {
    this.position = p;
    this.index = index;
    this.status = 0;
    this.queryStatus = false;
    this.connectionNumber = connectionNumber;
    this.connection = new int[connectionNumber];
    this.connectionDist = new double[connectionNumber];
  }

  public void makeConnections(int pointNumber, int connectionNumber) {
    double[] dist = new double[pointNumber];
    int[] distOrder;
    for(int i = 0; i < pointNumber; i++)
      dist[i] = Util.calculateDistance(parent.neuron[i],this);
    Sorter.sort(pointNumber, dist);
    distOrder = Sorter.index;
    for(int i = 0; i < connectionNumber; i++) {
      this.connection[i] = distOrder[i+1];
      this.connectionDist[i] = dist[distOrder[i+1]];
    }
    if (this.index != pointNumber-1)
      parent.neuron[this.index +1].makeConnections(pointNumber, connectionNumber);
    else {
      System.out.println("Connections Ok");
      parent.neuron[0].informConnections();
    }
  }
  public void informConnections() {
    for(int i=0; i < parent.connectionNumber; i++)
      parent.neuron[this.connection[i]].addConnection(this.index);
    if(this.index != parent.tsp.pointNumber-1)
      parent.neuron[this.index +1].informConnections();
     else
       System.out.println("Connection informing Ok");
  }
  public void addConnection(int connectionIndex) {
    int i;
    for(i=0; i < this.connectionNumber; i++)
      if(this.connection[i] == connectionIndex) break;
    if(i == this.connectionNumber) {
      this.connection = Util.resize(this.connection, this.connection.length + 1);
      this.connectionDist = Util.resize(this.connectionDist, this.connectionDist.length + 1);
      this.connection[this.connectionNumber] = connectionIndex;
      this.connectionDist[this.connectionNumber] = Util.calculateDistance(parent.neuron[connectionIndex], this);
      this.connectionNumber++;
    }
  }
  public void buildBridge(int callerIndex, int bridgeIndex) {
    bridgeIndex++;
    if(bridgeIndex > 0 && bridgeIndex < parent.tsp.pointNumber -1) {
      this.buildBridgeA(callerIndex, this.bridgeDepth[1]);
      this.buildBridgeB(0, bridgeIndex);
    }
    else if(bridgeIndex == 0) {
      this.buildBridgeB(0, bridgeIndex);
    }
    else if(bridgeIndex == parent.tsp.pointNumber -1) {
      this.buildBridgeA(callerIndex, this.bridgeDepth[1]);
      this.buildBridgeB(1, bridgeIndex);
    }
    else if(bridgeIndex == parent.tsp.pointNumber) {
      this.buildBridgeA(callerIndex, this.bridgeDepth[1]);
      System.out.println("Bridge building Ok");
    }
  }
  public void buildBridgeA(int callerIndex, int depth) {
    if (this.status == 0) {
      this.bridge[0] = callerIndex;
      this.bridgeDepth[0] = depth;
      this.status++;
    }
    else if (this.status == 1) {
      for (int i=0; i < 2; i++)
        if (this.bridge[i] == -1) {
          this.bridge[i] = callerIndex;
          this.bridgeDepth[i] = depth;
          this.status++;
          break;
        }
    }
    else if (this.status == 2) {
      int bridgeIndex;
      if (this.chain())
        bridgeIndex = 0;
      else
        bridgeIndex =1;
      this.bridge[bridgeIndex] = callerIndex;
      this.bridgeDepth[bridgeIndex] = depth;
      this.parent.neuron[this.bridge[bridgeIndex]].breakBridge(this.index);
      //this.parent.neuron[this.bridge[bridgeIndex]].buildBridgeB(1,);
    }
  }
  public void buildBridgeB(int desiredStatus, int bridgeIndex) {
    this.bridge[1] = this.queryConnections(this.index, desiredStatus, 0, 1);
    this.status++;
    if (this.bridgeDepth[1] > this.parent.maxBridgeDepth)
      this.parent.maxBridgeDepth = this.bridgeDepth[1];
    parent.totalLength += Util.calculateDistance(parent.neuron[this.bridge[1]],this);
    parent.neuron[this.bridge[1]].buildBridge(this.index, bridgeIndex);
  }
  public int queryConnections(int callerIndex, int desiredStatus, int depth, int bridge) {
    if(this.queryStatus == true)
      return -1;
    else
      this.queryStatus = true;
    int foundIndex;
    if(depth == 0){
      for (int i=0; i < this.connectionNumber; i++)
        if(parent.neuron[this.connection[i]].getStatus() == desiredStatus)
          if(parent.neuron[this.connection[i]].queryStatus  == false) {
            if(bridge != -1)
              this.bridgeDepth[bridge] = 0;
            return this.connection[i];
          }
      if(callerIndex == this.index)
        while(true) {
          this.endQuery();
          depth++;
          foundIndex = this.queryConnections(this.index,  desiredStatus, depth, -1);
          if (foundIndex != -1) {
            this.bridgeDepth[bridge] = depth;
            return foundIndex;
          }
        }
      else
        return -1;
    } else {
      for(int i=0; i < this.connectionNumber; i++) {
        foundIndex = parent.neuron[this.connection[i]].
            queryConnections(this.index, desiredStatus, depth-1, -1);
        if(foundIndex != -1)
          return foundIndex;
      }
      return -1;
    }
  }
  public void endQuery() {
    if(this.queryStatus == true) {
      this.queryStatus = false;
      for(int i=0; i < this.connectionNumber; i++)
        parent.neuron[this.connection[i]].endQuery();
    }
  }
  public void update(int callerIndex) {
    boolean start = false;
    if (callerIndex == this.index) {
      this.parent.maxBridgeDepth--;
      if (this.parent.maxBridgeDepth > 0)
        start = true;
    }
    for (int i=0; i < 2; i++)
      if (this.bridgeDepth[i] == this.parent.maxBridgeDepth) {
        this.update(i);
        start = true;
      }
    if (start == true)
      callerIndex = this.index;
    this.parent.neuron[this.bridge[1]].update(callerIndex);
  }
  void updateBridge(int bridgeIndex) {
    this.parent.neuron[this.bridge[bridgeIndex]].breakBridge(this.index);
    int i = 0;
    while (bridge[1-bridgeIndex] == connection[i])
      i++;
    this.bridge[bridgeIndex] = connection[i];
    this.bridgeDepth[bridgeIndex] = 0;
    this.parent.neuron[this.bridge[bridgeIndex]].buildBridgeA(this.index, 0);
  }
  boolean chain() {
    return true;
  }
  public void breakBridge(int callerIndex) {
    for (int i=0; i < 2; i++) {
      if (this.bridge[i] == callerIndex) {
        this.bridge[i] = -1;
        this.status--;
        break;
      }
    }
  }
  public int getStatus() {
    return this.status;
  }
}
