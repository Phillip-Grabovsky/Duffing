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

public class Grapher extends JPanel{
  private int WINDOW_SIZE;
  private double minx;
  private double maxx;
  private double minv;
  private double maxv;

  public Grapher(int dimension, double minx, double minv, double maxx, double maxv){
    this.WINDOW_SIZE = 2*dimension;
    this.minx = minx;
    this.maxx = maxx;
    this.maxv = maxv;
    this.minv = minv;
	}

	//Method for painting everything
	//Note: Method is called twice during initialization
	public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.WHITE);

    //draw the axes
    double[] xleft = new double[] {minx,0};
    double[] xright = new double[] {maxx,0};
    double[] vtop = new double[] {0,maxv};
    double[] vbottom = new double[] {0,minv};
    double[] line1 = new double[] {convertPoint(xleft)[0],convertPoint(xleft)[1],
      convertPoint(xright)[0],convertPoint(xright)[1]};
    double[] line2 = new double[] {convertPoint(vtop)[0],convertPoint(vtop)[1],
      convertPoint(vbottom)[0],convertPoint(vbottom)[1]};

    g.drawLine((int)Math.round(line1[0]), (int)Math.round(line1[1]), (int)Math.round(line1[2]), (int)Math.round(line1[3]));
    g.drawLine((int)Math.round(line2[0]), (int)Math.round(line2[1]), (int)Math.round(line2[2]), (int)Math.round(line2[3]));

    //draw the data
    double[][] data = Main.returnData();
    for(int i = 0; i<data.length; i++){
      double[] vector = new double[] {data[i][0],data[i][1]};
      double x = convertPoint(vector)[0];
      double v = convertPoint(vector)[1];

      g.drawOval((int)(Math.round(x)),(int)(Math.round(v)),2,2);
    }
  }

  private double[] convertPoint(double[] point){
    return new double[]{WINDOW_SIZE*(point[0]-minx)/(maxx-minx),
      WINDOW_SIZE * (maxv-point[1])/(maxv-minv)};
  }

  public Dimension getPreferredSize() {
    return new Dimension(WINDOW_SIZE, WINDOW_SIZE);
  }
}
