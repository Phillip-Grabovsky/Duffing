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

public class ColorMap extends JPanel{
  private int WINDOW_SIZE;
  double[][] colors;

  public ColorMap(int dimension, double[][] colors){
    this.WINDOW_SIZE = 2*dimension;
    this.colors = colors;
	}

	//Method for painting everything
	//Note: Method is called twice during initialization
	public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.WHITE);
    int x = 0;
    int v = -1*WINDOW_SIZE;
    for(int i = 0; i < WINDOW_SIZE; i++){
      for(int j = 0; j < WINDOW_SIZE; j++){
        x = j+1;
        v = WINDOW_SIZE-i;
        System.out.println(x+ ", " +v);

        //set color!
        System.out.println((int)(Math.round(colors[i][j])));
        g.setColor(new Color((int)(Math.round(colors[i][j])), 0, (int)(Math.round(255-colors[i][j]))));

        g.drawOval(x,v,1,1); //draw a pixel!
      }
    }

  }


  public Dimension getPreferredSize() {
    return new Dimension(WINDOW_SIZE, WINDOW_SIZE);
  }
}
