import javax.swing.JFrame;
import java.io.*;

public class Main {
  //

  static double[][] Data;

  //user set variables
  static double a = 1;
  static double b = 1;
  static double c = 0.15;
  static double w = 1;
  static double F = 2.65;
  static double x = 0;
  static double v = 0;

  //for method parameters
  static double[] variables = new double[]{a,b,c,w,F,x,v};

  //global graphing window variables.
  static int size = 100; //the positive axis distance in pixels.
  static double minx = -6;
  static double minv = -6;
  static double maxx = 6; //max x to be represented on graph
  static double maxv = 6; //max x to be represented on graph
  static int waitTime = 20; //the waitTime between frames for an animation
  static int skip = 1; //if your simulation is very fine, you may want to skip frames to make it faster

  //data resolution and time parameters
  static int numberData = 100;
  static double res = 0.001;

  //if you want to use a segmented solver (high res for long time, dumps unused data to save memory)
  //segmenting is only useful when you don't use all the data (i.e. only for uses of returnFirstFrame(s)).
  //set numberSegments to 1 for no segmenting!
  static int numberSegments = 1;

  //path for data output.
  static String path = "/home/phillip/Documents/Projects/Duffing/Data/";
  static String[] var = new String[] {"a", "b", "c", "w", "F", "x", "v"};

  public static void main(String[] args){

    //HEAT MAP!
    //generateXVHeatMap(0.3);



    //SHOW AN ANIMATION OF THE POINCARE SECTION EVOLVING AROUND ONE DRIVER PERIOD.
    /*
    double[][][] data = returnAllFrames();
    animatePeriods(data);
    */


    //TRACK A POINT OVER ALL TIME, RETURN DATA FOR ITS MOVEMENT
    /*
    double[][] data = returnAllPoints();
    Data = data;
    graphData("hi");
    ///outputData(data, path+"x", path+"v");
    */



    //SIMPLE ITERATION OF ONE VARIABLE. NO DATA OUTPUT, JUST A FUN ANIMATION OF THE
    //... 1ST POINCARE SECTION!
    /*
    double[][][] data = returnFirstFrames(5,0,4,0.0025);
    animatePeriods(data);
    */



    //ITERATE A SINGLE VARIABLE, ANIMATE CHANGES IN THE POINCARE SECTION. RETURN DATA FILES
    //... FOR EACH ICREMENT OF THE VARIABLE. SHOW GRAPHS AS SEPARATE WINDOWS IN SEQUENCE,
    //... WINDOW TITLES/FILENAMES ALL INDEXED BY VARIABLE VALUES!
    /*
    int index = 5;
    double increment = 0.05;
    double start = 0;
    double end = 3;
    double[][][] myData = returnFirstFrames(index,start,end+increment,increment);
    //SanimatePeriods(myData);
    //create filenames and store data.
    for(int i = 0; i<(end - start)/increment; i++){
      double[][] data = myData[i];
      String xPath = path + var[index] + "="+ (start+increment*i) + "xFile.txt";
      String vPath = path + var[index] + "="+ (start+increment*i) + "vFile.txt";
      outputData(data,xPath,vPath);
      Data = data;
      String name = var[index] +  " = " +  (start+i*increment);
      graphData(name);
      try{ Thread.sleep(2000); }
      catch (Exception exc){}
    }
    */




    //RETURN THE FIRST FRAME OF A POINCARE SECTION. SEGMENTING ALLOWS FOR SUPERHIGH
    //...RESOLUTION AND SUPERHIGH POINT NUMBERS!!
    /*
    double[][] allSegmentedPoints = returnFirstFrame();
    Data = allSegmentedPoints;
    graphData("ishaan");
    */



    //ITERATE UP TO 3 VARIABLES AT A TIME. THIS FUNCTION TESTS THE ENTIRE 3D SPACE OF
    //...VARIABLES, INCREMENTED BY THE USERS CHOICE OF INCREMENT.
    //...NO GRAPHING, BUT AUTOMATICALLY CREATES DATA FILES NAMED ACCORDING TO VARIABLE VALUES.
    /*
    double[][] info = new double[][] {{1,2,0.2},{1,2,0.2},{0.15,2.15,0.2}};
    int[] indecies = new int[] {0,1,2};
    iterateVariables(indecies,info);
    */
  }





