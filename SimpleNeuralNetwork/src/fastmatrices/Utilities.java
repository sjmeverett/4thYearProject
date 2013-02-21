package fastmatrices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class for useful matrix operations.
 */
public class Utilities
{
	/**
	 * Places the matrix below the double value.  Has the effect of adding a
	 * new initial row with the value d.  The matrix must only have one column.
	 * @param d
	 * @param m
	 * @return
	 */
	public static Matrix appendVertical(double d, Matrix m)
	{
		if (m.columns != 1)
			throw new IllegalArgumentException("matrix must have 1 column");
		
		double[] answer = new double[m.rows + 1];
		
		answer[0] = d;
		System.arraycopy(m.data, 0, answer, 1, m.rows);
		
		return new Matrix(answer, m.rows + 1, m.columns);
	}
	
	
	/**
	 * Reads the data in the specified CSV file into a matrix.
	 * @param path
	 * @return
	 */
	public static Matrix csvread(String path)
	{
		List<double[]> values = new ArrayList<double[]>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			
			while ((line = reader.readLine()) != null)
			{
				String[] s = line.split(",");
				double[] d = new double[s.length];
				
				for (int i = 0; i < s.length; i++)
				{
					d[i] = Double.parseDouble(s[i].trim());
				}
				
				values.add(d);
			}
			
			reader.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		double[][] answer = new double[values.size()][];
		
		for (int i = 0; i < values.size(); i++)
			answer[i] = values.get(i);
		
		return new Matrix(answer);
	}
	
	
	public static double confusion(Matrix target, Matrix predicted)
	{
		int targetIndex = 0, predictedIndex = 0, correct = 0;
		double max;
		int targetptr = 0, predictedptr = 0;
		
		for (int i = 0; i < target.rows; i++)
		{
			max = 0;
			
			for (int j = 0; j < target.columns; j++)
			{
				if (target.data[targetptr] == 1)
					targetIndex = j;
				
				if (predicted.data[predictedptr] > max)
				{
					max = predicted.data[predictedptr];
					predictedIndex = j;
				}
				
				targetptr++;
				predictedptr++;
			}
			
			if (targetIndex == predictedIndex)
				correct++;
		}
		
		return (double)(target.rows - correct) / target.rows;
	}
}
