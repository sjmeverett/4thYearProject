package machinelearning.tests.neuralnetworks.v2;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import machinelearning.GradientDescent;
import machinelearning.MinimisationAlgorithm;
import machinelearning.neuralnetworks.v2.ThreeLayerNeuralNetwork;
import machinelearning.neuralnetworks.v2.ThreeLayerNeuralNetworkCostFunction;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

public class CharacterRecognitionTest
{
	private double[][] X, y, targets;
	private double[] theta;
	
	public static void main(String[] args)
	{
		CharacterRecognitionTest test = new CharacterRecognitionTest();
		//test.nonTrainRun();
		test.run();
	}
	
	
	public void nonTrainRun()
	{
		loadData();
		loadWeights();
		ThreeLayerNeuralNetwork network = new ThreeLayerNeuralNetwork(400, 25, 10);
		network.setWeights(theta);
		predict(network);
	}
	
	public void run()
	{
		loadData();
		
		ThreeLayerNeuralNetwork network = new ThreeLayerNeuralNetwork(400, 25, 10);
		MinimisationAlgorithm min = new GradientDescent(1000, 1); 
		network.train(X, y, 1, min);
		
		predict(network);
	}

	
	private void loadData()
	{
		try
		{
			MatFileReader reader = new MatFileReader("ex4data1.mat");
			Map<String, MLArray> data = reader.getContent();
			
			X = ((MLDouble)data.get("X")).getArray();
			y = ((MLDouble)data.get("y")).getArray();
			
			double[][] id = new double[10][10];
			
			for (int i = 0; i < 10; i++)
			{
				id[i][i] = 1;
			}
			
			targets = new double[y.length][];
			
			for (int i = 0; i < y.length; i++)
			{
				targets[i] = id[(int)y[i][0] - 1].clone();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void loadWeights()
	{
		try
		{
			MatFileReader reader = new MatFileReader("ex4weights.mat");
			Map<String, MLArray> data = reader.getContent();
			
			double[][] theta1 = ((MLDouble)data.get("Theta1")).getArray();
			double[][] theta2 = ((MLDouble)data.get("Theta2")).getArray();
			
			int offset = 0;
			theta = new double[theta1.length * theta1[0].length + theta2.length * theta2[0].length];
			
			for (int i = 0; i < theta1.length; i++)
			{
				System.arraycopy(theta1[i], 0, theta, offset, theta1[0].length);
				offset += theta1[0].length;
			}
	
			for (int i = 0; i < theta2.length; i++)
			{
				System.arraycopy(theta2[i], 0, theta, offset, theta2[0].length);
				offset += theta2[0].length;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void predict(ThreeLayerNeuralNetwork network)
	{
		int correct = 0;
		Random random = new Random();
		
		for (int i = 0; i < 100; i++)
		{
			int index = random.nextInt(X.length);
			double[] prediction = network.predict(X[index], theta);
			int c = maxScore(prediction) + 1;
			
			System.out.println(String.format("Prediction: %d " + arrayToString(prediction) + ", Actual: %d", c, (int)y[index][0]));
			
			if (c == (int)y[index][0])
				correct++;
		}
		
		System.out.println("Percent correct: " + correct);
	}
	
	
	private int maxScore(double[] y)
	{
		int index = 0;
		double max = 0;
		
		for (int i = 0; i < y.length; i++)
		{
			if (y[i] > max)
			{
				max = y[i];
				index = i;
			}
		}
		
		return index;
	}
	
	
	private String arrayToString(double[] array)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		
		for (double d: array)
		{
			builder.append(String.format("%.2f", d) + " ");
		}
		
		builder.append("]");
		
		return builder.toString();
	}
}
