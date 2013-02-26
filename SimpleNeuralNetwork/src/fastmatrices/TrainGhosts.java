package fastmatrices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TrainGhosts
{
	public static void main(String[] args)
	{
		train("PINKY");
		train("INKY");
		train("BLINKY");
		train("SUE");
	}
	
	private static void train(String ghost)
	{
		Matrix data = Utilities.csvread("../agent/logs/" + ghost + ".txt");
		Matrix x = data.part(1, -1, 1, -5);
		Matrix y = data.part(1, -1, -4, -1);
		
		long time = System.currentTimeMillis();
		
		NeuralNetwork net = new NeuralNetwork(x.columns, 10, 4);
		net.train(x, y, 0.2, 1000);
		
		System.out.println("time: " + (System.currentTimeMillis() - time));
		
		Matrix predicted = new Matrix(net.bulkPredict(x));
		double err = Utilities.confusion(y, predicted);
		System.out.println(err);
		
		try
		{
			ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("../agent/weights/" + ghost + ".dat"));
			writer.writeObject(net.theta1.data);
			writer.writeObject(net.theta2.data);
			writer.close();
			
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
