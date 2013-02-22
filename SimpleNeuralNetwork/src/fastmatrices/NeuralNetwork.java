package fastmatrices;

import java.util.Random;

public class NeuralNetwork
{
	public Matrix theta1, theta2;
	
	
	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes)
	{
		theta1 = new Matrix(randomArray(hiddenNodes, inputNodes + 1), hiddenNodes, inputNodes + 1);
		theta2 = new Matrix(randomArray(outputNodes, hiddenNodes + 1), outputNodes, hiddenNodes + 1);
	}
	
	
	public void train(Matrix x, Matrix y, final double learningRate, int iterations)
	{
		Matrix a1, a2, a3 = null, t = null, d3, d2;
		int mod = iterations / 5;
		
		for (int i = 0; i < iterations; i++)
		{			
			for (int j = 1; j <= x.rows; j++)
			{
				a1 = x.getRowAsColumn(j);
				a2 = calculateLayer(theta1, a1);
				a3 = calculateLayer(theta2, a2);
				
				t = y.getRowAsColumn(j);
				
				d3 = calculateOutputError(t, a3);
				d2 = calculateHiddenError(theta2, d3, a2);
				
				updateWeights(theta1, d2, a1, learningRate);
				updateWeights(theta2, d3, a2, learningRate);
			}
			
			if (i % mod == 0)
			{
				Matrix d = t.subtract(a3);
				Matrix e = d.multiplyTransposeOp1(d);
				System.out.println(e.data[0]);
			}
		}
	}
	
	
	public double[] predict(Matrix x)
	{
		Matrix a1 = Utilities.appendVertical(1, x);
		Matrix a2 = Utilities.appendVertical(1, theta1.multiply(a1, sig));
		Matrix a3 = theta2.multiply(a2, sig);
		
		return a3.transpose().data;
	}
	
	
	public double[][] bulkPredict(Matrix x)
	{
		double[][] answer = new double[x.rows][];
		
		for (int i = 0; i < x.rows; i++)
		{
			answer[i] = predict(x.part(i + 1, i + 1, 1, -1).transpose());
		}
		
		return answer;
	}
	
	
	/**
	 * Calculates the activations matrix given the previous layer's activations
	 * and the weights for this layer,
	 * i.e., calculates sigmoid(theta * activations).
	 * @param theta
	 * @param activations
	 * @return
	 */
	private Matrix calculateLayer(Matrix theta, Matrix activations)
	{
		double[] answer = new double[theta.rows];
		int answerindex = 0, thetaindex = 0, activationsindex;
		
		if (theta.columns != (activations.rows + 1))
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (theta is %dx%d, activations is %dx%d)",
				theta.rows, theta.columns, activations.rows, activations.columns));
		
		for (int i = 0; i < theta.rows; i++)
		{
			double sum = theta.data[thetaindex];
			activationsindex = 0;
			
			for (int k = 0; k < activations.rows; k++)
			{
				sum += theta.data[thetaindex + k + 1] * activations.data[activationsindex++];
			}
			
			answer[answerindex++] = 1.0 / (1 + Math.exp(-sum));
			thetaindex += theta.columns;
		}

		return new Matrix(answer, theta.rows, activations.columns);
	}
	
	
	/**
	 * Calculates the output error given the expected output and the actual output.
	 * @param target The expected output.
	 * @param activations The actual output.
	 * @return
	 */
	private Matrix calculateOutputError(Matrix target, Matrix activations)
	{
		double[] answer = new double[target.rows];
		
		for (int i = 0; i < target.rows; i++)
		{
			double a = activations.data[i];
			answer[i] = (target.data[i] - a) * a * (1 - a);
		}
		
		return new Matrix(answer, target.rows, 1);
	}
	
	
	/**
	 * Calculates the hidden layer error given the output layer weights and error,
	 * and the hidden layer activations.
	 * @param theta The output layer weights.
	 * @param delta The output layer error.
	 * @param activations The hidden layer activations.
	 * @return
	 */
	private Matrix calculateHiddenError(Matrix theta, Matrix delta, Matrix activations)
	{
		double[] answer = new double[theta.columns - 1];
		int answerindex = 0, thetaindex, deltaindex;
		
		if (theta.rows != delta.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				theta.rows, theta.columns, delta.rows, delta.columns));
		
		for (int i = 1; i < theta.columns; i++)
		{
			double sum = 0;
			thetaindex = i;
			deltaindex = 0;
			
			for (int k = 0; k < delta.rows; k++)
			{
				sum += theta.data[thetaindex] * delta.data[deltaindex];
				thetaindex += theta.columns;
				deltaindex += delta.columns;
			}
			
			double d = activations.data[i - 1];
			answer[answerindex++] = sum * d * (1 - d);
		}
		
		return new Matrix(answer, theta.columns - 1, 1);
	}
	
	
	/**
	 * Updates the weights according to the specified parameters,
	 * i.e., calculates weights += learningRate * delta * activations'.
	 * @param weights
	 * @param delta
	 * @param activations
	 * @param learningRate
	 * @return
	 */
	private Matrix updateWeights(Matrix weights, Matrix delta, Matrix activations, double learningRate)
	{
		int answerindex = 0, deltaindex = 0, activationsindex;
		
		if (delta.columns != activations.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (delta is %dx%d, activations is %dx%d)",
				delta.rows, delta.columns, activations.rows, activations.columns));
		
		if (delta.rows != weights.rows || (activations.rows + 1) != weights.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (weights is %dx%d, delta * activations' is %dx%d)",
				weights.rows, weights.columns, delta.rows, activations.rows));
		
		for (int i = 0; i < delta.rows; i++)
		{
			weights.data[answerindex++] += delta.data[deltaindex] * learningRate;
			activationsindex = 0;
			
			for (int j = 0; j < activations.rows; j++)
			{
				weights.data[answerindex++] += delta.data[deltaindex] * activations.data[activationsindex] * learningRate;
				activationsindex += activations.columns;
			}
			
			deltaindex += delta.columns;
		}
		
		return weights;
	}
	
	
	/**
	 * Calculates the sigmoid activation function.
	 */
	private Matrix.Function sig = new Matrix.Function()
	{
		@Override
		public double apply(double value, int row, int col)
		{
			return 1.0 / (1 + Math.exp(-value));
		}
		
	};
	
	
	private static double[] randomArray(int rows, int columns)
	{
		double[] a = new double[rows * columns];
		Random random = new Random(1);
		int index = 0;
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				a[index++] = random.nextDouble();
			}
		}
		
		return a;
	}
}
