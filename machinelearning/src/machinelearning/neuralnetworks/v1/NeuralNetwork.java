package machinelearning.neuralnetworks.v1;

import java.util.Arrays;



/**
 * Represents a neural network, which can learn a function from input.
 */
public class NeuralNetwork
{
	private BasicNeuronLayer[] layers;
	private double[][][] delta;
	
	public NeuralNetwork(int[] layerSizes)
	{
		layers = new BasicNeuronLayer[layerSizes.length - 1];

		for (int i = 0; i < layers.length; i++)
		{
			layers[i] = new BasicNeuronLayer(layerSizes[i + 1], layerSizes[i]);
		}
		
		delta = new double[layers.length][][];
		
		for (int l = 0; l < layers.length; l++)
		{
			delta[l] = new double[layers[l].weights.length][layers[l].weights[0].length];
		}
	}
	
	
	public NeuralNetwork(double[][][] weights)
	{
		layers = new BasicNeuronLayer[weights.length];
		
		for (int i = 0; i < weights.length; i++)
		{
			layers[i] = new BasicNeuronLayer(weights[i]);
		}
		
		delta = new double[layers.length][][];
		
		for (int l = 0; l < layers.length; l++)
		{
			delta[l] = new double[layers[l].weights.length][layers[l].weights[0].length];
		}
	}


	public double[] predict(double[] input)
	{
		forwardPropagation(input);

		return layers[layers.length - 1].getOutput();
	}


	public void train(double[][] data, double[][] targets, int iterations, double learningRate, double lambda, ProgressCallback progressCallback)
	{
		for (int i = 0; i < iterations; i++)
		{
			for (int j = 0; j < data.length; j++)
			{
				forwardPropagation(data[j]);
				backPropagation(targets[j], delta);
			}
			
			if (progressCallback != null)
			{
				int percent = (int)Math.round((double)i / iterations * 100);
				
				if ((percent % 5) == 0)
				{
					progressCallback.updateProgress(percent);
				
//					double sum = 0;
//					
//					for (int j = 0; j < delta.length; j++)
//					{
//						for (int k = 0; k < delta[j].length; k++)
//						{
//							for (int l = 0; l < delta[j][k].length; l++)
//							{
//								sum += delta[j][k][l];
//							}
//						}
//					}
//					
//					System.out.println(sum);
					
					//System.out.println(Arrays.toString(layers[layers.length - 1].errors));
				}
			}
			
			updateWeights(learningRate, lambda, delta, data.length);
		}
	}


	private void forwardPropagation(double[] input)
	{
		layers[0].update(input);

		for (int i = 1; i < layers.length; i++)
		{
			layers[i].update(layers[i - 1].getOutput());
		}
	}


	private void backPropagation(double[] target, double[][][] delta)
	{
		NeuronLayer successorLayer;
		layers[layers.length - 1].calculateError(target);

		for (int i = layers.length - 2; i >= 0; i--)
		{
			successorLayer = layers[i + 1];
			layers[i].calculateError(successorLayer);
			layers[i].calculateDelta(delta[i]);
		}
	}


	private void updateWeights(double learningRate, double momentum, double[][][] delta, int m)
	{
		for (int i = 0; i < layers.length; i++)
		{
			layers[i].updateWeights(learningRate, momentum, delta[i], m);
		}
	}
}
