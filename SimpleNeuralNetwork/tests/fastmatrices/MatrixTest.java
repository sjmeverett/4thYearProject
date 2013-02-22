package fastmatrices;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest
{
	private static final double EPSILON = 10e-6;

	
	@Test
	public void multiplyTest()
	{
		Matrix op1 = new Matrix(new double[][] {
			{1, 4},
			{2, 5},
			{3, 6}
		});
		
		Matrix op2 = new Matrix(new double[][] {
			{7, 8, 9},
			{10, 11, 12}
		});
		
		Matrix answer = op1.multiply(op2);
		
		assertEquals(47, answer.data[0], EPSILON);
		assertEquals(52, answer.data[1], EPSILON);
		assertEquals(57, answer.data[2], EPSILON);
		assertEquals(64, answer.data[3], EPSILON);
		assertEquals(71, answer.data[4], EPSILON);
		assertEquals(78, answer.data[5], EPSILON);
		assertEquals(81, answer.data[6], EPSILON);
		assertEquals(90, answer.data[7], EPSILON);
		assertEquals(99, answer.data[8], EPSILON);
	}

	
	@Test
	public void multiplyTransposeOp1Test()
	{
		Matrix op1 = new Matrix(new double[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
		
		Matrix op2 = new Matrix(new double[][] {
			{7, 8, 9},
			{10, 11, 12}
		});
		
		Matrix answer = op1.multiplyTransposeOp1(op2);
		
		assertEquals(47, answer.data[0], EPSILON);
		assertEquals(52, answer.data[1], EPSILON);
		assertEquals(57, answer.data[2], EPSILON);
		assertEquals(64, answer.data[3], EPSILON);
		assertEquals(71, answer.data[4], EPSILON);
		assertEquals(78, answer.data[5], EPSILON);
		assertEquals(81, answer.data[6], EPSILON);
		assertEquals(90, answer.data[7], EPSILON);
		assertEquals(99, answer.data[8], EPSILON);
	}
	
	
	@Test
	public void multiplyTransposeOp2Test()
	{
		Matrix op1 = new Matrix(new double[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
		
		Matrix op2 = new Matrix(new double[][] {
			{7, 8, 9},
			{10, 11, 12}
		});
		
		Matrix answer = op1.multiplyTransposeOp2(op2);
		
		assertEquals(50, answer.data[0], EPSILON);
		assertEquals(68, answer.data[1], EPSILON);
		assertEquals(122, answer.data[2], EPSILON);
		assertEquals(167, answer.data[3], EPSILON);
	}

	
	@Test
	public void subtractTest()
	{
		Matrix op1 = new Matrix(new double[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
		
		Matrix op2 = new Matrix(new double[][] {
			{7, 8, 9},
			{10, 11, 12}
		});
		
		Matrix answer = op1.subtract(op2);
		
		assertEquals(answer.data[0], -6, EPSILON);
		assertEquals(answer.data[1], -6, EPSILON);
		assertEquals(answer.data[2], -6, EPSILON);
		assertEquals(answer.data[3], -6, EPSILON);
		assertEquals(answer.data[4], -6, EPSILON);
		assertEquals(answer.data[5], -6, EPSILON);
	}
	
	
	@Test
	public void addTest()
	{
		Matrix op1 = new Matrix(new double[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
		
		Matrix op2 = new Matrix(new double[][] {
			{7, 8, 9},
			{10, 11, 12}
		});
		
		Matrix answer = op1.add(op2);
		
		assertEquals(8, answer.data[0], EPSILON);
		assertEquals(10, answer.data[1], EPSILON);
		assertEquals(12, answer.data[2], EPSILON);
		assertEquals(14, answer.data[3], EPSILON);
		assertEquals(16, answer.data[4], EPSILON);
		assertEquals(18, answer.data[5], EPSILON);
	}
	
	
	@Test
	public void transposeTest()
	{
		Matrix op1 = new Matrix(new double[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
		
		Matrix answer = op1.transpose();
		
		assertEquals(1, answer.data[0], EPSILON);
		assertEquals(4, answer.data[1], EPSILON);
		assertEquals(2, answer.data[2], EPSILON);
		assertEquals(5, answer.data[3], EPSILON);
		assertEquals(3, answer.data[4], EPSILON);
		assertEquals(6, answer.data[5], EPSILON);
	}
}
