package TSPNeuron;
import java.awt.*;

import javax.swing.JPanel;

public class MatrixCanvas extends JPanel{
  TSPMatrixSolver parent;

  public MatrixCanvas(TSPMatrixSolver parent) {
    this.parent = parent;
    this.setLocation(0,0);
    this.setBounds(500,500,500,500);
    this.setVisible(true);
  }
  
  public void paint(Graphics g) {
	  
	super.paint(g);  
	  
    for(int i=0; i < parent.tsp.pointNumber; i++) {
      int j=i+1;
      if (j==parent.tsp.pointNumber)
    	  j=0;
      if (i > 0)
    	if (i==parent.active)
    		g.setColor(Color.BLUE);
    	else
    		g.setColor(new Color(225,0,0));
      
      g.drawRect((int)(4.5*parent.tsp.p[parent.index[i]].x)-1,(int)(4.5*parent.tsp.p[parent.index[i]].y)-1,
                 2,2);
      g.drawLine((int)(4.5*parent.tsp.p[parent.index[i]].x),(int)(4.5*parent.tsp.p[parent.index[i]].y),
                 (int)(4.5*parent.tsp.p[parent.index[j]].x),
                 (int)(4.5*parent.tsp.p[parent.index[j]].y));
    }
  }
 
}

