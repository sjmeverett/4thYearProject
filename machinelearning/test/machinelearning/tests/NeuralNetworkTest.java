package machinelearning.tests;

import static org.junit.Assert.*;

import machinelearning.NeuralNetwork;

import org.junit.Test;

public class NeuralNetworkTest
{	
	@Test
	public void test()
	{
		int[] layers = {2, 2, 1};
		NeuralNetwork network = new NeuralNetwork(layers);
		
		double[][] X = {
			{0, 0},
			{0, 1},
			{1, 0},
			{1, 1}
		};
		
		double[][] y = {
			{0},
			{1},
			{1},
			{0}
		};
		
		network.train(X, y, 1000, 1, 0.1, null);
		
		assertEquals(0, (int)Math.round(network.predict(new double[] {0, 0})[0]));
		assertEquals(1, (int)Math.round(network.predict(new double[] {0, 1})[0]));
		assertEquals(1, (int)Math.round(network.predict(new double[] {1, 0})[0]));
		assertEquals(0, (int)Math.round(network.predict(new double[] {1, 1})[0]));
	}

}
