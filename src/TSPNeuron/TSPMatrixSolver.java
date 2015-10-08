package TSPNeuron;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Scanner;

/*
 * Solves a TSP instance
 */
public class TSPMatrixSolver {
  double totalLength = 0;
  
  TSP tsp;
  double[][] matrix;
  double[][] rmatrix;
  double[][] smatrix;
  int[] index;
  int[] minIndex;
  MatrixPlotter plotter;
  Neuron[] neuron;
  double maxDist;
  int active;

  public TSPMatrixSolver(TSP tsp) {
	  this.tsp = tsp;
	  index = new int[tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++)
		  index[i]=i;
	  minIndex = new int[tsp.pointNumber];
  }
  
  public static void main(String[] args) {
	  
	TSP tsp = new TSP(100,100,"random");
	System.out.println("Size: " + tsp.pointNumber);
	
	// Neuron Solver
	TSPSolver tspSolver1 = new TSPSolver(tsp);
    tspSolver1.connectionNumber = 5;
    tspSolver1.neuron = tspSolver1.createNeurons(tspSolver1);
    tspSolver1.solve();
    
    Plotter plotter1 = new Plotter(tspSolver1);
	
    // Matrix solver from initial problem
    //TSPMatrixSolver tspSolver = new TSPMatrixSolver(tsp);
    //tspSolver.createMatrix();
    
    // Matrix solver from output of Neuron Solver
    TSPMatrixSolver tspSolver = toMatrix(tspSolver1);
    
    tspSolver.plotter = new MatrixPlotter(tspSolver);
    MatrixSurfFrame Mplotter = new MatrixSurfFrame(tspSolver);
    tspSolver.plotter.frame.setLocation(600, 0);
    tspSolver.solve1();
    tspSolver.solve();
    //tspSolver.solveMinimize();
  }
  
  void solve() {
	  
	  double tspLength = getTotalDistance();
	  System.out.println(tspLength);
	  
	  double prev=tspLength;
	  boolean modif=true;
	  while (modif) {
		  modif=false;
		  for (int i=0; i<tsp.pointNumber; i++) {
			  boolean swap=false;
			  int j=i+1;  
			  if (j>tsp.pointNumber-1) j=j%tsp.pointNumber;
			  swapRows(i,j);
			  swapRowsS(i,j);
			  swapIndex(i,j);
			  plotter.update();
		      //try { Thread.sleep(10); } catch (InterruptedException e) {}
			  tspLength = getTotalDistance();
			  if (prev<tspLength) {
				  swapRows(i,j);
				  swapRowsS(i,j);
				  swapIndex(i,j);
			  	  tspLength = prev;
			  } else {
				  modif=true;
				  System.out.println(tspLength);
				  swap=true;
			  }
			  prev=tspLength;
			  
			  if (!swap) {
			  j=i+2;
			  if (j>tsp.pointNumber-1) j=j%tsp.pointNumber;
			  swapRows(i,j);
			  swapRowsS(i,j);
			  swapIndex(i,j);
			  plotter.update();
		      try { Thread.sleep(10); } catch (InterruptedException e) {}
			  tspLength = getTotalDistance();
			  if (prev<tspLength) {
				  swapRows(i,j);
				  swapRowsS(i,j);
				  swapIndex(i,j);
			  	  tspLength = prev;
			  } else {
				  modif=true;
				  System.out.println(tspLength);
				  swap=true;
			  }
			  prev=tspLength;
			  }
			  
			  if (!swap) {
				  j=i+3;
				  if (j>tsp.pointNumber-1) j=j%tsp.pointNumber;
				  swapRows(i,j);
				  swapRowsS(i,j);
				  swapIndex(i,j);
				  plotter.update();
			      try { Thread.sleep(10); } catch (InterruptedException e) {}
				  tspLength = getTotalDistance();
				  if (prev<tspLength) {
					  swapRows(i,j);
					  swapRowsS(i,j);
					  swapIndex(i,j);
				  	  tspLength = prev;
				  } else {
					  modif=true;
					  System.out.println(tspLength);
					  swap=true;
				  }
				  prev=tspLength;
			  }
		    }  
	  }
	  plotter.update();
  }
	  
