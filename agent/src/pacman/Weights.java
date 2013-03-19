package pacman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Weights
{
	public static void main(String[] args)
	{
		loadGhost("weights/Training-BLINKY.dat", "Blinky");
		loadGhost("weights/Training-PINKY.dat", "Pinky");
		loadGhost("weights/Training-INKY.dat", "Inky");
		loadGhost("weights/Training-SUE.dat", "Sue");
	}
	
	
	public static void loadGhost(String file, String name)
	{
		try
		{
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
			
			double[] theta1 = (double[])reader.readObject();
			printArray(name + "Theta1", theta1);
			
			double[] theta2 = (double[])reader.readObject();
			printArray(name + "Theta2", theta2);
			
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void printArray(String name, double[] array)
	{
		System.out.println("\tpublic static double[] get" + name + "()\n{\n");
		System.out.println("\t\treturn new double[] { ");
		
		for (int i = 0; i < array.length; i++)
		{
			System.out.printf("%.4f", array[i]);
			
			if (i < array.length - 1)
				System.out.print(", ");
		}
		
		System.out.println("\n};\n}\n\n");
	}
}
