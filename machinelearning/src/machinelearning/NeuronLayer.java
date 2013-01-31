package machinelearning;

import java.util.Random;

public class NeuronLayer
{
	private static final double EPSILON = 1;

	public double[] input;
	public double[] output;
	public double[][] weights;
	public double[] errors;

	private int size, inputSize;
	private double[][] delta;
	

	public NeuronLayer(double[][] weights)
	{
		this.weights = weights;

		size = weights.length;
		inputSize = weights[0].length;

		delta = new double[size][inputSize + 1];
		output = new double[size];
		errors = new double[size];
	}


	public NeuronLayer(int size, int inputSize)
	{
		this.size = size;
		this.inputSize = inputSize;

		weights = new double[size][inputSize + 1];
		delta = new double[size][inputSize + 1];
		output = new double[size];
		errors = new double[size];

		initialiseWeights();
	}


	private void initialiseWeights()
	{
		Random random = new Random();

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < inputSize + 1; j++)
			{
				weights[i][j] = random.nextDouble() * EPSILON;
			}
		}
	}


	public void update(double[] input)
	{
		if (input.length != inputSize)
			throw new IllegalArgumentException(String.format("expected input size to be %d, not %d", inputSize, input.length));

		this.input = input;
		double sum;

		for (int i = 0; i < size; i++)
		{
			sum = weights[i][0];

			for (int j = 0; j < inputSize; j++)
			{
				sum += input[j] * weights[i][j + 1];
			}

			output[i] = activation(sum);
		}
	}


	public void calculateError(double[] target)
	{
		if (target.length != size)
			throw new IllegalArgumentException(String.format("expected 'expected' length to be %d, not %d", size, target.length));

		for (int i = 0; i < size; i++)
		{
			errors[i] = (target[i] - output[i]) * activationGradient(output[i]);
		}
	}


	public void calculateError(double[] successorErrors, double[][] successorWeights)
	{
		double sum;

		for (int i = 0; i < size; i++)
		{
			sum = 0;

			for (int j = 0; j < successorErrors.length; j++)
			{
				sum += successorErrors[j] * successorWeights[j][i + 1];
			}

			errors[i] = sum * activationGradient(output[i]);
		}
	}


	public void updateWeights(double learningRate, double momentum)
	{
		double d;

		for (int i = 0; i < size; i++)
		{
			weights[i][0] += learningRate * errors[i] + momentum * delta[i][0];
			delta[i][0] = errors[i];

			for (int j = 1; j <= inputSize; j++)
			{
				d = errors[i] * input[j - 1];
				weights[i][j] += learningRate * d + momentum * delta[i][j];
				delta[i][j] = d;
			}
		}
	}


	private double activation(double z)
	{
		return 1.0 / (1.0 + Math.exp(-z));
	}


	private double activationGradient(double a)
	{
		return a * (1 - a);
	}
}
