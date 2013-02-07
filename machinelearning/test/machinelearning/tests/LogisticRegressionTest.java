
package machinelearning.tests;

import machinelearning.MinimisationAlgorithm;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.junit.Before;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import machinelearning.GradientDescent;
import machinelearning.LogisticRegressionCostFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stewart
 */
public class LogisticRegressionTest
{
    private static final double DELTA = 10e-6;
    private static final int featureDegree = 6;
    private double[][] X;
    private double[] y;
    private double lambda;
    private double[] theta;
    private LogisticRegressionCostFunction f;
    
    
    public LogisticRegressionTest() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        lambda = 1;
        loadData();
    }
    
    
    @Before
    public void setup()
    {
        f = new LogisticRegressionCostFunction(X, y, lambda);
        theta = new double[X[0].length];
    }
    
    
    @Test
    public void initialCostTest()
    {
        //expected value taken from ml class tutorial 2
    	double[] gradient = new double[theta.length];
        double cost = f.getCost(theta, gradient);
        double expectedCost = 0.693147;
        
        assertEquals(expectedCost, cost, DELTA);
    }
    
    
    @Test
    public void initialGradientTest()
    {
        //expected values taken from ml class tutorial 2
        double[] gradient = new double[theta.length];
        f.getCost(theta, gradient);
        
        double[] expectedGradient = {
            8.4746e-03,
            1.8788e-02,
            7.7771e-05,
            5.0345e-02,
            1.1501e-02,
            3.7665e-02,
            1.8356e-02,
            7.3239e-03,
            8.1924e-03,
            2.3476e-02,
            3.9349e-02,
            2.2392e-03,
            1.2860e-02,
            3.0959e-03,
            3.9303e-02,
            1.9971e-02,
            4.3298e-03,
            3.3864e-03,
            5.8382e-03,
            4.4763e-03,
            3.1008e-02,
            3.1031e-02,
            1.0974e-03,
            6.3157e-03,
            4.0850e-04,
            7.2650e-03,
            1.3765e-03,
            3.8794e-02
        };
        
        assertArrayEquals(expectedGradient, gradient, DELTA);
    }
    
    @Test
    public void gradientDescentTest()
    {
        MinimisationAlgorithm min = new GradientDescent(1000, 1);
        min.minimise(theta, f);
        
        int correct = 0;
        
        for (int row = 0; row < X.length; row++)
        {
            double guess = 0;
            
            for (int column = 0; column < theta.length; column++)
            {
                guess += X[row][column] * theta[column];
            }
            
            if (guess >= 0.5 && y[row] == 1)
                correct++;
            else if (guess < 0.5 && y[row] == 0)
                correct++;
        }
        
        //we're using a less effective minimisation function, so allow a
        //reasonable margin for error
        assertEquals(83, ((double)correct / X.length) * 100, 5);
    }
    
    
    private void loadDataFromTxt() throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader("lrdata.txt"));
        String line;
        
        X = new double[118][28];
        y = new double[118];
        
        for (int row = 0; row < 118; row++)
        {
            line = reader.readLine();
            String[] rowData = line.split(",");
            
            X[row][0] = 1.0;
            double x1 = Double.parseDouble(rowData[0].trim());
            double x2 = Double.parseDouble(rowData[1].trim());
            
            int column = 1;
            
            for (int i = 1; i <= featureDegree; i++)
            {
                for (int j = 0; j <= i; j++)
                {
                    X[row][column++] = (Math.pow(x1, i - j) * Math.pow(x2, j));
                }
            }
            
            y[row] = Double.parseDouble(rowData[rowData.length - 1].trim());
        }
    }
    
    
    private void loadData() throws IOException, ClassNotFoundException
    {
        ObjectInputStream input = new ObjectInputStream(new FileInputStream("lrdata.data"));
        X = (double[][])input.readObject();
        y = (double[])input.readObject();
    }
}