  void solve1() {
		  
	  double tspLength = getTotalDistance();
	  System.out.println(tspLength);
	  
	  for (int i=0; i<tsp.pointNumber; i++) {
		  //if (Math.abs(minIndex[i]-i)>5) System.out.println(i + " " + minIndex[i]);
		  if (minIndex[i]-i<-5) {
			  //System.out.println("solve1: " + i + " " + (i-minIndex[i]));
			  int niter=i-minIndex[i];
			  for (int k=0; k < niter; k++) {
				  //System.out.println("swap " + k + " " + (i-minIndex[i]));
				  if (i-k-1 >= 0) {
					  int r1=i-k;
					  int r2=i-k-1;
					  swapRows(r1,r2);
					  swapRowsS(r1,r2);
					  swapIndex(r1,r2);
					  minIndex[r1]=findMinIndex(matrix[r1]);
					  minIndex[r2]=findMinIndex(matrix[r2]);
					  plotter.update();
					  try { Thread.sleep(100); } catch (InterruptedException e) {}
				  } 
 			  }
		  }
			  
	  }

	  plotter.update();
	  tspLength = getTotalDistance();
	  System.out.println("Matrix Solver - Length: " + tspLength);
  }
  
  void move(int start, int end) {
	  
	  int niter=start-end-1;
	  for (int k=0; k < niter; k++) {
		  int r1=start-k;
		  int r2=start-k-1;
		  swapRows(r1,r2);
		  swapIndex(r1,r2);
		  minIndex[r1]=findMinIndex(matrix[r1]);
		  minIndex[r2]=findMinIndex(matrix[r2]);
		  plotter.update();
		  //try { Thread.sleep(100); } catch (InterruptedException err) {}	  
	  }
	  double tspLength = getTotalDistance();
	  System.out.println("Matrix Solver - Length: " + tspLength);
  }
  
  void solveRandom() {
	  
	  Random generator = new Random();
	  
	  double tspLength = getTotalDistance();
	  System.out.println(tspLength);
	  
	  double prev=tspLength;
	  
	  int n=0;
	  while (n<1000) {
		  
		  int i = generator.nextInt(tsp.pointNumber-1);
		  //int i=neuron[node].connection[0];
		  //int j=neuron[node].connection[1];
		  int j=i+1;
		  if (j>tsp.pointNumber-1) j=j%tsp.pointNumber;
		  
	      swapRows(i,j);
	      swapIndex(i,j);
	      plotter.update();
	      try { Thread.sleep(100); } catch (InterruptedException e) {}
		  tspLength = getTotalDistance();
		  //System.out.println(tspLength);
		  if (prev<tspLength) {
			  swapRows(i,j);
			  swapIndex(i,j);
			  plotter.update();
			  tspLength = prev;
			  n=n+1;
		  } else {  
			  //swapIndex(i,j);
			  n=0;
			  //plotter.update();
			  System.out.println(tspLength);
		  }
		  prev=tspLength;	  
	  }
	  System.out.println(tspLength);
  }
  
  void createMatrix() {
	  
	  matrix = new double[tsp.pointNumber][tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++) {
		  matrix[i][i]=0.0;
		  for (int j=i+1; j<tsp.pointNumber; j++) {
			  matrix[i][j]=Point.calculateDistance(tsp.p[i], tsp.p[j]);
			  matrix[j][i]=matrix[i][j];
		  }
	  }
	  //printMatrix();
  }
  
void createMatrix(int[] index) {
	  
	  maxDist=0.0;
	  matrix = new double[tsp.pointNumber][tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++) {
		  matrix[i][i]=0.0;
		  for (int j=i+1; j<tsp.pointNumber; j++) {
			  matrix[i][j]=Point.calculateDistance(tsp.p[index[i]], tsp.p[index[j]]);
			  matrix[j][i]=matrix[i][j];
			  if (matrix[i][j]>maxDist) maxDist=matrix[i][j];
		  }
	  }
	  for (int i=0; i<tsp.pointNumber; i++) {
		  minIndex[i]=findMinIndex(matrix[i]);
	  }
	  
	  //Sorter sort = new Sorter();
	  smatrix = new double[tsp.pointNumber][tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++) {
		  Sorter.sortKeepOriginal(tsp.pointNumber, matrix[i]);
		  for (int j=0; j<tsp.pointNumber; j++)
			  smatrix[i][Sorter.index[j]]=j;
	  }
	  
	  //printMatrix();
  }

