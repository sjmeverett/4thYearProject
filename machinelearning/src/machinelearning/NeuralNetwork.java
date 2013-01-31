package machinelearning;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents a neural network, which can learn a function from input.
 */
public class NeuralNetwork
{
	private NeuronLayer[] layers;

	
	public NeuralNetwork(int[] layerSizes)
	{
		layers = new NeuronLayer[layerSizes.length - 1];

		for (int i = 0; i < layers.length; i++)
		{
			layers[i] = new NeuronLayer(layerSizes[i + 1], layerSizes[i]);
		}
	}


	public double[] predict(double[] input)
	{
		forwardPropagation(input);

		return layers[layers.length - 1].output;
	}


	public void train(double[][] data, double[][] targets, int iterations, double learningRate, double momentum)
	{
		for (int i = 0; i < iterations; i++)
		{
			for (int j = 0; j < data.length; j++)
			{
				forwardPropagation(data[j]);
				backPropagation(targets[j]);
				updateWeights(learningRate, momentum);
			}
		}
	}


	private void forwardPropagation(double[] input)
	{
		layers[0].update(input);

		for (int i = 1; i < layers.length; i++)
		{
			layers[i].update(layers[i - 1].output);
		}
	}


	private void backPropagation(double[] target)
	{
		NeuronLayer successorLayer;
		layers[layers.length - 1].calculateError(target);

		for (int i = layers.length - 2; i >= 0; i--)
		{
			successorLayer = layers[i + 1];
			layers[i].calculateError(successorLayer.errors, successorLayer.weights);
		}
	}


	private void updateWeights(double learningRate, double momentum)
	{
		for (int i = 0; i < layers.length; i++)
		{
			layers[i].updateWeights(learningRate, momentum);
		}
	}
}
