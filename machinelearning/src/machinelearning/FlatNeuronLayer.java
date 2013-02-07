package machinelearning;

import java.util.Random;

public class FlatNeuronLayer implements NeuronLayer
{
	private static final double EPSILON = 1;

	public double[] input;
	public double[] output;
	public double[] weights;
	public double[] errors;

	private int size, inputSize;
	private double[] delta;
	

	public FlatNeuronLayer(double[] weights, int size)
	{
		this.weights = weights;

		this.size = size;
		this.inputSize = weights.length / size;

		delta = new double[weights.length];
		output = new double[size];
		errors = new double[size];
	}


	public FlatNeuronLayer(int size, int inputSize)
	{
		this.size = size;
		this.inputSize = inputSize;

		weights = new double[size * (inputSize + 1)];
		delta = new double[size * (inputSize + 1)];
		output = new double[size];
		errors = new double[size];

		initialiseWeights();
	}


	private void initialiseWeights()
	{
		Random random = new Random();

		for (int i = 0; i < weights.length; i++)
		{
			weights[i] = random.nextDouble() * EPSILON;
		}
	}


	@Override
	public void update(double[] input)
	{
		if (input.length != inputSize)
			throw new IllegalArgumentException(String.format("expected input size to be %d, not %d", inputSize, input.length));

		this.input = input;
		double sum;
		int offset = 0;
		
		for (int i = 0; i < size; i++)
		{
			sum = weights[offset++];

			for (int j = 0; j < inputSize; j++)
			{
				sum += input[j] * weights[offset++];
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
		FlatNeuronLayer flatSuccessor = (FlatNeuronLayer)successor;
		double[] successorErrors = flatSuccessor.errors;
		double[] successorWeights = flatSuccessor.weights;
		
		double sum;
		int offset;
		
		for (int i = 0; i < size; i++)
		{
			sum = 0;
			offset = i + 1;

			for (int j = 0; j < successorErrors.length; j++)
			{
				sum += successorErrors[j] * successorWeights[offset];
				offset += inputSize + 1;
			}

			errors[i] = sum * activationGradient(output[i]);
		}
	}


	@Override
	public void updateWeights(double learningRate, double lambda, double[][] delta, int m)
	{
//		double d;
//		int offset = 0;
//		
//		for (int i = 0; i < size; i++)
//		{
//			weights[offset] += learningRate * errors[i] + lambda * delta[offset];
//			delta[offset] = errors[i];
//			offset++;
//			
//			for (int j = 1; j <= inputSize; j++)
//			{
//				d = errors[i] * input[j - 1];
//				weights[offset] += learningRate * d + lambda * delta[offset];
//				delta[offset] = d;
//				offset++;
//			}
//		}
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