  int findMinIndex(double[] a) {
	  
	  int mindex=0;
	  double min=1000000.0;
	  for (int i=1; i<a.length; i++) {
		  if (a[i]<min && a[i]>0.0) {
			  min=a[i];
			  mindex=i;
		  }
	  }
	  return mindex;
  }
  
  void swapRows(int r1, int r2) {
	  
	  double[] temp = new double[tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	temp[i]=matrix[r1][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	matrix[r1][i]=matrix[r2][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  matrix[r2][i]=temp[i];
	  
	  for (int i=0; i<tsp.pointNumber; i++)
		  	temp[i]=matrix[i][r1];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	matrix[i][r1]=matrix[i][r2];
	  for (int i=0; i<tsp.pointNumber; i++)
		  matrix[i][r2]=temp[i];
	  
	  /*int mtemp = minIndex[r1];
	  minIndex[r1]=minIndex[r2];
	  minIndex[r2]=mtemp;
	  
	  if (minIndex[r1] == r1) {
		  System.out.println("Warning!!! " + matrix[r1][r1]);
		  minIndex[r1]=r2;
	  }
	  if (minIndex[r2] == r2) {
		  System.out.println("Warning 2 !!! " + matrix[r2][r2]);
		  minIndex[r2]=r1;
	  }*/
		  
  }
  
  void swapRowsS(int r1, int r2) {
	  
	  double[] temp = new double[tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	temp[i]=smatrix[r1][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	smatrix[r1][i]=smatrix[r2][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	smatrix[r2][i]=temp[i];
	  
	  for (int i=0; i<tsp.pointNumber; i++)
		  	temp[i]=smatrix[i][r1];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	smatrix[i][r1]=smatrix[i][r2];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	smatrix[i][r2]=temp[i];
  }
  
  void swapRows(int r1, int r2, int r3) {
	  
	  double[] temp = new double[tsp.pointNumber];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	temp[i]=matrix[r1][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	matrix[r1][i]=matrix[r2][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  matrix[r2][i]=matrix[r3][i];
	  for (int i=0; i<tsp.pointNumber; i++)
		  matrix[r3][i]=temp[i];
	  
	  for (int i=0; i<tsp.pointNumber; i++)
		  	temp[i]=matrix[i][r1];
	  for (int i=0; i<tsp.pointNumber; i++)
		  	matrix[i][r1]=matrix[i][r2];
	  for (int i=0; i<tsp.pointNumber; i++)
		  matrix[i][r2]=matrix[i][r3];
	  for (int i=0; i<tsp.pointNumber; i++)
		  matrix[i][r3]=temp[i];
  }
  
  void swapIndex(int p1, int p2) {
	  int temp = index[p1];
	  index[p1]=index[p2];
	  index[p2]=temp;
  }
  
  void swapIndex(int p1, int p2, int p3) {
	  int temp = index[p1];
	  index[p1]=index[p2];
	  index[p2]=index[p3];
	  index[p3]=temp;
  }
  
  void printMatrix() {
	  for (int i=0; i<tsp.pointNumber; i++) {
		  String line=""; 
		  for (int j=0; j<tsp.pointNumber; j++)
  				line+=" "+matrix[i][j];
		  System.out.println(line);
	  }
  }
  
  double getTotalDistance() {
	  double sum=0.0;
	  for (int i=0; i<tsp.pointNumber; i++) {
		  int j=i+1;
		  if (j<tsp.pointNumber)
			  sum += matrix[i][i+1];
	  }
	  sum += matrix[0][tsp.pointNumber-1];
	  return sum;
  }
  
  static TSPMatrixSolver toMatrix(TSPSolver solver) {
	  
	  TSPMatrixSolver msolver = new TSPMatrixSolver(solver.tsp);
	  msolver.neuron = solver.neuron;
	  
	  for (int i=1; i<msolver.tsp.pointNumber; i++)
		  msolver.index[i]=solver.neuron[msolver.index[i-1]].bridge[1];
	  
	  msolver.createMatrix(msolver.index);
	  
	  return msolver;
	  
	  
  }
  
  void solveMinimize() {
	  
	  int n = tsp.pointNumber;
	  
	  rmatrix = new double[n][n];
	  
	  int[] bridges = new int[n];
	  
	  for (int i=0; i<n; i++)
		  for (int j=0; j<n; j++)
			  rmatrix[i][j] = matrix[i][j];
	  
	  for (int i=0; i<n; i++)
		  rmatrix[i][i] = -1.0;
	  
	  // substruct row min
	  for (int i=0; i<n; i++) {
		  int minIndex = rowMinIndex(i);
		  double minVal = rmatrix[i][minIndex];
		  for (int j=0; j<n; j++) {
			  if (i != j) rmatrix[i][j] -= minVal;
		  }
	  }
	  
	  // substruct col min
	  for (int i=0; i<n; i++) {
		  int minIndex = colMinIndex(i);
		  double minVal = rmatrix[minIndex][i];
		  for (int j=0; j<n; j++) {
			  if (i != j) rmatrix[j][i] -= minVal;
		  }
	  }
	  
	  
	  java.awt.Point maxp = new java.awt.Point();
	  
	  int[] rowIndex = new int[n];
	  int[] colIndex = new int[n];
	  for (int i=0; i<n; i++) {
		  rowIndex[i]=colIndex[i]=index[i];
	  }
	  
	  
	  int rn=n;
	  
	  while (rn>0) {
	  double max=0.0;
	  maxp.x=0;
	  maxp.y=0;
	  for (int i=0; i<rn; i++) {
		  for (int j=0; j<rn; j++) {
			  if (rmatrix[i][j]==0.0) {
				  double min1 = rmatrix[i][rowMinIndex(i)];
				  double min2 = rmatrix[colMinIndex(j)][j];
				  if (min1+min2 > max) {maxp.x=i; maxp.y=j; max=min1+min2;}
			  }
		  }
	  }
	  
	  bridges[rowIndex[maxp.x]]=colIndex[maxp.y];
	  System.out.println(rowIndex[maxp.x] + " " + colIndex[maxp.y]);
	  
	  int ri=-1; 
	  int rj=-1;
	  boolean updateColIndex=true;
	  for (int i=0; i<rn; i++) {
		  rj=-1;
		  if (i != maxp.x) {
			  ri++;
			  rowIndex[ri]=rowIndex[i];
			  for (int j=0; j<rn; j++) {
				if (j != maxp.y) {
					rj++;
					if (updateColIndex) colIndex[rj]=colIndex[j];
					if (ri==maxp.y && rj==maxp.x)
						rmatrix[ri][rj]=-1.0;
					else {
						rmatrix[ri][rj]=rmatrix[i][j];
					}
				}
			  }
			  updateColIndex=false;
		  }
	  }
	  rn--;

	  }
	  int start=0;
	  int origin=start;
	  int target=-1;
	  int count=0;
	  while (target != start) {
		  count++;
		  target = bridges[origin];
		  origin=target;
	  }
	  System.out.println("Count: " + count);
	  
  }
  
  int rowMinIndex(int r) {
	  double[] a = rmatrix[r];
	  int mindex=0;
	  double min=a[0];
	  for (int i=1; i<a.length; i++) {
		  if (a[i]<min && a[i]>0.0) {
			  min=a[i];
			  mindex=i;
		  }
	  }
	  return mindex;
  }
  
  int colMinIndex(int r) {
	  
	  int mindex=0;
	  double min=rmatrix[0][r];
	  for (int i=1; i<rmatrix[0].length; i++) {
		  if (rmatrix[i][r]<min && rmatrix[i][r]>0.0) {
			  min=rmatrix[i][r];
			  mindex=i;
		  }
	  }
	  return mindex;
  }
  
  public void mouseClicked(MouseEvent e) {
	    System.out.println(e.getX() + " " + e.getY());
	  }
}


