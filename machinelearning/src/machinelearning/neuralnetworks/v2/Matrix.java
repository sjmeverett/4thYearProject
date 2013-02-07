package machinelearning.neuralnetworks.v2;

public class Matrix
{
	public double[] data;
	public int startIndex, rows, cols;
	
	public Matrix(int startIndex, int rows, int cols, double[] source, boolean addOnes)
	{
		this.rows = rows;
		
		if (addOnes)
		{
			cols++;
			this.cols = cols;
			this.startIndex = 0;
			int srcOffset = startIndex;
			int destOffset = 0;
			
			data = new double[rows * cols];
			
			for (int i = 0; i < rows; i++)
			{
				data[destOffset] = 1;
				System.arraycopy(source, srcOffset, data, destOffset + 1, cols - 1);
				srcOffset += cols - 1;
				destOffset += cols;
			}
		}
		else
		{
			data = new double[rows * cols];
			this.cols = cols;
			this.data = source;
			this.startIndex = startIndex;
		}
	}
	
	
	public Matrix(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		this.data = new double[rows * cols];
	}
	
	
	public Matrix(double[][] source, boolean addOnes)
	{
		int offset = 0;
		startIndex = 0;
		rows = source.length;
		
		if (addOnes)
		{
			cols = source[0].length + 1;
			data = new double[rows * cols];
			
			for (int i = 0; i < rows; i++)
			{
				data[offset] = 1;
				System.arraycopy(source[i], 0, data, offset + 1, cols - 1);
				offset += cols;
			}
		}
		else
		{
			cols = source[0].length;
			data = new double[rows * cols];
			
			for (int i = 0; i < rows; i++)
			{
				System.arraycopy(source[i], 0, data, offset, cols);
				offset += cols;
			}
		}
	}
	
	
	public Matrix multiply(Matrix op2, ElementFunction f)
	{
		Matrix op1 = this;
		
		if (op1.cols != op2.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.cols, op2.rows, op2.cols));
		
		Matrix answer = new Matrix(op1.rows, op2.cols);
		int op1offset = op1.startIndex, op2offset, answeroffset = 0;
		
		for (int i = 0; i < answer.rows; i++)
		{
			for (int j = 0; j < answer.cols; j++)
			{
				double sum = 0;
				op2offset = op2.startIndex + j;
				
				for (int k = 0; k < op1.cols; k++)
				{
					sum += op1.data[op1offset + k] * op2.data[op2offset];
					op2offset += op2.cols;
				}
				
				if (f != null)
					sum = f.apply(sum, i, j, answeroffset);
				
				answer.data[answeroffset] = sum;
				answeroffset++;
			}
			
			op1offset += op1.cols;
		}
		
		return answer;
	}
	
	
	public Matrix multiplyTransposeOp1(Matrix op2, ElementFunction f, Matrix answer)
	{
		Matrix op1 = this;
		
		if (op1.rows != op2.rows)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.cols, op2.rows, op2.cols));
		
		if (answer.rows != op1.cols || answer.cols != op2.cols)
			throw new IllegalArgumentException(String.format(
				"answer matrix must be %dx%d (%dx%d matrix given)",
				op1.cols, op1.cols, answer.rows, answer.cols));
		
		int op1offset, op2offset, answeroffset = 0;
		
		for (int i = 0; i < answer.rows; i++)
		{
			for (int j = 0; j < answer.cols; j++)
			{
				double sum = 0;
				op1offset = op1.startIndex + i;
				op2offset = op2.startIndex + j;
				
				for (int k = 0; k < op1.cols; k++)
				{
					sum += op1.data[op1offset] * op2.data[op2offset];
					op1offset += op1.cols;
					op2offset += op2.cols;
				}
				
				if (f != null)
					sum = f.apply(sum, i, j, answeroffset);
				
				answer.data[answeroffset] = sum;
				answeroffset++;
			}
		}
		
		return answer;
	}
	
	
	public Matrix multiplyTransposeOp2(Matrix op2, ElementFunction f)
	{
		Matrix op1 = this;
		
		if (op1.cols != op2.cols)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.cols, op2.rows, op2.cols));
		
		Matrix answer = new Matrix(op1.rows, op2.rows);
		int op1offset = op1.startIndex, op2offset, answeroffset = 0;
		
		for (int i = 0; i < answer.rows; i++)
		{
			op2offset = op2.startIndex;
			
			for (int j = 0; j < answer.cols; j++)
			{
				double sum = 0;
				
				for (int k = 0; k < op1.cols; k++)
				{
					sum += op1.data[op1offset + k] * op2.data[op2offset + k];
				}
				
				if (f != null)
					sum = f.apply(sum, i, j, answeroffset);
				
				answer.data[answeroffset] = sum;
				answeroffset++;
				op2offset += op2.cols;
			}
			
			op1offset += op1.cols;
		}
		
		return answer;
	}
	
	
	public Matrix subtract(Matrix op2)
	{
		Matrix op1 = this;
		
		if (op1.rows != op2.rows || op1.cols != op2.cols)
			throw new IllegalArgumentException(String.format(
				"non-conformant arguments (op1 is %dx%d, op2 is %dx%d)",
				op1.rows, op1.cols, op2.rows, op2.cols));
		
		Matrix answer = new Matrix(op1.rows, op1.cols);
		int answeroffset = 0, op1offset = op1.startIndex, op2offset = op2.startIndex;
		
		for (int i = 0; i < op1.rows; i++)
		{
			for (int j = 0; j < op1.cols; j++)
			{
				answer.data[answeroffset++] = op1.data[op1offset++] - op2.data[op2offset++];
			}
		}
		
		return answer;
	}
	
	
	public double sum(ElementFunction f)
	{
		int offset = startIndex;
		double sum = 0;
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				if (f != null)
					sum += f.apply(data[offset], i, j, offset);
				else
					sum += data[offset];
				
				offset++;
			}
		}
		
		return sum;
	}
	
	
	public Matrix part(int startRow, int endRow, int startColumn, int endColumn)
	{
		Matrix m = new Matrix(endRow - startRow + 1, endColumn - startColumn + 1);
		int srcOffset = startIndex + startRow * cols + startColumn;
		int destOffset = 0;
		
		for (int i = startRow; i <= endRow; i++)
		{
			System.arraycopy(data, srcOffset, m.data, destOffset, m.cols);
			srcOffset += cols;
			destOffset += m.cols;
		}
		
		return m;
	}
	
	
	public interface ElementFunction
	{
		double apply(double value, int row, int col, int offset);
	}
}
