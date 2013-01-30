package machinelearning.tests;

import static org.junit.Assert.*;

import machinelearning.NeuralNetwork;

import org.junit.Test;

public class NeuralNetworkTest
{
	private static final double DELTA = 10e-6;
	
	@Test
	public void test()
	{
		int[] layers = {2, 2, 1};
		NeuralNetwork network = new NeuralNetwork(layers, 1, 1);
		
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
		
		network.train(X, y, 100);
		/*
		assertArrayEquals(new double[] {0}, network.predict(new double[] {0, 0}), DELTA);
		assertArrayEquals(new double[] {0}, network.predict(new double[] {0, 1}), DELTA);
		assertArrayEquals(new double[] {0}, network.predict(new double[] {1, 0}), DELTA);
		assertArrayEquals(new double[] {0}, network.predict(new double[] {1, 1}), DELTA);*/
		
		System.out.println(network.predict(new double[] {0, 0})[0]);
		System.out.println(network.predict(new double[] {0, 1})[0]);
		System.out.println(network.predict(new double[] {1, 0})[0]);
		System.out.println(network.predict(new double[] {1, 1})[0]);
	}

}
