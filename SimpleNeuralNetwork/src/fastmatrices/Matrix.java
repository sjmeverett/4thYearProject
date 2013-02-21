package fastmatrices;

/**
 * Simple dense matrix class.
 */
public class Matrix
{
	public double[] data;
	public int rows, columns;
	
	
	public Matrix(double[][] data)
	{
		this.rows = data.length;
		this.columns = data[0].length;
		this.data = new double[rows * columns];
		
		int index = 0;
		
		for (int i = 0; i < data.length; i++)
		{
			System.arraycopy(data[i], 0, this.data, index, columns);
			index += columns;
		}
	}
	
	
	/**
	 * Creates a new matrix based on the specified data.
	 * @param data
	 */
	public Matrix(double[] data, int rows, int columns)
	{
		this.data = data;
		this.rows = rows;
		this.columns = columns;
	}
	
	
	/**
	 * Creates a new matrix of the specified size, initialised with zeroes.
	 * @param rows
	 * @param columns
	 */
	public Matrix(int rows, int columns)
	{
		this.data = new double[rows * columns];
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
		//fast case
		if (columns == 1 || rows == 1)
		{
			return new Matrix(data.clone(), columns, rows);
		}
		
		//otherwise
		double[] transpose = new double[columns * rows];
		int index = 0, answerindex;
		
		for (int i = 0; i < columns; i++)
		{
			answerindex = i;
			
			for (int j = 0; j < rows; j++)
			{
				transpose[answerindex] = data[index++];
				answerindex += columns;
			}
		}
		
		return new Matrix(transpose, columns, rows);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix with the specified matrix.
	 * @param op2
	 * @param f The function to apply to each element, or null.
	 * @return
	 */
	public Matrix multiply(Matrix op2, Function f)
	{
		Matrix op1 = this;
		double[] answer = new double[op1.rows * op2.columns];
		int answerindex = 0, op1index = 0, op2index;
		
		if (op1.columns != op2.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op2.columns; j++)
			{
				double sum = 0;
				op2index = j;
				
				for (int k = 0; k < op2.rows; k++)
				{
					sum += op1.data[op1index + k] * op2.data[op2index];
					op2index += op2.columns;
				}
				
				if (f != null)
					sum = f.apply(sum, i, j);
				
				answer[answerindex++] = sum;
			}
			
			op1index += op1.columns;
		}
		
		return new Matrix(answer, op1.rows, op2.columns);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix with the specified matrix.
	 * @param op2
	 * @return
	 */
	public Matrix multiply(Matrix op2)
	{
		return multiply(op2, null);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix transposed with the specified matrix.
	 * @param op2
	 * @param f The function to apply to each element, or null.
	 * @return
	 */
	public Matrix multiplyTransposeOp1(Matrix op2, Function f)
	{
		Matrix op1 = this;
		double[] answer = new double[op1.columns * op2.columns];
		int answerindex = 0, op1index, op2index;
		
		if (op1.rows != op2.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.columns; i++)
		{
			for (int j = 0; j < op2.columns; j++)
			{
				double sum = 0;
				op1index = i;
				op2index = j;
				
				for (int k = 0; k < op2.rows; k++)
				{
					sum += op1.data[op1index] * op2.data[op2index];
					op1index += op1.columns;
					op2index += op2.columns;
				}
				
				if (f != null)
					sum = f.apply(sum, i, j);
				
				answer[answerindex++] = sum;
			}
		}
		
		return new Matrix(answer, op1.columns, op2.columns);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix transposed with the specified matrix.
	 * @param op2
	 * @return
	 */
	public Matrix multiplyTransposeOp1(Matrix op2)
	{
		return multiplyTransposeOp1(op2, null);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix with the specified matrix
	 * transposed.
	 * @param op2
	 * @param f The function to apply to each element, or null.
	 * @return
	 */
	public Matrix multiplyTransposeOp2(Matrix op2, Function f)
	{
		Matrix op1 = this;
		double[] answer = new double[op1.rows * op2.rows];
		int answerindex = 0, op1index = 0, op2index;
		
		if (op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			op2index = 0;
			
			for (int j = 0; j < op2.rows; j++)
			{
				double sum = 0;
				
				for (int k = 0; k < op2.columns; k++)
				{
					sum += op1.data[op1index + k] * op2.data[op2index + k];
				}
				
				if (f != null)
					sum = f.apply(sum, i, j);
				
				answer[answerindex++] = sum;
				op2index += op2.columns;
			}
			
			op1index += op1.columns;
		}
		
		return new Matrix(answer, op1.rows, op2.rows);
	}
	
	
	/**
	 * Returns the result of multiplying this matrix with the specified matrix
	 * transposed.
	 * @param op2
	 * @return
	 */
	public Matrix multiplyTransposeOp2(Matrix op2)
	{
		return multiplyTransposeOp2(op2, null);
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
		double[] answer = new double[data.length];
		int index = 0;
		
		if (op1.rows != op2.rows || op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.columns; j++)
			{
				answer[index] = op1.data[index] * op2.data[index];
				index++;
			}
		}
		
		return new Matrix(answer, rows, columns);
	}
	
	
	/**
	 * Returns the result of multiplying every element in this matrix by the
	 * specified scalar value.
	 * @param op2
	 * @return
	 */
	public Matrix scale(double scale)
	{
		double[] answer = new double[data.length];
		int index = 0;
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				answer[index] = data[index] * scale;
				index++;
			}
		}
		
		return new Matrix(answer, rows, columns);
	}
	
	
	/**
	 * Returns the result of adding the specified matrix to this matrix.
	 * @param op2
	 * @return
	 */
	public Matrix add(Matrix op2)
	{
		Matrix op1 = this;
		double[] answer = new double[data.length];
		int index = 0;
		
		if (op1.rows != op2.rows || op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.columns; j++)
			{
				answer[index] = op1.data[index] + op2.data[index];
				index++;
			}
		}
		
		return new Matrix(answer, rows, columns);
	}
	
	
	/**
	 * Returns the result of subtracting the specified matrix from this matrix.
	 * @param op2
	 * @param f The function to apply to each element, or null.
	 * @return
	 */
	public Matrix subtract(Matrix op2, Function f)
	{
		Matrix op1 = this;
		double[] answer = new double[data.length];
		int index = 0;
		
		if (op1.rows != op2.rows || op1.columns != op2.columns)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.columns, op2.rows, op2.columns));
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.columns; j++)
			{
				double d = op1.data[index] - op2.data[index];
				
				if (f != null)
					d = f.apply(d, i, j);
				
				answer[index] = d;
				index++;
			}
		}
		
		return new Matrix(answer, rows, columns);
	}
	
	
	/**
	 * Returns the result of subtracting the specified matrix from this matrix.
	 * @param op2
	 * @return
	 */
	public Matrix subtract(Matrix op2)
	{
		return subtract(op2, null);
	}
	
	
	/**
	 * Returns a matrix containing the results of applying the specified
	 * function to all the elements of this matrix.
	 * @param f
	 * @return
	 */
	public Matrix apply(Function f)
	{
		double[] answer = new double[data.length];
		int index = 0;
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				answer[index] = f.apply(data[index], i, j);
				index++;
			}
		}
		
		return new Matrix(answer, rows, columns);
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
		
		int answerrows = endrow - startrow + 1;
		int answercols = endcolumn - startcolumn + 1;
		
		double[] answer = new double[answerrows * answercols];
		int index = startrow * columns + startcolumn, answerindex = 0;
		
		for (int i = 0; i < answerrows; i++)
		{
			System.arraycopy(data, index, answer, answerindex, answercols);
			index += columns;
			answerindex += answercols;
		}
		
		return new Matrix(answer, answerrows, answercols);
	}

	
	/**
	 * Gets the specified row from the matrix, as a column vector.
	 * @param row
	 * @return
	 */
	public Matrix getRowAsColumn(int row)
	{
		double[] answer = new double[columns];
		System.arraycopy(data, (row - 1) * columns, answer, 0, columns);
		return new Matrix(answer, columns, 1);
	}
	
	
	/**
	 * Represents a function which can be applied to the elements of the matrix.
	 */
	public interface Function
	{
		double apply(double value, int row, int col);
	}
}
