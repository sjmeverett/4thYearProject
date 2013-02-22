package matrices;

import java.util.Random;

public class NeuralNetwork
{
	public Matrix theta1, theta2;
	
	
	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes)
	{
		theta1 = new Matrix(randomArray(hiddenNodes, inputNodes + 1));
		theta2 = new Matrix(randomArray(outputNodes, hiddenNodes + 1));
	}
	
	
	public void train(Matrix x, Matrix y, double learningRate, int iterations)
	{
		Matrix a1, a2, a3 = null, t = null, d3, d2;
		int mod = iterations / 5;
		
		for (int i = 0; i < iterations; i++)
		{
			for (int j = 1; j <= x.rows; j++)
			{
				a1 = Utilities.appendVertical(1, x.part(j, j, 1, -1).transpose());
				a2 = Utilities.appendVertical(1, theta1.multiply(a1).apply(sig));
				a3 = theta2.multiply(a2).apply(sig);
				
				t = y.part(j, j, 1, -1).transpose();
				d3 = t.subtract(a3).elementMultiply(a3.apply(sigd));
				
				d2 = theta2.part(1, -1, 2, -1).transpose().multiply(d3)
						.elementMultiply(a2.part(2, -1, 1, 1).apply(sigd));
				
				theta1 = theta1.add(d2.multiply(a1.transpose()).scale(learningRate));
				theta2 = theta2.add(d3.multiply(a2.transpose()).scale(learningRate));
			}
			
			if (i % mod == 0)
			{
				Matrix d = t.subtract(a3);
				Matrix e = d.transpose().multiply(d);
				System.out.println(e.data[0][0]);
			}
		}
	}
	
	
	public double[] predict(double[] x)
	{
		Matrix a1 = Utilities.appendVertical(1, new Matrix(new double[][] { x }).transpose());
		Matrix a2 = Utilities.appendVertical(1, theta1.multiply(a1).apply(sig));
		Matrix a3 = theta2.multiply(a2).apply(sig);
		
		return a3.transpose().data[0];
	}
	
	
	public Matrix bulkPredict(Matrix x)
	{
		double[][] answer = new double[x.rows][];
		
		for (int i = 0; i < x.rows; i++)
		{
			answer[i] = predict(x.data[i]);
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Calculates the sigmoid activation function.
	 */
	private Matrix.Function sig = new Matrix.Function()
	{
		@Override
		public double apply(double value)
		{
			return 1.0 / (1 + Math.exp(-value));
		}
		
	};
	
	
	/**
	 * Calculates the gradient of the sigmoid activation function.
	 */
	private Matrix.Function sigd = new Matrix.Function()
	{
		@Override
		public double apply(double value)
		{
			return value * (1 - value);
		}
	};
	
	
	private static double[][] randomArray(int rows, int columns)
	{
		double[][] a = new double[rows][columns];
		Random random = new Random(1);
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				a[i][j] = random.nextDouble();
			}
		}
		
		return a;
	}
}
