package TSPNeuron;
import java.awt.*;

public class MatrixPlotter {
  TSPMatrixSolver parent;
  Frame frame = new Frame();
  MatrixCanvas canvas;

  public MatrixPlotter(TSPMatrixSolver parent) {
    this.parent = parent;
    this.canvas = new MatrixCanvas(parent);
    this.frame.setSize(500,500);
    this.frame.add(this.canvas);
    this.frame.setTitle("Matrix Solver");
    frame.setVisible(true);
  }
  
  void update() {
	  canvas.repaint();
  }
}

