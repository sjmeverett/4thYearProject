package nonmatrix;
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
	private static final double EPSILON = 1;
	protected double[] hiddenWeights, outputWeights;
	private int inputLayerSize, hiddenLayerSize, outputLayerSize;
	private double learningRate;
	
	
	/**
	 * Creates a new neural network of the specified size with the specified
	 * pre-trained weights.
	 * @param inputLayerSize
	 * @param hiddenLayerSize
	 * @param outputLayerSize
	 * @param learningRate
	 * @param hiddenWeights
	 * @param outputWeights
	 */
	public NeuralNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize,
		double learningRate, double[] hiddenWeights, double[] outputWeights)
	{
		if (hiddenWeights.length != hiddenLayerSize * (inputLayerSize + 1))
			throw new IllegalArgumentException("hiddenWeights is the wrong length");
		
		if (outputWeights.length != outputLayerSize * (hiddenLayerSize + 1))
			throw new IllegalArgumentException("outputWeights is the wrong length");
		
		this.inputLayerSize = inputLayerSize;
		this.hiddenLayerSize = hiddenLayerSize;
		this.outputLayerSize = outputLayerSize;
		this.hiddenWeights = hiddenWeights;
		this.outputWeights = outputWeights;
		this.learningRate = learningRate;
	}
	
	
	/**
	 * Creates a new neural network of the specified size with randomly
	 * initialised weights.
	 * @param inputLayerSize
	 * @param hiddenLayerSize
	 * @param outputLayerSize
	 * @param learningRate
	 */
	public NeuralNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize,
		double learningRate)
	{
		this(inputLayerSize, hiddenLayerSize, outputLayerSize, learningRate,
			randomArray(hiddenLayerSize * (inputLayerSize + 1)),
			randomArray(outputLayerSize * (hiddenLayerSize + 1)));
	}
	
	
	/**
	 * Calculate the output of the network for a given input using the weights
	 * currently stored in the network.
	 * @param input The input to feed into the network.
	 * @return An array containing the output values.
	 */
	public double[] predict(double[] input)
	{
		//make sure the input is the correct length
		if (input.length != inputLayerSize)
			throw new IllegalArgumentException(String.format(
				"Input array size (%d) does not match input layer size (%d).",
				input.length, inputLayerSize));
		
		//simple forward propagation algorithm
		double[] hiddenActivations = computeLayer(input, hiddenWeights, hiddenLayerSize);
		return computeLayer(hiddenActivations, outputWeights, outputLayerSize);
	}
	
	
	public double[][] bulkPredict(double[][] input)
	{
		double[][] answer = new double[input.length][];
		
		for (int i = 0; i < input.length; i++)
		{
			answer[i] = predict(input[i]);
		}
		
		return answer;
	}
	
	
	/**
	 * Trains the neural network with the specified example.
	 * @param input The input to train on.
	 * @param targets The expected output for the given input.
	 * @return The cost for this example.
	 */
	public double train(double[] input, double[] targets)
	{
		//make sure the input is the correct length
		if (input.length != inputLayerSize)
			throw new IllegalArgumentException(String.format(
				"Input array size (%d) does not match input layer size (%d).",
				input.length, inputLayerSize));
		
		//make sure the target array is the correct length
		if (targets.length != outputLayerSize)
			throw new IllegalArgumentException(String.format(
				"Targets array size (%d) does not match output layer size (%d).",
				targets.length, outputLayerSize));
		
		//forward propagation
		double[] hiddenActivations, outputActivations;
		hiddenActivations = computeLayer(input, hiddenWeights, hiddenLayerSize);
		outputActivations = computeLayer(hiddenActivations, outputWeights, outputLayerSize);
		
		//backpropagation
		double[] outputErrors, hiddenErrors;
		outputErrors = computeOutputErrors(outputActivations, targets);
		hiddenErrors = computeHiddenErrors(outputErrors, hiddenActivations);
		
		adjustWeights(hiddenWeights, hiddenErrors, input, hiddenLayerSize);
		adjustWeights(outputWeights, outputErrors, hiddenActivations, outputLayerSize);
		
		//calculate cost
		double cost = 0;
		
		for (int i = 0; i < outputErrors.length; i++)
		{
			double e = targets[i] - outputActivations[i];
			cost += e * e;
		}
		
		return cost;
	}
	
	
	/**
	 * Calculates the output of a layer in the neural network.
	 * @param input The input to the layer.
	 * @param weights The weights which modify the input to the layer. This
	 * is an array containing the weights for each successive input connected
	 * to the first node in the layer, followed by those for the second, and
	 * so on.
	 * @param layerSize The number of neurons in the layer.
	 * @return An array of size layerSize containing the values for each
	 * neuron in the layer.
	 */
	private double[] computeLayer(double[] input, double[] weights, int layerSize)
	{	
		double[] output = new double[layerSize];
		
		//use a pointer which we simply indicate as we go: faster than
		//multiplication such as weights[i * layerSize + j] every time
		int index = 0;
		
		//for each unit in the layer
		for (int i = 0; i < layerSize; i++)
		{
			//include the bias input unit, which is defined as always
			//having the value of 1
			double sum = weights[index++];
			
			//for each input to this unit
			for (int j = 0; j < input.length; j++)
			{
				sum += weights[index++] * input[j];
			}
			
			output[i] = activationFunction(sum);
		}
		
		return output;
	}
	
	
	/**
	 * Calculates the error values for the output layer.
	 * @param activations The computed predictions for the output layer.
	 * @param targets The desired values for the output layer.
	 * @return
	 */
	private double[] computeOutputErrors(double[] activations, double[] targets)
	{
		double[] errors = new double[outputLayerSize];
		
		//for each unit in the output layer
		for (int i = 0; i < outputLayerSize; i++)
		{
			errors[i] = (targets[i] - activations[i]) * activationFunctionGradient(activations[i]);
		}
		
		return errors;
	}
	
	
	/**
	 * Compute the error values for each unit in the hidden layer.
	 * @param outputErrors The error values for each unit in the output layer.
	 * @param activations The activations of each unit in the hidden layer.
	 * @return
	 */
	private double[] computeHiddenErrors(double[] outputErrors, double[] activations)
	{
		double[] errors = new double[hiddenLayerSize];
		
		//use a pointer, rather than multiplication, since it adding is faster
		//this is also much faster than a two dimensional array implementation,
		//which messes up processor caching since we are traversing column-wise
		int index;
		
		//for each unit in the hidden layer
		for (int i = 1; i < hiddenLayerSize; i++)
		{
			double sum = 0;
			
			//column-wise traversal
			index = i;
			
			//for each unit in the output layer that this unit is connected to
			for (int j = 0; j < outputLayerSize; j++)
			{
				sum += outputErrors[j] * outputWeights[j * (hiddenLayerSize + 1) + i];
				
				//move down a row
				index += hiddenLayerSize + 1;
			}
			
			errors[i - 1] = sum * activationFunctionGradient(activations[i]);
		}
		
		return errors;
	}
	
	
	/**
	 * Adjust the weights array based on the error values.
	 * @param weights The current weights.
	 * @param errors The error values for this layer.
	 * @param activations The activation values for the input layer to this layer.
	 * @param layerSize The size of this layer.
	 */
	private void adjustWeights(double[] weights, double[] errors, double[] activations, int layerSize)
	{
		int index = 0;
		
		//for each unit in the layer
		for (int i = 0; i < layerSize; i++)
		{
			//bias unit
			weights[index++] += learningRate * errors[i];
			
			//for each input to this unit
			for (int j = 0; j < activations.length; j++)
			{
				double d = learningRate * errors[i] * activations[j];
				weights[index++] += d;
			}
		}
	}
	
	
	private double activationFunction(double a)
	{
		return 1.0 / (1.0 + Math.exp(-a));
	}
	
	
	private double activationFunctionGradient(double a)
	{
		return a * (1 - a);
	}
	
	
	/**
	 * Randomly initialises an array of the specified size with values between
	 * -EPSILON and +EPSILON.
	 * @param size The size of the array to create.
	 * @return
	 */
	private static double[] randomArray(int size)
	{
		double[] a = new double[size];
		Random random = new Random();
		
		for (int i = 0; i < size; i++)
		{
			a[i] = random.nextDouble() * EPSILON * 2 - EPSILON;
		}
		
		return a;
	}
}
