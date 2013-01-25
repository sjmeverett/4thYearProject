
package machinelearning;


public class NeuralNetworkCostFunction implements MinimisableFunction
{
    private double[][] X, Xt;
    private double[][] y;
    private int[] layers;
    private double lambda;

    
    public NeuralNetworkCostFunction(double[][] X, double[][] y, double lambda)
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
    public double getValue(double[] theta)
    {
        int m = y.length;
        int layerstart = 0;
    	double[][] a = X;
    	
    	//run forward propagation through the layers
    	for (int layer = 0; layer < layers.length; layer++)
    	{
    		a = runlayer(a.clone(), theta, layerstart, layers[layer]);
    		layerstart += layers[layer];
    	}
    	
    	//calculate the total cost
    	double sum = 0;
    	
    	for (int i = 0; i < a.length; i++)
    	{
    		sum += sumcost(y[i], a[i]);
    	}
    	
    	//calculate the regularisation term
    	double regular = 0;
    	layerstart = 0;
    	
    	for (int layer = 0; layer < layers.length; layer++)
    	{
    		for (int t = layerstart + 1; t < layers[layer]; t++)
    		{
    			regular += theta[t] * theta[t];
    		}
    		
    		layerstart += layers[layer];
    	}
        
        return (1.0 / m) * sum + lambda / (2.0 * m) * regular;
    }
    
    
    @Override
    public double[] getGradient(double[] theta)
    {
    	double[] grad = new double[theta.length];
        
        return grad;   
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

    
    private double hForRow(double[] theta, int layerstart, double[] row)
    {
    	double result = theta[layerstart];
    	
    	for (int feature = 0; feature < row.length; feature++)
    	{
    		result += row[feature] * theta[layerstart + feature + 1];
    	}
    	
    	return result;
    }
    
    
    private double[][] runlayer(double[][] a, double[] theta, int layerstart, int layersize)
    {
    	double[][] result = new double[a.length][layersize];
    	
    	for (int node = 0; node < layersize; node++)
    	{
    		for (int row = 0; row < a.length; row++)
    		{
    			result[row][node] = hForRow(theta, layerstart + node * a[row].length, a[row]);
    		}
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
