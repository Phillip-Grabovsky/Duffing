import javax.swing.JFrame;
import java.io.*;

public class Main {
  static double[][] Data;

  //user set variables
  static double a = 1;
  static double b = 1;
  static double c = 0.15;
  static double w = 1;
  static double F = 0.855*0.3;
  static double x = 0;
  static double v = 0;

  //for method parameters
  static double[] variables = new double[]{a,b,c,w,F,x,v};

  //global graphing window variables.
  static int size = 200; //the positive axis distance in pixels.
  static double maxx = 1; //max x to be represented on graph
  static double maxv = 1; //max x to be represented on graph
  static int waitTime = 100; //the waitTime between frames for an animation
  static int skip = 1; //if your simulation is very fine, you may want to skip frames to make it faster

  //data resolution and time parameters
  static int numberData = 1000;
  static double res = 0.001;

  //if you want to use a segmented solver (high res for long time, dumps unused data to save memory)
  //set numberSegments to 1 for no segmenting!
  //At the current state, using increased segmenting turns simulation into an O(t^2) algorithm, where t is the final time.
  static int numberSegments = 10;

  //path for data output.
  static String path = "/home/phillip/Documents/Projects/Duffing/Data2/";
  static String[] var = new String[] {"a", "b", "c", "w", "F", "x", "v"};

  public static void main(String[] args){

    double[][] allSegmentedPoints = returnFirstFrame();
    Data = allSegmentedPoints;
    graphData("ishaan");

    /*
    int index = 3;
    double increment = 0.05;
    double start = 0.05;
    double end = 3;
    double[][][] myData = returnFirstFrames(index,start,end+increment,increment);

    animatePeriods(myData);


    //create filenames and store data.
    for(int i = 0; i<(end - start)/increment; i++){
      double[][] data = myData[i];
      String xPath = path + var[index] + "="+ (start+increment*i) + "xFile.txt";
      String vPath = path + var[index] + "="+ (start+increment*i) + "vFile.txt";
      outputData(data,xPath,vPath);
      Data = data;
      //String name = var[index] +  " = " +  (start+i*increment);
      //graphData(name);
      try{ Thread.sleep(2000); }
      catch (Exception exc){}
    }*/

  }





  public static double[][] returnFirstFrame() {
    //return the first poincare section.
    Oscillator theOscillator = new Oscillator(variables);

    double[][] firstFrame = new double[numberData*numberSegments][3]; //final output
    double nextPeriod = 0; //period phase detection
    double onePeriod = 2*Math.PI/w;
    int dataAdded = 0;

    for(int i = 0; i < numberSegments; i++){ //iterate over all the segments necessary
      double endTime = (i+1) * numberData*(2*Math.PI/w);
      double[][] smallData = theOscillator.evalToTime(endTime,res); //evaluate it to the edge of next segment.

      for(int j = 0; j<smallData.length; j++){ //incorportate that data into the full data array
        if(smallData[j][2] >= nextPeriod){ //only add points when driver has correct phase
          firstFrame[dataAdded] = smallData[j];
          dataAdded++;
          nextPeriod+=onePeriod; //phase detection
        }
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
    double endTime = (numberData * 2*Math.PI/w);
    Oscillator theOscillator = new Oscillator(variables);
    double[][] data = theOscillator.evalToTime(endTime,res); //now run to end time, recording data.
    return data;
  }


  /*public static void iterateVariables(int[] indecies, double[][] info){
    //info: 2d array which looks like this:
    // [[increment, start, end], [increment, start, end], [increment, start, end], ... ]

    //iterates three variables, testing the entire space of bounds.
    for(int i = 0; i<indecies.length; i++){
      variables[indecies[i]] = starts[i];
    }

    for(double var1 = )

  }
  */



  public static double[][][] returnFirstFrames(int index, double start, double end, double increment){
    //don't be stupid:
    variables[index] = start;

    //iterate one variable by a certain increment and look at first poincare section!
    int numberIterations = (int)Math.round(Math.abs(end - start) / increment);
    System.out.println("numberIterations: " + numberIterations);
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






  public static double[][] getSegmentedPoints(){
    //make the full data array
    int totalSteps = (int)(Math.round(numberData*numberSegments*2*Math.PI/(w*res))) + 1;
    double[][] data = new double[totalSteps][3];
    System.out.println("full data array: " + data.length);

    //create an oscillator
    Oscillator theOscillator = new Oscillator(variables);

    int currentDataIndex = 0;
    for(int i = 0; i < numberSegments; i++){ //iterate over all the segments necessary
      double endTime = (i+1) * numberData*(2*Math.PI/w);
      double[][] smallData = theOscillator.evalToTime(endTime,res); //evaluate it to the edge of next segment.
      System.out.println("small data array: " + smallData.length);

      for(int j = 0; j<smallData.length; j++){ //incorportate that data into the full data array
        data[currentDataIndex] = smallData[j];
        currentDataIndex++;
      }
    }
    return data;
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
