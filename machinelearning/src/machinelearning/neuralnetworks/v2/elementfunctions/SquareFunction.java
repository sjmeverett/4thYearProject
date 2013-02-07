package machinelearning.neuralnetworks.v2.elementfunctions;

import machinelearning.neuralnetworks.v2.Matrix;

public class SquareFunction implements Matrix.ElementFunction
{
	@Override
	public double apply(double value, int row, int col, int offset)
	{
		return value * value;
	}
}