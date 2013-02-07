package machinelearning;

public interface NeuronLayer
{
	public abstract void update(double[] input);

	public abstract void calculateError(double[] target);

	public abstract void calculateError(NeuronLayer successor);

	public abstract void updateWeights(double learningRate, double momentum, double[][] delta, int m);

	public double[] getOutput();
}