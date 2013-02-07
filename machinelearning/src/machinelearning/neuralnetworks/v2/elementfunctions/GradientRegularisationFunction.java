package machinelearning.neuralnetworks.v2.elementfunctions;

import machinelearning.neuralnetworks.v2.Matrix;

public class GradientRegularisationFunction  implements Matrix.ElementFunction
{
	private double scale;
	private double lambdascale;
	private Matrix theta;
	
	public GradientRegularisationFunction(int m, double lambda, Matrix theta)
	{
		this.scale = 1.0 / m;
		this.lambdascale = lambda / m;
		this.theta = theta;
	}
	
	@Override
	public double apply(double value, int row, int col, int offset)
	{
		value *= scale;
		
		if (col != 0)
			value += lambdascale * theta.data[offset];
		
		return value;
	}
}