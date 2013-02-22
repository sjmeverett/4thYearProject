package nonmatrix;
import matrices.Matrix;
import matrices.Utilities;


public class TestNonMatrix
{
	public static void main(String[] args)
	{
		TestNonMatrix test = new TestNonMatrix();
		test.run();
	}
	
	
	public void run()
	{
		NeuralNetwork network = new NeuralNetwork(14, 10, 4, 1);
		
		Matrix data = Utilities.csvread("../agent/logs/BLINKY.txt");
		Matrix x = data.part(1, -1, 1, -5);
		Matrix y = data.part(1, -1, -4, -1);
		
		double[][] examples = x.data;
		double[][] targets = y.data;
		
		long time = System.currentTimeMillis();
		
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
		
		System.out.println("time: " + (System.currentTimeMillis() - time));
		
		Matrix predicted = new Matrix(network.bulkPredict(examples));
		double err = Utilities.confusion(y, predicted);
		System.out.println(err);
		
	}
}
