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
      
      /*if (parent.maxBridgeDepth > 1000) {
      	breakBridge(bridge[1]);
      	parent.maxBridgeDepth--;
      	int kickOut=0;
      	if (connection[kickOut] == bridge[0])
      		kickOut=1;
      	System.out.println(index + " " + kickOut + " " + connection[kickOut]);
      	parent.neuron[connection[kickOut]].getOut();
      	buildBridge(index, bridgeIndex--);
      }*/
    }
    else if(bridgeIndex == parent.tsp.pointNumber) {
      this.buildBridgeA(callerIndex, bridgeDepth[1]);
      System.out.println("Bridge building Ok");
    }
  }
  public void buildBridgeA(int callerIndex, int depth) {
    if (status == 0) {
      bridge[0] = callerIndex;
      bridgeDepth[0] = depth;
      status++;
    }
    else if (status == 1) {
      for (int i=0; i < 2; i++)
        if (bridge[i] == -1) {
          bridge[i] = callerIndex;
          bridgeDepth[i] = depth;
          status++;
          break;
        }
    }
    else if (status == 2) {
      int bridgeIndex;
      if (chain())
        bridgeIndex = 0;
      else
        bridgeIndex = 1;
      bridge[bridgeIndex] = callerIndex;
      bridgeDepth[bridgeIndex] = depth;
      parent.neuron[bridge[bridgeIndex]].breakBridge(index);
      //this.parent.neuron[this.bridge[bridgeIndex]].buildBridgeB(1,);
    }
  }
  public void buildBridgeB(int desiredStatus, int bridgeIndex) {
    bridge[1] = queryConnections(index, desiredStatus, 0, 1);
    status++;
    if (bridgeDepth[1] > parent.maxBridgeDepth)
      parent.maxBridgeDepth = bridgeDepth[1];
    parent.totalLength += Util.calculateDistance(parent.neuron[bridge[1]], this);
    parent.neuron[bridge[1]].buildBridge(index, bridgeIndex);
  }
  
  public int queryConnections(int callerIndex, int desiredStatus, int depth, int bridge) {
    if(queryStatus == true)
      return -1;
    else
      queryStatus = true;
    int foundIndex;
    if(depth == 0){
      for (int i=0; i < connectionNumber; i++)
        if(parent.neuron[connection[i]].getStatus() == desiredStatus)
          if(parent.neuron[connection[i]].queryStatus  == false) {
            if(bridge != -1) 
              bridgeDepth[bridge] = 0;
            endQuery();
            return connection[i];
          }
      if(callerIndex == index)
        while(true) {
          endQuery();
          depth++;
          foundIndex = queryConnections(index,  desiredStatus, depth, -1);
          if (foundIndex != -1) {
            bridgeDepth[bridge] = depth;
            return foundIndex;
          }
        }
      else
        return -1;
    } else {
      for(int i=0; i < connectionNumber; i++) {
        foundIndex = parent.neuron[connection[i]].
            queryConnections(index, desiredStatus, depth-1, -1);
        if(foundIndex != -1)
          return foundIndex;
      }
      return -1;
    }
  }
  
  public void endQuery() {
    if(queryStatus == true) {
      queryStatus = false;
      for(int i=0; i < connectionNumber; i++)
        parent.neuron[connection[i]].endQuery();
    }
  }
  
  public int queryConnectionsIndex(int callerIndex, int desiredIndex, int depth, int bridge) {
	    if(queryStatus == true)
	      return -1;
	    else
	      queryStatus = true;
	    int foundIndex;
	    if(depth == 0) {
	      //System.out.println("depth0!");
	      for (int i=0; i < connectionNumber; i++)
	        if(parent.neuron[connection[i]].index == desiredIndex)
	          if(parent.neuron[connection[i]].queryStatus  == false) {
	            if(bridge != -1)
	              bridgeDepth[bridge] = 0;
	            endQuery();
	            return connection[i];
	          }
	      if(callerIndex == index) {
	        while(true) {
	          //System.out.println("whiletrue!");
	          endQuery();
	          depth++;
	          foundIndex = queryConnectionsIndex(index, desiredIndex, depth, -1);
	          if (foundIndex != -1) {
	            bridgeDepth[bridge] = depth;
	            return foundIndex;
	          }
	        }
	      } else {
	        return -1;
	      }
	    } else {
	      for(int i=0; i < connectionNumber; i++) {
	        foundIndex = parent.neuron[connection[i]].
	            queryConnectionsIndex(index, desiredIndex, depth-1, -1);
	        if(foundIndex != -1)
	          return foundIndex;
	      } 
	      return -1;
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
	        //System.out.println("Bridge deleted! " + index + " " + callerIndex);
	        return;
	      }
	    }
	    System.out.println("Bridge not found!" + index + " " + bridge[0] + " " + bridge[1] + " " + callerIndex);
  }
  
  public void formBridge(int target) {
	    for (int i=0; i < 2; i++) {
	      if (bridge[i] == -1) {
	        bridge[i] = target;
	        this.status++;
	        //System.out.println("Bridge created! " + index + " " + bridge[i] + " " + bridge[1-i]);
	        return;
	      }
	    }
	    System.out.println("No free slot!" + index + " " + bridge[0] + " " + bridge[1] + " " + target);
  }
  
  
  public int getStatus() {
    return this.status;
  }
  
  void getOut() {
	  if (bridge[0] >0 && bridge[1] > 0) {
		  parent.neuron[bridge[0]].bridgeTo(parent.neuron[bridge[1]]);
	  }
  }
  
  void bridgeTo(Neuron n) {
	  bridge[1] = n.index;
	  n.bridge[0] = this.index;
	  
  }
  
  public void updateOther(int origin, int callerIndex, int target, int newbridge) {
	    
	  	if (index != target) {
	  		for (int i=0; i < 2; i++) {
	  			if (this.bridge[i] != callerIndex) {
	  				parent.neuron[bridge[i]].updateOther(origin, index, target, newbridge);
	  				break;
	  			}
	  		}
	    } else {
	    	for (int i=0; i < 2; i++) {
	    		if (this.bridge[i] != callerIndex) {
		  	    	  parent.neuron[bridge[i]].breakBridge(index);
		  	    	  parent.neuron[bridge[i]].formBridge(origin);
		  	    	  parent.neuron[origin].formBridge(bridge[i]);
		  	    	  breakBridge(bridge[i]);
		  	    	  formBridge(newbridge);
		  	    	  parent.neuron[newbridge].formBridge(index);
		  	    	  break;
		  	    }  
	    	}
	    }
	  }
  
  void optimize(int maxDepth) {
	  
	  for (int b=1; b<2; b++) {
		  if (bridgeDepth[b] > maxDepth) {
			  System.out.println("optimize..");
			  int cindex=0;
			  double cdist=1000000.0;
			  for (int c=1; c<connectionNumber; c++) {
				int cc = connection[c];
				if (cc != bridge[0] && cc != bridge[1]) {
				   if (parent.neuron[cc].bridge[0] != bridge[b] && 
						   parent.neuron[cc].bridge[1] != bridge[b]) {
					   	double dist = Util.calculateDistance(parent.neuron[connection[c]], 
						   	parent.neuron[bridge[b]]);  
				   		if (cdist>dist) {
					   		cindex=c;
					   		cdist=dist;
				    	}
				   }
				}
			  }
			  
			  int temp = bridge[b];
			  System.out.println("info: " + b + " " + bridge[b] + " " + bridgeDepth[b]);
			  parent.neuron[bridge[b]].breakBridge(index);
			  breakBridge(bridge[b]);
			  parent.neuron[bridge[1-b]].updateOther(index, index, cindex, temp);
			  
			  int found = queryConnectionsIndex(index, bridge[b], 0, b);
			  // update inverse link depth
			  System.out.println("New depth: " + bridgeDepth[b] + " " + bridge[b] + " " + found);
			  
		  }
	  }
  }
  
  void fixOrder(int origin, int callerIndex, int count) {
	  
	  if (bridge[1]==callerIndex) {
		  int temp=bridge[0];
		  bridge[0]=bridge[1];
		  bridge[1]=temp;
		  //System.out.println("fix! " + index);
	  }
	  if (origin == index)
		  count++;
	  
	  if (count<2)
		  parent.neuron[bridge[1]].fixOrder(origin, index, count);
	  
  }

}
