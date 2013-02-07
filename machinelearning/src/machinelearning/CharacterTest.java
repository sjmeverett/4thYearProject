package machinelearning;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

public class CharacterTest implements ProgressCallback
{
	private int lastPercent = -1;
	private double[][] X, y, targets;
	
	public static void main(String[] args)
	{
		CharacterTest test = new CharacterTest();
		//test.nonTrainRun();
		test.run();
	}
	
	public void nonTrainRun()
	{
		try
		{
			MatFileReader reader = new MatFileReader("ex4weights.mat");
			Map<String, MLArray> data = reader.getContent();
			
			NeuralNetwork network = new NeuralNetwork(new double[][][] {
				((MLDouble)data.get("Theta1")).getArray(),
				((MLDouble)data.get("Theta2")).getArray()
			});
			
			loadData();
			//network.train(X, targets, 100, 0.1, 1, this);
			predict(network);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void run()
	{
		loadData();
		
		NeuralNetwork network = new NeuralNetwork(new int[] { 400, 25, 10 });
		network.train(X, targets, 1000, 0.1, 1, this);
		
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
	
	
	private void predict(NeuralNetwork network)
	{
		int correct = 0;
		Random random = new Random();
		
		for (int i = 0; i < 100; i++)
		{
			int index = random.nextInt(X.length);
			double[] prediction = network.predict(X[index]);
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

	@Override
	public void updateProgress(int percent)
	{
		if (percent != lastPercent)
		{
			System.out.println(percent + "%");
			lastPercent = percent;
		}
	}
}
