package fastmatrices;

import java.util.Arrays;

public class TestFast
{
	public static void main(String[] args)
	{
		Matrix data = Utilities.csvread("../agent/logs/BLINKY.txt");
		Matrix x = data.part(1, -1, 1, -5);
		Matrix y = data.part(1, -1, -4, -1);
		
		NeuralNetwork net = new NeuralNetwork(x.columns, 10, 4);
		net.train(x, y, 1, 1000);
		
		Matrix predicted = new Matrix(net.bulkPredict(x));
		double err = Utilities.confusion(y, predicted);
		System.out.println(err);
	}
}
