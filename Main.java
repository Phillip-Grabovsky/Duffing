public class Main {
  public static void main(String[] args){
    double a = 1;
    double b = 1;
    double c = 1;
    double w = 1;
    double F = 0.855;
    double x = 0;
    double v = 0;

    double endTime = 10000;
    double resolution = 0.001;

    Oscillator theOscillator = new Oscillator(a,b,c,w,F,x,v);

    double[][] data = theOscillator.evalToTime(endTime,resolution);

    double nextPeriod = 0;
    for(int i = 0; i < data.length; i++){
      if(data[i][2] >= nextPeriod){
        System.out.println("(" + data[i][2] + ", " + data[i][0] + ", "  + data[i][1] + ")");
        nextPeriod+=2*Math.PI;
      }
    }
  }
}
