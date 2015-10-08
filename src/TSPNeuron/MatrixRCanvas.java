package TSPNeuron;
import java.awt.*;

import javax.swing.JPanel;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MatrixRCanvas extends JPanel{
  TSPMatrixSolver parent;
  int size;
  int n;
  double dx;
  
  private List<java.awt.Point> fillCells;
  private List<java.awt.Point> fillDiagonal;

  public MatrixRCanvas(TSPMatrixSolver parent, int size) { 
	  this.size = size;
	  this.parent = parent;
	  n = parent.tsp.pointNumber;
	  dx=(double)size/(double)n;
      fillCells = new ArrayList<>(n);
      fillDiagonal = new ArrayList<>(n);
      this.setBounds(size,size,size,size);
      this.setLocation(0,0);
      for (int i=0; i<n; i++) 
    	  fillDiagonal.add(new java.awt.Point(i, i));
  }
  
  int minIndex(double[] a) {
	  
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
  
  protected void paintComponent(Graphics g) {
	  
      super.paintComponent(g);
      
      for (java.awt.Point fillCell : fillDiagonal) {
          int cellX = (int)(dx + (fillCell.x * dx));
          int cellY = (int)(dx + (fillCell.y * dx));
          g.setColor(Color.BLACK);
          g.fillRect(cellX, cellY, (int)dx, (int)dx);
      }
      
      fillCells.clear();
      for (int i=0; i<n; i++) {
    	  int x=i;
    	  int y=minIndex(parent.matrix[x]);
    	  fillCell(x, y);
      }
           
      for (java.awt.Point fillCell : fillCells) {
          int cellX = (int)(dx + (fillCell.x * dx));
          int cellY = (int)(dx + (fillCell.y * dx));
          g.setColor(Color.RED);
          g.fillRect(cellX, cellY, (int)dx, (int)dx);
      }
      
      int drawsize = (int)(dx*n);
      g.setColor(Color.BLACK);
      //g.drawRect((int)dx, (int)dx, drawsize, drawsize);

      for (int i = (int)dx; i <= drawsize+(int)dx; i += (int)dx) {
          g.drawLine(i, (int)dx, i, drawsize+(int)dx);
      }

      for (int i = (int)dx; i <= drawsize+(int)dx; i += (int)dx) {
          g.drawLine((int)dx, i, drawsize+(int)dx, i);
      }
  }
  
  public void fillCell(int x, int y) {
      fillCells.add(new java.awt.Point(x, y));
      repaint();
  }
 
}

