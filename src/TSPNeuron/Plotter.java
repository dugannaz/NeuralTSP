package TSPNeuron;
import java.awt.*;

public class Plotter {
  TSPSolver parent;
  Frame frame = new Frame();
  TSPCanvas canvas;

  public Plotter(TSPSolver parent) {
    this.parent = parent;
    this.canvas = new TSPCanvas(parent);
    this.frame.setSize(500,500);
    this.frame.add(this.canvas);
    frame.setVisible(true);
  }
}
