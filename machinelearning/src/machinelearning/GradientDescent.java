
package machinelearning;


/**
 * Implementation of the gradient descent optimisation algorithm
 */
public class GradientDescent implements MinimisationAlgorithm
{
    private int iterations;
    private double alpha;
    
    /**
     * Constructor.
     * @param iterations The maximum amount of iterations to use when waiting
     * for convergence.
     * @param alpha The learning rate.
     */
    public GradientDescent(int iterations, double alpha)
    {
        this.iterations = iterations;
        this.alpha = alpha;
    }
    
    @Override
    public void minimise(double[] theta, MinimisableFunction function)
    {
    	double[] gradient = new double[theta.length];
    	int mod = iterations / 100 * 5;
    	
        for (int i = 0; i < iterations; i++)
        {
            double cost = function.getCost(theta, gradient);
            
            for (int t = 0; t < theta.length; t++)
            {
                theta[t] = theta[t] - alpha * gradient[t];
            }
            
            if ((i % mod) == 0)
            {
            	System.out.printf("[%d%%]\tcost: %.4f ", (int)((double)i / iterations * 100), cost);
            	
            	double sum = 0;
            	
            	for (int j = 0; j < gradient.length; j++)
            		sum += gradient[j];
            	
            	System.out.printf("gradient: %.8f\n", sum);
            }
        }
    }
}
