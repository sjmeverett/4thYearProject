
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
        for (int i = 0; i < iterations; i++)
        {
            double[] gradient = function.getGradient(theta);
            
            for (int t = 0; t < theta.length; t++)
            {
                theta[t] = theta[t] - alpha * gradient[t];
            }
        }
    }
}
