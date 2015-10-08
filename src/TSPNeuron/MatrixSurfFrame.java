package TSPNeuron;
import java.awt.*;

public class MatrixSurfFrame {
  TSPMatrixSolver parent;
  Frame frame = new Frame();
  MatrixSCanvas canvas;

  public MatrixSurfFrame(TSPMatrixSolver parent) {
	int size=500;
    this.parent = parent;
    this.canvas = new MatrixSCanvas(parent, size);
    this.frame.setSize(size+35,size+57);
    this.frame.add(this.canvas);
    this.frame.setTitle("Matrix Solver");
    frame.setVisible(true);
  }
  
  void update() {
	  canvas.repaint();
  }
}

