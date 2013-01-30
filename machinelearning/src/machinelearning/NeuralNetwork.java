package machinelearning;

import java.util.Random;

/**
 * Represents a neural network, which can learn a function from input.
 */
public class NeuralNetwork
{
	private double[][][] weights;
	private double learningRate, lambda;
	private static double EPSILON = 0.2;
	
	/**
	 * Constructor for a pre-trained (or semi-trained) network.
	 * @param initialWeights The weights to use in the network.
	 * @param learningRate A parameter controlling how quickly to learn.
	 * @param lambda A parameter controlling regularisation.
	 */
	public NeuralNetwork(double[][][] initialWeights, double learningRate, double lambda)
	{
		this.weights = initialWeights;
		this.learningRate = learningRate;
		this.lambda = lambda;
	}
	
	
	/**
	 * Constructor for a network to be trained.
	 * @param layers An array of integers stating the number of nodes in each layer.
	 * @param learningRate A parameter controlling how quickly to learn.
	 * @param lambda A parameter controlling regularisation.
	 */
	public NeuralNetwork(int[] layers, double learningRate, double lambda)
	{
		this(randomWeights(layers), learningRate, lambda);
	}
	
	
	/**
	 * Predict the output for the given input, using the current weights in the network.
	 * @param input The input to predict output for.
	 * @return The predicted output.
	 */
	public double[] predict(double[] input)
	{
		double[] output = input;
		
		for (int layer = 1; layer <= weights.length; layer++)
		{
			output = calculateLayer(output, weights[layer - 1]);
		}
		
		return output;
	}
	
	
	/**
	 * Trains the network on the given input.
	 * @param input An array with a row for each training example and a column for each input feature.
	 * @param output An array with a row for each training example and a column for each output.
	 * @param iterations The number of iterations to run the training for.
	 */
	public void train(double[][] input, double[][] output, int iterations)
	{
		double[][] nodeValues;
		double[][][] gradient;
		
		for (int i = 0; i < iterations; i++)
		{
			for (int example = 0; example < input.length; example++)
			{
				nodeValues = forwardPropagation(input[example]);
				
				gradient = initialiseGradient();
				backPropagation(gradient, nodeValues, output[example]);
				updateWeights(gradient, input.length);
			}
		}
	}
	
	
	/**
	 * Returns an array filled with zero of the same dimensions as the weights array.
	 * @return
	 */
	private double[][][] initialiseGradient()
	{
		double[][][] gradient = new double[weights.length][][];
		
		for (int layer = 0; layer < weights.length; layer++)
		{
			gradient[layer] = new double[weights[layer].length][weights[layer][0].length];
		}
		
		return gradient;
	}
	
	
	/**
	 * Generate a random weights array filled with random numbers around zero.
	 * @param layers An array specifying the size of each layer.
	 * @return
	 */
	private static double[][][] randomWeights(int[] layers)
	{
		double[][][] weights = new double[layers.length - 1][][];
		Random random = new Random();
		
		for (int layer = 0; layer < layers.length - 1; layer++)
		{
			weights[layer] = new double[layers[layer + 1]][layers[layer] + 1];
			
			for (int i = 0; i < layers[layer + 1]; i++)
			{
				for (int j = 0; j < layers[layer] + 1; j++)
				{
					weights[layer][i][j] = random.nextDouble() * 2 * EPSILON - EPSILON;
				}
			}
		}
		
		return weights;
	}
	
	
	/**
	 * Updates the weights of the network using the gradient descent algorithm.
	 * @param gradient A matrix with the sume of the gradient values for all weight values,
	 * over the input set.
	 * @param numberOfExamples The number of training examples used.
	 */
	private void updateWeights(double[][][] gradient, int numberOfExamples)
	{
		for (int layer = 0; layer < weights.length; layer++)
		{
			for (int j = 0; j < weights[layer].length; j++)
			{
				weights[layer][j][0] -= learningRate * gradient[layer][j][0] / numberOfExamples;
				
				for (int k = 1; k < weights[layer][j].length; k++)
				{
					weights[layer][j][k] -= learningRate * (gradient[layer][j][k] / numberOfExamples
						+ lambda / numberOfExamples * weights[layer][j][k]);
				}
			}
		}
	}
	
	
	/**
	 * Run the forward propagation algorithm.
	 * @param input The input to propagate to the output layer.
	 * @return An array containing the values of all the nodes in the network.
	 */
	private double[][] forwardPropagation(double[] input)
	{
		double[][] nodeValues;
		nodeValues = new double[weights.length + 1][];
		nodeValues[0] = input;
		
		for (int layer = 1; layer <= weights.length; layer++)
		{
			nodeValues[layer] = calculateLayer(nodeValues[layer - 1], weights[layer - 1]);
		}
		
		return nodeValues;
	}
	
	
	/**
	 * Calculates a layer in the neural network from the weights for that layer and the values for
	 * the previous layer.
	 * @param previousLayer The previous layer in the network.
	 * @param weights The weights for this layer.
	 * @return An array containing the values for the nodes in this layer.
	 */
	private double[] calculateLayer(double[] previousLayer, double[][] weights)
	{
		double[] layer = new double[weights.length];
		double sum;
		
		for (int i = 0; i < weights.length; i++)
		{
			sum = weights[i][0];
			
			for (int j = 0; j < previousLayer.length; j++)
			{
				sum += weights[i][j + 1] * previousLayer[j];
			}
			
			layer[i] = activation(sum);
		}
		
		return layer;
	}
	
	
	/**
	 * Runs the backpropagation algorithm and adds the gradients for the current training example
	 * to the gradient array.
	 * @param gradient The gradient accumulator.
	 * @param nodeValues An array containing the values of all the nodes in the network for the 
	 * current training example.
	 * @param output The expected output for the current training example.
	 */
	private void backPropagation(double[][][] gradient, double[][] nodeValues, double[] output)
	{
		//output layer
		double outputLayer[] = nodeValues[nodeValues.length - 1];
		double delta[] = new double[output.length];
		
		for (int i = 0; i < output.length; i++)
		{
			delta[i] = outputLayer[i] - output[i];
		}

		//other layers
		for (int i = weights.length - 2; i > 0; i--)
		{
			calculateLayerGradient(gradient[i], delta, nodeValues[i]);
			delta = calculateLayerDelta(weights[i], delta, nodeValues[i]);
		}
		
		calculateLayerGradient(gradient[0], delta, nodeValues[0]);
	}
	
	
	/**
	 * Calculates the delta values (how far off the expected value each node is) for the specified layer.
	 * @param weights The weights for this layer.
	 * @param successorDelta The delta values for the layer after this layer.
	 * @param layer The values for the nodes in this layer.
	 * @return An array containing the delta values, one for each node in the layer.
	 */
	private double[] calculateLayerDelta(double[][] weights, double[] successorDelta, double[] layer)
	{
		double[] layerDelta = new double[layer.length];
		
		for (int i = 0; i < successorDelta.length; i++)
		{
			for (int j = 0; j < layer.length; j++)
			{
				layerDelta[i] += weights[j + 1][i] * successorDelta[j];
			}
			
			layerDelta[i] *= activationDerivative(layer[i]);
		}
		
		return layerDelta;
	}
	
	
	/**
	 * Calculates the gradient for the weights array for the specified layer.
	 * @param currentLayerGradient The current totals for the gradients of the weights for this layer.
	 * @param delta The delta values for the layer after this one.
	 * @param layer The values for the nodes in this layer.
	 */
	private void calculateLayerGradient(double[][] layerGradient, double[] delta, double[] layer)
	{
		for (int i = 0; i < delta.length; i++)
		{
			layerGradient[i][0] += delta[i];
			
			for (int j = 1; j < layer.length; j++)
			{
				layerGradient[i][j] += delta[i] * layer[j - 1];
			}
		}
	}
	
	
	/**
	 * The activation function.
	 * @param z
	 * @return
	 */
	private double activation(double z)
    {
        return 1.0 / (1 + Math.exp(-z));
    }
	
	
	/**
	 * The derivative of the activation function.
	 * @param a
	 * @return
	 */
	private double activationDerivative(double a)
	{
		return a * (1 - a);
	}
}
