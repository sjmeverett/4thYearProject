package machinelearning.neuralnetworks.v2;

import machinelearning.MinimisableFunction;
import machinelearning.neuralnetworks.v2.elementfunctions.CostFunction;
import machinelearning.neuralnetworks.v2.elementfunctions.GradientRegularisationFunction;
import machinelearning.neuralnetworks.v2.elementfunctions.SigmoidFunction;
import machinelearning.neuralnetworks.v2.elementfunctions.SigmoidGradientFunction;
import machinelearning.neuralnetworks.v2.elementfunctions.SquareFunction;

public class ThreeLayerNeuralNetworkCostFunction implements MinimisableFunction
{
	private int inputLayerSize, hiddenLayerSize, outputLayerSize;
	private Matrix X;
	private Matrix y;
	private double lambda;
	private Matrix.ElementFunction sigmoidFunction, squareFunction, costFunction;
	
	
	public ThreeLayerNeuralNetworkCostFunction(int hiddenNodes, double[][] X, double[][] y, double lambda)
	{
		this.inputLayerSize = X[0].length;
		this.hiddenLayerSize = hiddenNodes;
		this.outputLayerSize = y[0].length;
		
		this.X = new Matrix(X, true);
		this.y = new Matrix(y, false);
		this.lambda = lambda;
		
		//instance some reusable functions
		sigmoidFunction = new SigmoidFunction();
		squareFunction = new SquareFunction();
		costFunction = new CostFunction(y);
	}
	
	
	@Override
	public double getCost(double[] theta, double[] gradient)
	{
		//reshape the theta values into a theta matrix for each layer
		Matrix theta1 = new Matrix(0, hiddenLayerSize, inputLayerSize + 1, theta, false);
		Matrix theta2 = new Matrix(hiddenLayerSize * (inputLayerSize + 1), outputLayerSize, hiddenLayerSize + 1, theta, false);
		
		//we also need the theta matrices without the bias units
		Matrix theta1part = theta1.part(0, theta1.rows - 1, 1, theta1.cols - 1);
		Matrix theta2part = theta2.part(0, theta2.rows - 1, 1, theta2.cols - 1);
		
		//setup the gradient matrices so that they store values in the gradient array
		Matrix theta1Gradient = new Matrix(0, hiddenLayerSize, inputLayerSize + 1, gradient, false);
		Matrix theta2Gradient = new Matrix(hiddenLayerSize * (inputLayerSize + 1), outputLayerSize, hiddenLayerSize + 1, gradient, false);

		//input layer
		Matrix a1 = X;
		
		//hidden layer
		Matrix a2 = a1.multiplyTransposeOp2(theta1, sigmoidFunction);
		a2 = new Matrix(0, a2.rows, a2.cols, a2.data, true);
		
		//output layer
		Matrix a3 = a2.multiplyTransposeOp2(theta2, sigmoidFunction);
		
		//backpropagation
		//output layer error
		Matrix d3 = a3.subtract(y);
		
		//hidden layer error
		Matrix.ElementFunction sigmoidGradientFunction = new SigmoidGradientFunction(a2);
		Matrix d2 = d3.multiply(theta2part, sigmoidGradientFunction);
		
		//theta1 gradient with regularisation
		Matrix.ElementFunction theta1Reg = new GradientRegularisationFunction(X.rows, lambda, theta1);
		d2.multiplyTransposeOp1(a1, theta1Reg, theta1Gradient);
		
		//theta2 gradient with regularisation
		Matrix.ElementFunction theta2Reg = new GradientRegularisationFunction(X.rows, lambda, theta2);
		d3.multiplyTransposeOp1(a2, theta2Reg, theta2Gradient);
		
		//regularisation term
		double Jreg = lambda / (2 * X.rows) * 
			( theta1part.sum(squareFunction)
			+ theta2part.sum(squareFunction) );
		
		return (1 / X.rows) * a3.sum(costFunction) + Jreg;
	}	
}
