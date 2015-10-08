package TSPNeuron;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MatrixMouse extends MouseAdapter {
  MatrixSCanvas m;
  int scale;
  int select;

  public MatrixMouse(MatrixSCanvas m) {
    this.m = m;
    scale = m.size/m.n;
    select = -1;
  }

  public void mouseClicked(MouseEvent e) {
	int x = (e.getX()/scale)-1;
	int y = (e.getY()/scale)-1;
	if (y != select) {
		m.parent.active=y;
		if (select != -1) {
			m.parent.move(select, y);
			select=-1;
		} else
			select=y;
	} else {
		m.parent.active=-1;
		select=-1;
	}
	m.parent.plotter.update();
  }
}
