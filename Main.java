import javax.swing.JFrame;
import java.io.*;

public class Main {
  static double[][] Data;

  //user set variables
  static double a = 1;
  static double b = 0.2;
  static double c = 0.15;
  static double w = 1;
  static double F = 0.855*0.3;
  static double x = 0;
  static double v = 0;

  static String[] var = new String[] {"a", "b", "c", "w", "F", "x", "v"};

  //for method parameters
  static double[] variables = new double[]{a,b,c,w,F,x,v};

  //global graphing window variables.
  static int size = 200; //the positive axis distance in pixels.
  static double maxx = 1; //max x to be represented on graph
  static double maxv = 1; //max x to be represented on graph
  static int waitTime = 10; //the waitTime between frames for an animation
  static int skip = 1; //if your simulation is very fine, you may want to skip frames to make it faster

  //data resolution and time parameters
  static int numberData = 1000;
  static double res = 0.001;

  //path for data output.
  static String path = "/home/phillip/Documents/Projects/Duffing/Data/";


  public static void main(String[] args){

    //if you want to increment (use outputFirstFrames), use these values
    /*double increment = 0.001;
    double start = 0.3;
    double end = 0.1;*/

    //when using the graphData method (just plots a set of points), set your data equal to global Data var.
    /*int numberTimes = 100;
    int totalPeriods = numberData * numberTimes;

    double[][] data = new double[totalPeriods][3];
    for(int i = 0; i <numberTimes; i++){
      double[][] smallData = returnFirstFrame();
      for(int j = 0; j < numberData; j++){
        data[i*numberData+j] = smallData[j];
      }
      variables[5] = smallData[smallData.length-1][0];
      variables[6] = smallData[smallData.length-1][1];
    }*/

    int index = 0;
    double increment = 0.001;
    double start = 0.70;
    double end = 1.40;
    double[][][] myData = returnFirstFrames(index,start,end,increment);
    for(int i = 0; i<(end - start)/increment; i++){
      double[][] data = myData[i];
      String xPath = path + var[index] + "="+ (start+increment*i) + "xFile.txt";
      String vPath = path + var[index] + "="+ (start+increment*i) + "vFile.txt";
      outputData(data,xPath,vPath);
      Data = data;
      //String name = var[index] +  " = " +  (start+i*increment);
      //graphData(name);
    }
    animatePeriods(myData);

    //data output
    /*double[][] myData = returnFirstFrame();
    String xPath = path+"Xfile.txt";
    String vPath = path+"Vfile.txt";
    outputData(myData, xPath, vPath);*/
  }





  public static double[][] returnFirstFrame() {
    //return the first poincare section.

    double[][] firstFrame = new double[numberData][3]; //final output
    double nextPeriod = 0; //period phase detection
    double onePeriod = 2*Math.PI/w; //^

    double[][] data = returnAllPoints(); //get the data
    int dataAdded = 0;

    //next step is to pick out only the first point of each driver period.
    for(int i = 0; i<data.length; i++){
      if(data[i][2] >= nextPeriod){ //only add points when driver has correct phase
        firstFrame[dataAdded] = data[i];
        dataAdded++;
        nextPeriod+=onePeriod; //phase detection
      }
    }

    return firstFrame;
  }





  public static double[][][] returnAllFrames(){
    //separate the data file into groups based on the phase.
    int placeInPeriod = 0; //indexing final array
    int periodNumber = 0; //indexing final array
    double onePeriod = 2*Math.PI/w; //phase detection
    double nextPeriod = 0;
    double[][] data = returnAllPoints(); //get the data!
    double[][][] allPeriods = new double[2+(int)(Math.round(2*Math.PI/(w*res)))][numberData+2][3];
    //above line provides for any weird discrepancies in counting phase, leaves extra empty space in array.

    for(int i = 0; i < data.length; i++){
      allPeriods[placeInPeriod][periodNumber] = data[i];
      placeInPeriod++;

      //when the end of the period has been reached:
      if(data[i][2] >= nextPeriod){
        //reset or increment the followig values
        placeInPeriod = 0;
        periodNumber++;
        nextPeriod+=onePeriod;
      }
    }

    return allPeriods;
  }




  public static double[][] returnAllPoints(){
    double endTime = numberData * 2*Math.PI/w;
    Oscillator theOscillator = new Oscillator(variables);
    double[][] data = theOscillator.evalToTime(endTime,res);
    return data;
  }




  public static double[][][] returnFirstFrames(int index, double start, double end, double increment){
    //don't be stupid:
    variables[index] = start;

    //iterate one variable by a certain increment and look at first poincare section!
    int numberIterations = (int)Math.round(Math.abs(end - start) / increment);
    //make sure that we are incrementing in the correct direction

    if((start - end) > 0){
      increment = -1*increment;
    }

    double[][][] firstFrames = new double[numberIterations][numberData][3];
    for(int i = 0; i<numberIterations; i++){ //iterate the correct variable and add results
      System.out.println(variables[index]);
      variables[index] += increment;
      firstFrames[i] = returnFirstFrame();
    }
    return firstFrames;
  }




  public static void outputData(double[][] data, String pathX, String pathV){
    try {
      File fileX = new File(pathX);
      File fileV = new File(pathV);
      BufferedWriter outputX = new BufferedWriter(new FileWriter(fileX));
      BufferedWriter outputV = new BufferedWriter(new FileWriter(fileV));

      for(double[] point : data){
        outputX.write(point[0] + "\n");
        outputV.write(point[1] + "\n");
      }

      outputX.close();
      outputV.close();
    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }



  public static double[][] returnData(){
    return Data;
  }





  public static void graphData(String name){
    JFrame frame = new JFrame(name);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Grapher(size, maxx, maxv));
    frame.pack();
    frame.setVisible(true);
  }






  public static void animatePeriods(double[][][] allPeriods){
    Data = allPeriods[0];
    JFrame frame = new JFrame("Animation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Grapher(size, maxx, maxv));
    frame.pack();
    frame.setVisible(true);

    while(true){
      int skipvalue = 0;
      for(double[][] graph : allPeriods){
        skipvalue++;
        if(skipvalue == skip){
          skipvalue = 0;
          Data = graph;
          try{ Thread.sleep(waitTime); }
          catch (Exception exc){}
          frame.repaint();
        }
      }
    }
  }



}
