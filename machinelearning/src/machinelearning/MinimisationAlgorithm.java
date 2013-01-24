/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machinelearning;

/**
 *
 * @author stewart
 */
public interface MinimisationAlgorithm
{
    void minimise(double[] theta, MinimisableFunction function);
}
