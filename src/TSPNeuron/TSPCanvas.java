package TSPNeuron;
import java.awt.*;

public class TSPCanvas extends Canvas{
  TSPSolver parent;

  public TSPCanvas(TSPSolver parent) {
    this.parent = parent;
    this.setLocation(0,0);
    this.setBounds(500,500,500,500);
    this.setVisible(true);
  }
  public void paint(Graphics g) {
    for(int i=0; i < parent.tsp.pointNumber; i++) {
      if (i > 0)
        g.setColor(new Color(225,0,0));
      g.drawRect((int)(4.5*parent.neuron[i].position.x)-1,(int)(4.5*parent.neuron[i].position.y)-1,
                 2,2);
      g.drawLine((int)(4.5*parent.neuron[i].position.x),(int)(4.5*parent.neuron[i].position.y),
                 (int)(4.5*parent.neuron[parent.neuron[i].bridge[1]].position.x),
                 (int)(4.5*parent.neuron[parent.neuron[i].bridge[1]].position.y));
    }
  }
}
