package nonmatrix;
import java.util.Arrays;

import matrices.Matrix;
import matrices.Utilities;


public class Test
{
	public static void main(String[] args)
	{
		Test test = new Test();
		test.run();
	}
	
	
	public void run()
	{
		NeuralNetwork network = new NeuralNetwork(14, 10, 4, 1);/*,
			new double[] {1.9323, -5.6666, -5.7611, 4.8180, -3.3599, -3.3473},
			new double[] {-2.8097, -6.9729, 6.4459});*/
		
		Matrix data = Utilities.csvread("../agent/logs/BLINKY.txt");
		Matrix x = data.part(1, -1, 1, -5);
		Matrix y = data.part(1, -1, -4, -1);
		
		double[][] examples = x.data;
		double[][] targets = y.data;
		
		for (int i = 0; i < 1000; i++)
		{
			double cost = 0;
			
			for (int j = 0; j < examples.length; j++)
			{
				cost = network.train(examples[j], targets[j]);
			}
			
			if (i % 200 == 0)
			{
				System.out.printf("Cost: %.4f\n", cost);
			}
		}
		
		Matrix predicted = new Matrix(network.bulkPredict(examples));
		double err = Utilities.confusion(y, predicted);
		System.out.println(err);
		
	}
}
