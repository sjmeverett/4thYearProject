package pacman.entries.pacman.neuralnetworks;

import java.util.Random;

/**
 * Implements a neural network using ideas in chapter 8 of "An Introduction
 * to Neural Computing, Second Edition" by Igor Aleksander and Helen Morton,
 * which in turn summarises the findings in "Learning internal representations
 * by error propagation" by Rumelhart, Hinton and Williams (in "Parallel
 * Distributed Processing: Explorations in the Microstructure of Cognition",
 * edited by Rumelhart and McClelland).
 * 
 * The neural network consists of an input layer, hidden layer and an output
 * layer, where the output of a neuron is given by the activation function of
 * the sum of the incoming connections modified by corresponding weights.
 * 
 * The activation function used is a sigmoid function, namely 1 / (1 + e^-a).
 * 
 * The weights are modified iteratively using a backpropagation algorithm.
 */
public class NeuralNetwork
{
	private static final double MINSQUAREDERROR = 10e-6;
	public Matrix theta1, theta2;
	
	
	/**
	 * Creates a new neural network of the specified size.
	 * @param inputNodes The number of units in the input layer.
	 * @param hiddenNodes The number of units in the hidden layer.
	 * @param outputNodes The number of units in the output layer.
	 */
	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes)
	{
		theta1 = new Matrix(randomArray(hiddenNodes, inputNodes + 1), hiddenNodes, inputNodes + 1);
		theta2 = new Matrix(randomArray(outputNodes, hiddenNodes + 1), outputNodes, hiddenNodes + 1);
	}
	
	
	/**
	 * Creates a new neural network of the specified size using the specified initial weights.
	 * @param inputNodes
	 * @param hiddenNodes
	 * @param outputNodes
	 * @param theta1
	 * @param theta2
	 */
	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, double[] theta1, double[] theta2)
	{
		this.theta1 = new Matrix(theta1, hiddenNodes, inputNodes + 1);
		this.theta2 = new Matrix(theta2, outputNodes, hiddenNodes + 1);
	}
	
	
	/**
	 * Trains the neural network with the specified example.
	 * @param x The example input.
	 * @param y The corresponding expected output for the input.
	 * @param learningRate A parameter affecting how quickly the network learns.
	 * @param iterations The number of iterations to train over.
	 */
	public void train(double[] x, double[] y, double learningRate, int iterations)
	{
		Matrix a1, a2, a3 = null, t = null, d3, d2;
		int mod = iterations / 5;
		
		a1 = new Matrix(x, x.length, 1);
		t = new Matrix(y, y.length, 1);
		
		for (int i = 0; i < iterations; i++)
		{
			a2 = calculateLayer(theta1, a1);
			a3 = calculateLayer(theta2, a2);
			
			d3 = calculateOutputError(t, a3);
			d2 = calculateHiddenError(theta2, d3, a2);
			
			updateWeights(theta1, d2, a1, learningRate);
			updateWeights(theta2, d3, a2, learningRate);
			
			if (i % mod == 0)
			{
				Matrix d = t.subtract(a3);
				Matrix e = d.multiplyTransposeOp1(d);

				if (e.data[0] < MINSQUAREDERROR)
					return;
			}
		}
	}
	
	
	/**
	 * Predicts the output for a given input using the learned weights.
	 * @param x
	 * @return
	 */
	public double[] predict(double[] x)
	{
		Matrix a1 = new Matrix(x, x.length, 1);
		Matrix a2 = calculateLayer(theta1, a1);
		Matrix a3 = calculateLayer(theta2, a2);
		
		return a3.data;
	}
	
	
	/**
	 * Given the input x, predicts the class with the highest probability
	 * using the network's learned weights.
	 * @param x The input data.
	 * @param classes An array of classes in the same order as the output nodes.
	 * @return
	 */
	public Object predict(double[] x, Object[] classes)
	{
		double[] y = predict(x);
		Object output = null;
		double max = 0;
		
		for (int i = 0; i < y.length; i++)
		{
			if (y[i] > max)
			{
				max = y[i];
				output = classes[i];
			}
		}
		
		return output;
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
		int thetaindex = 0;
		
		if (theta.columns != (activations.rows + 1))
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (theta is %dx%d, activations is %dx%d)",
				theta.rows, theta.columns, activations.rows, activations.columns));
		
		for (int i = 0; i < theta.rows; i++)
		{
			double sum = theta.data[thetaindex++];
			
			for (int j = 0; j < activations.rows; j++)
			{
				sum += theta.data[thetaindex++] * activations.data[j];
			}
			
			answer[i] = 1.0 / (1 + Math.exp(-sum));
		}

		return new Matrix(answer, theta.rows, 1);
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
		int thetaindex;
		
		if (theta.rows != delta.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				theta.rows, theta.columns, delta.rows, delta.columns));
		
		for (int i = 1; i < theta.columns; i++)
		{
			double sum = 0;
			thetaindex = i;
			
			for (int j = 0; j < delta.rows; j++)
			{
				sum += theta.data[thetaindex] * delta.data[j];
				thetaindex += theta.columns;
			}
			
			double d = activations.data[i - 1];
			answer[i - 1] = sum * d * (1 - d);
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
		int answerindex = 0;
		
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
			weights.data[answerindex++] += delta.data[i] * learningRate;
			
			for (int j = 0; j < activations.rows; j++)
			{
				weights.data[answerindex++] += delta.data[i] * activations.data[j] * learningRate;
			}
		}
		
		return weights;
	}
	
	
	/**
	 * Randomly initialises an array.
	 * @param rows
	 * @param columns
	 * @return
	 */
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
