package machinelearning.neuralnetworks.v2.elementfunctions;

import machinelearning.neuralnetworks.v2.Matrix;

public class SigmoidGradientFunction implements Matrix.ElementFunction
{
	private Matrix a2;
	
	public SigmoidGradientFunction(Matrix a2)
	{
		this.a2 = a2;
	}
	
	@Override
	public double apply(double value, int row, int col, int offset)
	{
		double a = a2.data[offset];
		return value * a * (1- a);
	}
}