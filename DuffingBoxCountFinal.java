
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.lang.Math;
/**
 * Write a description of class DuffO here.
 *
 * @Ishaan Lal
 * @24 July 2019
 */
public class DuffingBoxCountFinal
{

    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in), inf = null;
        boolean valid;
        ArrayList<Double> list = new ArrayList<Double>();
        System.out.println("Enter file name");
        String input = scan.next();
        double[] slopes = new double[4];
        double[] rSquared = new double[4];
        double[] kval = new double[4];

        //determines if the file is found
        do{
            valid = true;
            try
            {
                inf = new Scanner(new File(input));
                System.out.println("found");
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found.");
                valid = false;
            }
        }
        while(!valid);

        //Puts values into a list.
        while(inf.hasNext())
        {
            double line = inf.nextDouble();
            //Double val = Double.parseDouble(line);
            //line*=(800/3);
            list.add(line);
        }
        inf.close();

        //SECOND FILE

        System.out.println("Enter file name");
        String input2 = scan.next();
        boolean valid2;
        ArrayList<Double> list2 = new ArrayList<Double>();

        //Determines if the file is found or not.

        do{
            valid2 = true;
            try
            {
                inf = new Scanner(new File(input2));
                System.out.println("found");
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found.");
                valid2 = false;
            }
        }
        while(!valid2);

        //Puts values into a list.

        while(inf.hasNext())
        {
            double line2 = inf.nextDouble();
            //double val2 = Double.parseDouble(line2);
            //line2*= (800/3);
            list2.add(line2);
        }
        inf.close();

        //print arraylist of first entered file
        System.out.println();
        System.out.print(input + " : ");
        for(int i=0; i<list.size(); i++)
        {
            System.out.print(list.get(i) + "\t");
        }
        System.out.println();
        //print arraylist of second entered file
        System.out.print(input2 + " : ");
        for(int i =0; i<list2.size(); i++)
        {
            System.out.print(list2.get(i) + "\t");
        }

        System.out.println();
        System.out.println("How many values to delete?");
        int numDelete = scan.nextInt();
        for(int i =0; i<numDelete; i++)
        {
            list.remove(0);
            list2.remove(0);
        }

        System.out.println();
        System.out.print(input + " : ");
        for(int i=0; i<list.size(); i++)
        {
            System.out.print(list.get(i) + "\t");
        }
        System.out.println();
        //print arraylist of second entered file
        System.out.print(input2 + " : ");
        for(int i =0; i<list2.size(); i++)
        {
            System.out.print(list2.get(i) + "\t");
        }

        double x2 = list.get(0);
        for(int i=1; i<list.size(); i++)
        {
            if(x2 < list.get(i))
            {
                x2 = list.get(i);
            }
        }
        System.out.println();
        System.out.println("x2: " + x2);

        double y2 = list2.get(0);
        for(int i =1; i<list2.size(); i++)
        {
            if(y2 < list2.get(i))
            {
                y2 = list2.get(i);
            }
        }
        System.out.println("y2: " + y2);

        double x1 = list.get(0);
        for(int i =1; i<list.size(); i++)
        {
            if(x1 > list.get(i))
            {
                x1 = list.get(i);
            }
        }
        System.out.println("x1: " + x1);

        double y1 = list2.get(0);
        for(int i =1; i<list2.size(); i++)
        {
            {
                if(y1 > list2.get(i))
                {
                    y1 = list2.get(i);
                }
            }
        }
        System.out.println("y1: " + y1);

        // it works. I think
        double widthx = x2 - x1;
        double heighty = y2 - y1;
        System.out.println("The width of the graph is: " + widthx);
        System.out.println("The height of the graph is: " + heighty);
        double[] oneOverR = new double[40];
        double[] NR = new double[40];

        for(int ishaan =0; ishaan<4; ishaan++)
        {
            double initial = 0.00375;
            double[] increments = new double[] {.003, 0.0035, 0.03, 0.035};
            

            //System.out.println("Input the width of box: ");
            //String b = scan.next();
            double boxSize = initial;
            double numboxwidth = (widthx/boxSize);
            double numboxheight = (heighty/boxSize);
            int numBoxes = (int)(numboxwidth*numboxheight);
            //System.out.println("Input the increment (add neg if needed plz)");
            //String c = scan.next();
            double increment = increments[ishaan];

            for(int q =0; q<40; q++)
            {

                //int[][] twoarray = new int[numboxwidth][numboxheight];
                numboxwidth = (int)(widthx/boxSize);
                numboxheight = (int)(heighty/boxSize);
                numBoxes = (int)(numboxwidth*numboxheight);
                System.out.println(numBoxes);
                int[] counter = new int[5*numBoxes];
                int countering = 0;
                double dx1 = x1*1.0;
                double dx2 = x2*1.0;
                double dy1 = y1*1.0;
                double dy2 = y2*1.0;
                int idk = 0;

                for(double i = dx1; i<dx2-boxSize; i+=boxSize)
                {
                    for(double j = dy1; j<dy2-boxSize; j+=boxSize)
                    {
                        for(int k=0; k<list.size(); k++)
                        {
                            if(list.get(k) >= i && list.get(k) < i+boxSize && 
                            list2.get(k) >= j && list2.get(k) < j+boxSize)
                            {
                                counter[countering]++;
                                break;
                            }

                        }
                        countering++;
                    }
                    idk = countering;

                }

                for(double i = dx1; i< dx2-boxSize; i+=boxSize)
                {
                    for(int k =0; k<list.size(); k++)
                    {
                        if(list.get(k) >= i && list.get(k) < i+boxSize && 
                        list2.get(k) >= dy2-boxSize && list2.get(k) <= dy2)
                        {
                            counter[idk]++;
                        }
                    }
                    idk++;
                }
                //idk++;
                for(double i = dy1; i< dy2-boxSize; i+=boxSize)
                {
                    for(int k =0; k<list.size(); k++)
                    {
                        if(list.get(k) >= dx2-boxSize && list.get(k) <= dx2 && 
                        list2.get(k) >= i && list2.get(k) < i+boxSize)
                        {
                            counter[idk]++;
                        }
                    }
                    idk++;
                }
                //idk++;
                for(int k=0; k<list.size(); k++)
                {
                    if(list.get(k) >= dx2-boxSize && list.get(k) <= dx2 &&
                    list2.get(k) >= dy2-boxSize && list2.get(k) <= dy2)
                    {
                        counter[idk]++;
                    }
                }

                double count = 0;
                for(int i=0; i<counter.length; i++)
                {
                    if(counter[i] >0)
                    {
                        count++;
                    }  

                }

                NR[q] = Math.log10(count);
                oneOverR[q] = Math.log10(1/boxSize);
                System.out.println("BOX COUNT: " + count);
                System.out.println("BOX SIZE: " + boxSize);

                boxSize += increment;
            }

            //BOX COUNTING ENDS HERE
            System.out.println();
            for(int i =0; i<NR.length; i++)
            {
                System.out.print(NR[i] + " ");
            }

            System.out.println();
            for(int i =0; i<oneOverR.length; i++)
            {
                System.out.print(oneOverR[i] + " ");
            }

            //Regression stuff starts here.
            double intercept, slope;
            double r2;
            double svar0, svar1;
            if (oneOverR.length != NR.length) {
                throw new IllegalArgumentException("array lengths are not equal");
            }
            int n = oneOverR.length;

            // first pass
            double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
            for (int i = 0; i < n; i++) {
                sumx  += oneOverR[i];
                sumx2 += oneOverR[i]*oneOverR[i];
                sumy  += NR[i];
            }
            double xbar = sumx / n;
            double ybar = sumy / n;

            // second pass: compute summary statistics
            double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
            for (int i = 0; i < n; i++) {
                xxbar += (oneOverR[i] - xbar) * (oneOverR[i] - xbar);
                yybar += (NR[i] - ybar) * (NR[i] - ybar);
                xybar += (oneOverR[i] - xbar) * (NR[i] - ybar);
            }
            slope  = xybar / xxbar;
            intercept = ybar - slope * xbar;

            // more statistical analysis
            double rss = 0.0;      // residual sum of squares
            double ssr = 0.0;      // regression sum of squares
            for (int i = 0; i < n; i++) {
                double fit = slope*oneOverR[i] + intercept;
                rss += (fit - NR[i]) * (fit - NR[i]);
                ssr += (fit - ybar) * (fit - ybar);
            }

            int degreesOfFreedom = n-2;
            r2    = ssr / yybar;
            double svar  = rss / degreesOfFreedom;
            svar1 = svar / xxbar;
            svar0 = svar/n + xbar*xbar*svar1;
            System.out.println('\n' + "slope is: " + slope);
            System.out.println("intercept is: " + intercept + " so k = " + Math.pow(10, intercept));
            System.out.println("R^2 value is: " + r2);
            
            slopes[ishaan] = slope;
            rSquared[ishaan] = r2;
            kval[ishaan] = Math.pow(10,intercept);
        }
        
        System.out.println();
        System.out.print("Slopes:" + "\t");
        for(int i=0; i<4; i++)
        {
            System.out.print(slopes[i] + "\t");
        }
        
        System.out.println();
        System.out.print("rSquared:" + "\t");
        for(int i=0; i<4; i++)
        {
            System.out.print(rSquared[i] + "\t");
        }
        
        System.out.println();
        System.out.print("kval:" + "\t");
        for(int i=0; i<4; i++)
        {
            System.out.print(kval[i] + "\t");
        }
    }
}
