package pacman.entries.pacman.neuralnetworks.ghosts;

import pacman.entries.pacman.logging.GhostState;
import pacman.entries.pacman.neuralnetworks.NeuralNetwork;
import pacman.entries.pacman.neuralnetworks.moveselectionstrategies.MoveSelectionStrategy;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Wraps the neural network class to expose methods relevant to the application
 * to learning ghost behaviour.
 */
public class GhostNeuralNetwork
{
	private static final double LEARNING_RATE = 1;
	private static final int HIDDEN_UNITS = 10;
	
	private NeuralNetwork network;
	private GHOST ghost;
	private GhostState state;
	private MoveSelectionStrategy selectionStrategy;
	
	private double[] x, y;
	
	/**
	 * Constructor.
	 * @param ghost The ghost that this network is to represent.
	 * @param selectionStrategy A strategy for selecting moves from network output.
	 */
	public GhostNeuralNetwork(GHOST ghost, MoveSelectionStrategy selectionStrategy)
	{
		network = new NeuralNetwork(GhostState.FEATURE_COUNT, HIDDEN_UNITS, 4);
		this.ghost = ghost;
		this.selectionStrategy = selectionStrategy;
	}
	
	
	/**
	 * Allows you to specify the initial weights.
	 * @param ghost
	 * @param selectionStrategy
	 * @param theta1
	 * @param theta2
	 */
	public GhostNeuralNetwork(GHOST ghost, MoveSelectionStrategy selectionStrategy, double[] theta1, double[] theta2)
	{
		network = new NeuralNetwork(GhostState.FEATURE_COUNT, HIDDEN_UNITS, 4, theta1, theta2);
		this.ghost = ghost;
		this.selectionStrategy = selectionStrategy;
	}
	
	
	/**
	 * Trains the network with the game state.
	 * @param game
	 */
	public void train(Game game, int iterations)
	{
		if (state != null && state.requiresAction)
		{
			x = state.toArray();
			y = state.getDirection(game);
		}
			
		if (y != null)
		{
			network.train(x, y, LEARNING_RATE, iterations);
		}
		
		state = new GhostState(game, ghost);
	}
	
	
	/**
	 * Predicts a move for the learned model from the current game state.
	 * @param game
	 * @return
	 */
	public MOVE getMove(Game game)
	{
		GhostState current = new GhostState(game, ghost);
		double[] y = network.predict(current.toArray());
		return selectionStrategy.selectMove(y);
	}
}
