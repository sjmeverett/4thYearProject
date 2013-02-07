package machinelearning.neuralnetworks.v2.elementfunctions;

import machinelearning.neuralnetworks.v2.Matrix;

public class CostFunction implements Matrix.ElementFunction
{
	private double[][] y;
	
	public CostFunction(double[][] y)
	{
		this.y = y;
	}
	
	@Override
	public double apply(double value, int row, int col, int offset)
	{
		if (y[row][col] == 1)
			return -Math.log(value);
		else
			return -Math.log(1 - value);
	}
}