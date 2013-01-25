
package machinelearning;


/**
 * Represents a function which can be minimised.  It provides methods for getting
 * the value of the function given an input, and for getting the gradient of the
 * function at a given point.
 */
public interface MinimisableFunction
{
    double getValue(double[] at);
    double[] getGradient(double[] at);
}
