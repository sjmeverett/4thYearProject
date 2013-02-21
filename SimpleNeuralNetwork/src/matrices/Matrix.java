package matrices;

/**
 * Simple dense matrix class.
 */
public class Matrix
{
	public double[][] data;
	public int rows, columns;
	
	/**
	 * Creates a new matrix based on the specified data.
	 * @param data
	 */
	public Matrix(double[][] data)
	{
		this.data = data;
		this.rows = data.length;
		this.columns = data[0].length;
	}
	
	
	/**
	 * Creates a new matrix of the specified size, initialised with zeroes.
	 * @param rows
	 * @param columns
	 */
	public Matrix(int rows, int columns)
	{
		this.data = new double[rows][columns];
		this.rows = rows;
		this.columns = columns;
	}
	
	
	/**
	 * Returns the result of transposing the matrix, i.e., reflecting along
	 * the diagonal.
	 * @return
	 */
	public Matrix transpose()
	{
		double[][] transpose = new double[columns][rows];
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				transpose[j][i] = data[i][j];
			}
		}
		
		return new Matrix(transpose);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix with the specified matrix.
	 * @param op2
	 * @return
	 */
	public Matrix multiply(Matrix op2)
	{
		Matrix op1 = this;
		double[][] answer = new double[op1.rows][op2.columns];
		
		if (op1.columns != op2.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op2.columns; j++)
			{
				double sum = 0;
				
				for (int k = 0; k < op2.rows; k++)
				{
					sum += op1.data[i][k] * op2.data[k][j];
				}
				
				answer[i][j] = sum;
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Returns the result of applying every element in this matrix with the
	 * corresponding element in op2.
	 * @param op2
	 * @return
	 */
	public Matrix elementMultiply(Matrix op2)
	{
		Matrix op1 = this;
		double[][] answer = new double[op1.rows][op1.columns];
		
		if (op1.rows != op2.rows || op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.columns; j++)
			{
				answer[i][j] = op1.data[i][j] * op2.data[i][j];
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Returns the result of multiplying every element in this matrix by the
	 * specified scalar value.
	 * @param op2
	 * @return
	 */
	public Matrix scale(double scale)
	{
		double[][] answer = new double[rows][columns];
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				answer[i][j] = data[i][j] * scale;
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Returns the result of adding the specified matrix to this matrix.
	 * @param op2
	 * @return
	 */
	public Matrix add(Matrix op2)
	{
		Matrix op1 = this;
		double[][] answer = new double[op1.rows][op1.columns];
		
		if (op1.rows != op2.rows || op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.columns; j++)
			{
				answer[i][j] = op1.data[i][j] + op2.data[i][j];
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Returns the result of subtracting the specified matrix from this matrix.
	 * @param op2
	 * @return
	 */
	public Matrix subtract(Matrix op2)
	{
		Matrix op1 = this;
		double[][] answer = new double[op1.rows][op1.columns];
		
		if (op1.rows != op2.rows || op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.columns; j++)
			{
				answer[i][j] = op1.data[i][j] - op2.data[i][j];
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Returns a matrix containing the results of applying the specified
	 * function to all the elements of this matrix.
	 * @param f
	 * @return
	 */
	public Matrix apply(Function f)
	{
		double[][] answer = new double[rows][columns];
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				answer[i][j] = f.apply(data[i][j]);
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Returns the portion of this matrix specified.  The indices start from 1.
	 * Negative indices can be used: the indice -n would specify the (n + 1)th
	 * index to last.  For example, -1 is the last index, and -2 is the 2nd last.
	 * @param startrow The row to start from.
	 * @param endrow The last row to include.
	 * @param startcolumn The column to start from.
	 * @param endcolumn The last column to include.
	 * @return
	 */
	public Matrix part(int startrow, int endrow, int startcolumn, int endcolumn)
	{
		if (startrow < 0)
			startrow += rows;
		else
			startrow--;
				
		if (endrow < 0)
			endrow += rows;
		else
			endrow--;
		
		if (startcolumn < 0)
			startcolumn += columns;
		else
			startcolumn--;
		
		if (endcolumn < 0)
			endcolumn += columns;
		else
			endcolumn--;
		
		double[][] answer = new double[endrow - startrow + 1][endcolumn - startcolumn + 1];
		
		for (int i = 0; i < answer.length; i++)
		{
			for (int j = 0; j < answer[0].length; j++)
			{
				answer[i][j] = data[startrow + i][startcolumn + j];
			}
		}
		
		return new Matrix(answer);
	}
	
	
	/**
	 * Represents a function which can be applied to the elements of the matrix.
	 */
	public interface Function
	{
		double apply(double value);
	}
}
