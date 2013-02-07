package machinelearning.neuralnetworks.v1;

import java.util.Random;

public class BasicNeuronLayer implements NeuronLayer
{
	private static final double EPSILON = 0.5;

	public double[] input;
	public double[] output;
	public double[][] weights;
	public double[] errors;

	private int size, inputSize;
	

	public BasicNeuronLayer(double[][] weights)
	{
		this.weights = weights;

		size = weights.length;
		inputSize = weights[0].length - 1;

		output = new double[size];
		errors = new double[size];
	}


	public BasicNeuronLayer(int size, int inputSize)
	{
		this.size = size;
		this.inputSize = inputSize;

		weights = new double[size][inputSize + 1];
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
				weights[i][j] = random.nextDouble() * 2 * EPSILON - EPSILON;
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


	@Override
	public void calculateError(double[] target)
	{
		if (target.length != size)
			throw new IllegalArgumentException(String.format("expected 'expected' length to be %d, not %d", size, target.length));

		for (int i = 0; i < size; i++)
		{
			errors[i] = output[i] - target[i];
		}
	}


	@Override
	public void calculateError(NeuronLayer successor)
	{
		BasicNeuronLayer basicSuccessor = (BasicNeuronLayer)successor;
		double[] successorErrors = basicSuccessor.errors;
		double[][] successorWeights = basicSuccessor.weights;
		
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
	
	
	public void calculateDelta(double[][] delta)
	{
		for (int i = 0; i < size; i++)
		{
			delta[i][0] += errors[i];
			
			for (int j = 1; j <= inputSize; j++)
			{
				delta[i][j] += errors[i] * input[j - 1];
			}
		}
	}


	@Override
	public void updateWeights(double learningRate, double lambda, double[][] delta, int m)
	{
		for (int i = 0; i < size; i++)
		{
			weights[i][0] -= learningRate / m * delta[i][0];
			delta[i][0] = 0;

			for (int j = 1; j <= inputSize; j++)
			{
				weights[i][j] -= learningRate / m * delta[i][j] + lambda / m * weights[i][j];
				delta[i][j] = 0;
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