  public static double[][] returnFirstFrame() {
    //return the first poincare section.
    Oscillator theOscillator = new Oscillator(variables);

    double[][] firstFrame = new double[numberData*numberSegments][3]; //final output
    double nextPeriod = 0; //period phase detection
    double onePeriod = 2*Math.PI/w;
    int dataAdded = 0;

    for(int i = 0; i < numberSegments; i++){ //iterate over all the segments necessary
      //System.out.println("Segment number: " + i);
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




  public static void iterateVariables(int[] indecies, double[][] info){
    //info: 2d array which looks like this:
    //yes, this code is quite awful. currently, it only iterates a hard-coded number of variables.
    // [[start, end, increment], [start, end, increment], [start, end, increment], ... ]
    //iterates three variables, testing the entire space of bounds.
    //assumes starting values are always lower than ending values.

    //for convinience:
    int i = indecies[0];
    int j = indecies[1];
    int k = indecies[2];

    //time for some O(n^3) stuff!
    for(variables[i] = info[0][0]; variables[i] <= info[0][1]; variables[i]+=info[0][2]){
      for(variables[j] = info[1][0]; variables[j] <= info[1][1]; variables[j]+=info[1][2]){
        for(variables[k] = info[2][0]; variables[k] <= info[2][1]; variables[k]+=info[2][2]){
          System.out.println("step: " + var[i]+"="+variables[i]+","+var[j]+"="+variables[j]+","+var[k]+"="+variables[k]);
          String pathX = path + var[i]+"="+variables[i]+","+var[j]+"="+variables[j]+","+var[k]+"="+variables[k]+"X";
          String pathV = path + var[i]+"="+variables[i]+","+var[j]+"="+variables[j]+","+var[k]+"="+variables[k]+"V";
          double[][] data = returnFirstFrame();
          outputData(data, pathX, pathV);
        }
      }
    }

  }



  public static void generateXVHeatMap(double boxSize){
    //iterate over the 2d grid, pixel by pixel. color each pixel by some metric.
    double[][] colors = new double[2*size][2*size];
    int numberBoxes = 0;

    //for assigning colors, we need to find the minimum and maximum box counting values!
    int min = 10000;
    int max = 0;

    for(int i = 0; i < 2*size; i++){
      for(int j = 0; j < 2*size; j++){
        //set positions
        variables[5] = minx+(((maxx-minx)/(2*size)) * (j+0.5));
        variables[6] = minv+(((maxv-minv)/(2*size)) * (i+0.5));
        //System.out.println(variables[5] + ", " +  variables[6]);

        //run simulation, and pick a color!
        double[][] data = returnFirstFrame();
        numberBoxes = countBoxes(data,boxSize);
        if(numberBoxes < min){
          min = numberBoxes;
        }
        else if(numberBoxes > max){
          max = numberBoxes;
        }
        //System.out.println(numberBoxes);
        colors[i][j] = numberBoxes;
        //System.out.println(colors[i][j]);

      }
      System.out.println("row number: " + i);
    }

    //show color map.

    //first reassign box counting values to range from 0 to 255!
    int range = max-min;
    for(int i = 0; i < colors.length; i++){
      for(int j = 0; j < colors[i].length; j++){
        colors[i][j] = ((colors[i][j] - min) * 255)/(range);
      }
    }

    System.out.println("all pixels done");
    JFrame frame = new JFrame("Color Map");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new ColorMap(size, colors));
    frame.pack();
    frame.setVisible(true);
  }



  public static int countBoxes(double[][] data, double boxSize){
    //counts number of squares of sidelength boxsize needed to cover all data points.
    //ASSUMES THAT THE DOMAIN IS A SQUARE.
    int numberBoxes = 0;

    //iterate over every box, add one if there's a point inside.
    for(double row = minv; row < maxv; row+=boxSize){
      for(double col = minx; col < maxx; col+=boxSize){

        //now check if a point exists in the box.
        for(double[] point : data){
          if(point[0] > col && point[0] < col+boxSize){
            if(point[1] > row && point[0] < row+boxSize){
              numberBoxes++;
              break;
            }
          }
        }

        //now that the presence of points has been checked, repeat for the next box.
      }
    }

    return numberBoxes;
  }





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
    frame.add(new Grapher(size, minx, minv, maxx, maxv));
    frame.pack();
    frame.setVisible(true);
  }







  public static void animatePeriods(double[][][] allPeriods){
    Data = allPeriods[0];
    JFrame frame = new JFrame("Animation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Grapher(size, minx,minv,maxx, maxv));
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
