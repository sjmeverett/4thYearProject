package pacman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Weights
{
	public static void main(String[] args)
	{
		
	}
	
	
	public static void loadGhost(String file, String name)
	{
		try
		{
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
			double[] theta1 = (double[])reader.readObject();
			double[] theta2 = (double[])reader.readObject();
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			
		}
	}
}
