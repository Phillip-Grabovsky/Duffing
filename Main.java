import javax.swing.JFrame;

public class Main {
  //global graphing window variables.
  static int size = 200; //the positive axis distance in pixels.
  static double maxx = 2; //max x to be represented on graph
  static double maxv = 2; //max x to be represented on graph
  static double[][] Data;
  static double[][] periodData;
  static double[][][] allPeriods;

  public static void main(String[] args){
    //user set variables
    double a = 1;
    double b = -1;
    double c = 0.3;
    double w = 1.2;
    double F = 0.5;
    double x = 1;
    double v = 0;

    int numberData = 1000;
    double resolution = 0.001;

    //run simulation
    periodData = new double[numberData][3];
    allPeriods = new double[2+(int)(Math.round(2*Math.PI/(w*resolution)))][numberData+2][3];
    double endTime = numberData * 2*Math.PI/w;
    Oscillator theOscillator = new Oscillator(a,b,c,w,F,x,v);
    double[][] data = theOscillator.evalToTime(endTime,resolution);
    int dataAdded = 0;
    Data = data;
    int placeInPeriod = 0;
    int periodNumber = 0;
    double nextPeriod = 0;
    for(int i = 0; i < data.length; i++){
      allPeriods[placeInPeriod][periodNumber] = data[i];
      if(data[i][2] >= nextPeriod){
        dataAdded++;
        placeInPeriod = 0;
        periodNumber++;
        periodData[dataAdded-1] = data[i];
        System.out.println("(" + data[i][2] + ", " + data[i][0] + ", "  + data[i][1] + ")");
        nextPeriod+=(2*Math.PI)/w;
      }
      placeInPeriod++;
    }

    System.out.println("data added: " + dataAdded);
    animatePeriods();
  }

  public static double[][] returnData(){
    return Data;
  }

  public static void graphData(){
    JFrame frame = new JFrame("Simulation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Grapher(size, maxx, maxv));
    frame.pack();
    frame.setVisible(true);
  }


  public static void animatePeriods(){
    Data = allPeriods[0];
    JFrame frame = new JFrame("Animation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Grapher(size, maxx, maxv));
    frame.pack();
    frame.setVisible(true);

    while(true){
      for(double[][] graph : allPeriods){
        Data = graph;
        try{ Thread.sleep(1); }
        catch (Exception exc){}
        frame.repaint();
      }
    }
  }

}
