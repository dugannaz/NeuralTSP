package TSPNeuron;
import java.awt.*;

public class MatrixFrame {
  TSPMatrixSolver parent;
  Frame frame = new Frame();
  MatrixRCanvas canvas;

  public MatrixFrame(TSPMatrixSolver parent) {
	int size=500;
    this.parent = parent;
    this.canvas = new MatrixRCanvas(parent, size);
    this.frame.setSize(size+35,size+57);
    this.frame.add(this.canvas);
    this.frame.setTitle("Matrix Solver");
    frame.setVisible(true);
  }
  
  void update() {
	  canvas.repaint();
  }
}

