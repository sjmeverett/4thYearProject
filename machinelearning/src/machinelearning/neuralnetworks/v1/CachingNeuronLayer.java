package machinelearning.neuralnetworks.v1;

import java.util.Random;

public class CachingNeuronLayer implements NeuronLayer
{
	private static final double EPSILON = 1;

	public double[] input;
	public double[] output;
	public double[][] weights;
	public double[] errors;

	private int size, inputSize;
	private double[][] delta;
	

	public CachingNeuronLayer(double[][] weights)
	{
		this.weights = weights;

		size = weights.length;
		inputSize = weights[0].length - 1;

		delta = new double[size][inputSize + 1];
		output = new double[size];
		errors = new double[size];
	}


	public CachingNeuronLayer(int size, int inputSize)
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


	@Override
	public void update(double[] input)
	{
		if (input.length != inputSize)
			throw new IllegalArgumentException(String.format("expected input size to be %d, not %d", inputSize, input.length));

		this.input = input;
		double sum;
		double weightsi[];

		for (int i = 0; i < size; i++)
		{
			weightsi = weights[i];
			sum = weightsi[0];

			for (int j = 0; j < inputSize; j++)
			{
				sum += input[j] * weightsi[j + 1];
			}

			output[i] = activation(sum);
		}
	}


	@Override
	public void calculateError(double[] target)
	{
		if (target.length != size)
			throw new IllegalArgumentException(String.format("expected 'expected' length to be %d, not %d", size, target.length));

		for (int i = 0; i < size; i++)
		{
			errors[i] = (target[i] - output[i]) * activationGradient(output[i]);
		}
	}


	@Override
	public void calculateError(NeuronLayer successor)
	{
		CachingNeuronLayer cachingSuccessor = (CachingNeuronLayer)successor;
		double[] successorErrors = cachingSuccessor.errors;
		double[][] successorWeights = cachingSuccessor.weights;
		
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


	@Override
	public void updateWeights(double learningRate, double lambda, double[][] delta, int m)
	{
		double d;
		double[] weightsi, deltai;
		double errorsi;

		for (int i = 0; i < size; i++)
		{
			weightsi = weights[i];
			deltai = delta[i];
			errorsi = errors[i];
			
			weightsi[0] += learningRate * errorsi + lambda * deltai[0];
			deltai[0] = errorsi;

			for (int j = 1; j <= inputSize; j++)
			{
				d = errorsi * input[j - 1];
				weightsi[j] += learningRate * d + lambda * deltai[j];
				deltai[j] = d;
			}
		}
	}
	
	
	@Override
	public double[] getOutput()
	{
		return output;
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
