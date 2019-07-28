import java.lang.Math;

public class Oscillator{
    //parameters for the oscillator
    private double a; //cubic force term; quartic potential
    private double b; //linear force term; quadratic potential
    private double c; //damping constant
    private double w; //frequency of driver
    private double F; //force of driver

    //initial conditions/state variables
    private double x;
    private double v;
    private double t;

    public void timeStep(double dt){
      //change the system's state by one small iteration
      //calculates acceleration from the duffing oscillator
      double A = F*Math.cos(w*t) -a*Math.pow(x,3) - b*x - c*v;
      x+=dt*v;
      v+=dt*A;
      t+=dt;
      //System.out.println(x + ", " + v + ", " + A);
    }

    public double[][] evalToTime(double endTime, double res){
      //evaluates an interval starting at the current time and ending at endTime, outputs a list of
      // eval() results.
      int numberSteps = (int)(Math.round(endTime/res));
      double[][] theData = new double[numberSteps][3];
      for(int timeNumber = 0; timeNumber < numberSteps; timeNumber++){
        theData[timeNumber] = new double[] {x, v, t};
        timeStep(res);
      }

      return(theData);
    }

    public double[] evalAtTime(double timeGoal, double res){
      //evaluates to a certain timeGoal, then outputs results at that time.
      //ideally, timeGoal should be a multiple of
      while(t < timeGoal){
        timeStep(res);
        t+=res;
      }

      return(new double[] {x,v,t});
    }

    public double[] eval(){
      //returns the current state of the system.
      return(new double[] {x, v, t});
    }

    public Oscillator(double[] variables){
      t = 0;
      this.a = variables[0];
      this.b = variables[1];
      this.c = variables[2];
      this.w = variables[3];
      this.F = variables[4];
      this.x = variables[5];
      this.v = variables[6];
    }
}
