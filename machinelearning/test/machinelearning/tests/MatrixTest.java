package machinelearning.tests;

import static org.junit.Assert.*;

import machinelearning.neuralnetworks.v2.Matrix;

import org.junit.Before;
import org.junit.Test;

public class MatrixTest
{
	private static final double EPSILON = 10e-6;
	private Matrix op1, op2;
	
	@Before
	public void setup()
	{
		double[] data = new double[] { 1, 2, 3, 4, 5, 6};
		op1 = new Matrix(0, 2, 2, data, false);
		op2 = new Matrix(4, 2, 1, data, true);
	}
	
	@Test
	public void multiplyTest()
	{
		Matrix answer = op1.multiply(op2, null);
		
		assertEquals(3, answer.data[0], EPSILON);
		assertEquals(17, answer.data[1], EPSILON);
		assertEquals(7, answer.data[2], EPSILON);
		assertEquals(39, answer.data[3], EPSILON);
	}

	@Test
	public void multiplyTransposeOp1Test()
	{
		Matrix answer = new Matrix(2, 2);
		op1.multiplyTransposeOp1(op2, null, answer);
		
		assertEquals(4, answer.data[0], EPSILON);
		assertEquals(23, answer.data[1], EPSILON);
		assertEquals(6, answer.data[2], EPSILON);
		assertEquals(34, answer.data[3], EPSILON);
	}
	
	@Test
	public void multiplyTransposeOp2Test()
	{
		Matrix answer = op1.multiplyTransposeOp2(op2, null);
		
		assertEquals(11, answer.data[0], EPSILON);
		assertEquals(13, answer.data[1], EPSILON);
		assertEquals(23, answer.data[2], EPSILON);
		assertEquals(27, answer.data[3], EPSILON);
	}
}
