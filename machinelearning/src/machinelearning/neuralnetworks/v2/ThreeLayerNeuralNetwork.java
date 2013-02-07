package machinelearning.neuralnetworks.v2;

import java.util.Random;

import machinelearning.MinimisationAlgorithm;
import machinelearning.neuralnetworks.v2.elementfunctions.SigmoidFunction;

public class ThreeLayerNeuralNetwork
{
	private double[] theta;
	private Matrix theta1, theta2;
	private Matrix.ElementFunction sigmoidFunction;
	private int hiddenNodes;
	
	public ThreeLayerNeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes)
	{
		this.hiddenNodes = hiddenNodes;
		
		//randomly initialise theta
		theta = new double[hiddenNodes * (inputNodes + 1) + outputNodes * (hiddenNodes + 1)];
		Random random = new Random();
		
		for (int i = 0; i < theta.length; i++)
		{
			theta[i] = random.nextDouble() * 0.4 - 0.2;
		}
		
		//set up the two theta matrices to point into the flat theta array
		theta1 = new Matrix(0, hiddenNodes, inputNodes + 1, theta, false);
		theta2 = new Matrix(hiddenNodes * (inputNodes + 1), outputNodes, hiddenNodes + 1, theta, false);
		
		//setup the sigmoid function
		sigmoidFunction = new SigmoidFunction();
	}
	
	
	public void train(double X[][], double y[][], double lambda, MinimisationAlgorithm min)
	{
		ThreeLayerNeuralNetworkCostFunction costFunction = new ThreeLayerNeuralNetworkCostFunction(hiddenNodes, X, y, lambda);
		min.minimise(theta, costFunction);
	}
	
	
	public double[] predict(double[] input, double[] theta)
	{
		//input layer
		Matrix a1 = new Matrix(0, 1, input.length, input, true);
		
		//hidden layer
		Matrix a2 = a1.multiplyTransposeOp2(theta1, sigmoidFunction);
		a2 = new Matrix(0, a2.rows, a2.cols, a2.data, true);
		
		//output layer
		Matrix a3 = a2.multiplyTransposeOp2(theta2, sigmoidFunction);
		
		return a3.data;
	}
	
	
	public void setWeights(double[] weights)
	{
		this.theta = weights;
	}
}
