package matrices;

import java.util.Arrays;

public class TestMatrices
{
	public static void main(String[] args)
	{
		Matrix data = Utilities.csvread("../agent/logs/BLINKY.txt");
		Matrix x = data.part(1, -1, 1, -5);
		Matrix y = data.part(1, -1, -4, -1);
		
		long time = System.currentTimeMillis();
		
		NeuralNetwork net = new NeuralNetwork(x.columns, 10, 4);
		net.train(x, y, 1, 1000);

		System.out.println("time: " + (System.currentTimeMillis() - time));
		
		Matrix predicted = net.bulkPredict(x);
		double err = Utilities.confusion(y, predicted);
		System.out.println(err);
		
		nonmatrix.NeuralNetwork net2 = new nonmatrix.NeuralNetwork(14, 10, 4, 1.0, 
				flatten(net.theta1.data), flatten(net.theta2.data));
		
		System.out.println(Arrays.toString(net.predict(x.data[0])));
		System.out.println(Arrays.toString(net2.predict(x.data[0])));
		
	}
	
	
	private static double[] flatten(double[][] d)
	{
		int n = d[0].length;
		double[] answer = new double[d.length * n];
		
		for (int i = 0; i < d.length; i++)
		{
			System.arraycopy(d[i], 0, answer, i * n, n);
		}
		
		return answer;
	}
}
