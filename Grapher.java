import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.List;
import javax.swing.JPanel;

public class Grapher extends JPanel {
  private int WINDOW_SIZE;
  private double xScalar;
  private double vScalar;

  public Grapher(int dimension, double maxx, double maxv){
    this.WINDOW_SIZE = 2*dimension;
    xScalar = dimension / maxx;
    vScalar = dimension / maxv;
	}

	//Method for painting everything
	//Note: Method is called twice during initialization
	public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.WHITE);

    //draw the axes
    g.drawLine(0, WINDOW_SIZE, 2*WINDOW_SIZE, WINDOW_SIZE);
    g.drawLine(WINDOW_SIZE,0 , WINDOW_SIZE, 2*WINDOW_SIZE);

    //draw the data
    double[][] data = Main.returnData();
    for(int i = 0; i<data.length; i++){
      double x = data[i][0]*xScalar;
      double v = data[i][1]*vScalar;

      g.drawOval(WINDOW_SIZE+(int)(Math.round(x)),WINDOW_SIZE-(int)(Math.round(v)),2,2);
    }
  }

  public Dimension getPreferredSize() {

    return new Dimension(2*WINDOW_SIZE, 2*WINDOW_SIZE);

  }
}
