
package machinelearning;


public class LogisticRegressionCostFunction implements MinimisableFunction
{
    private double[][] X, Xt;
    private double[] y;
    private double lambda;

    
    public LogisticRegressionCostFunction(double[][] X, double[] y, double lambda)
    {
        this.X = X;
        this.y = y;
        this.lambda = lambda;
        
        //store X'
        //otherwise, getGradient has to do column-wise iteration which will
        //result in loads of cache misses and slow things down
        this.Xt = transpose(X.clone());
    }
    
    
    @Override
    public double getCost(double[] theta, double[] gradient)
    {
        int m = y.length;
        double[] h = h(X, theta);
        
        //grad = (1 / m) * (X' * (h - y) + lambda * [0; theta(2:end)]);
        //calculate the error in the current prediction (h - y)
        double[] error = new double[h.length];
        
        for (int i = 0; i < h.length; i++)
        {
            error[i] = h[i] - y[i];
        }
        
        //calculate the gradient vector
        for (int row = 0; row < theta.length; row++)
        {
            for (int col = 0; col < X.length; col++)
            {
                gradient[row] += Xt[row][col] * error[col];
            }
            
            if (row > 0)
                gradient[row] += lambda * theta[row];
            
            gradient[row] /= m;
        }
                
        //J = (1 / m) * sum(cost(h(x),y)) + lambda / (2 * m) * sum(theta ^ 2)
        return (1.0 / m) * sumcost(y, h) + lambda / (2.0 * m) * sumSquared(theta);
    }
    
    
    private double sigmoid(double z)
    {
        return 1.0 / (1 + Math.exp(-z));
    }
    
    
    private double[] h(double[][] X, double[] theta)
    {
        double[] h = new double[X.length];
        
        //h = sigmoid(X * theta)
        for (int row = 0; row < X.length; row++)
        {
            for (int col = 0; col < theta.length; col++)
            {
                h[row] += X[row][col] * theta[col];
            }
            
            h[row] = sigmoid(h[row]);
        }
        
        return h;
    }
    
    
    private double sumcost(double[] y, double[] h)
    {
        double result = 0;
        
        for (int i = 0; i < y.length; i++)
        {
            result -= (y[i] == 1) ? Math.log(h[i]) : Math.log(1 - h[i]);
        }
        
        return result;
    }
    
    
    private double sumSquared(double[] theta)
    {
        double result = 0;
        
        //don't regularise theta 0
        for (int i = 1; i < theta.length; i++)
        {
            result += theta[i] * theta[i];
        }
        
        return result;
    }
    
    
    private double[][] transpose(double[][] matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[cols][rows];
        
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                result[col][row] = matrix[row][col];
            }
        }
        
        return result;
    }
}
