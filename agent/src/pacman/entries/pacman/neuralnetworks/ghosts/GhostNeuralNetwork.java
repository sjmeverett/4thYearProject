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
    private boolean bulkTrain;
	
	private double[] x, y;
	
	/**
	 * Constructor.
	 * @param ghost The ghost that this network is to represent.
	 * @param selectionStrategy A strategy for selecting moves from network output.
     * @param bulkTrain True if the ghost is to be trained only when a decision is made;
     * otherwise, false to train every tick.
	 */
	public GhostNeuralNetwork(GHOST ghost, MoveSelectionStrategy selectionStrategy, boolean bulkTrain)
	{
		network = new NeuralNetwork(GhostState.FEATURE_COUNT, HIDDEN_UNITS, 4);
		this.ghost = ghost;
		this.selectionStrategy = selectionStrategy;
        this.bulkTrain = bulkTrain;
	}
	
	
	/**
	 * Allows the initial weights to be specified.
	 * @param ghost
	 * @param selectionStrategy
	 * @param theta1
	 * @param theta2
     * @param bulkTrain
	 */
	public GhostNeuralNetwork(GHOST ghost, MoveSelectionStrategy selectionStrategy, double[] theta1, double[] theta2, boolean bulkTrain)
	{
		network = new NeuralNetwork(GhostState.FEATURE_COUNT, HIDDEN_UNITS, 4, theta1, theta2);
		this.ghost = ghost;
		this.selectionStrategy = selectionStrategy;
        this.bulkTrain = bulkTrain;
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

            //train only here if bulkTrain is true
            if (bulkTrain)
                network.train(x, y, LEARNING_RATE, iterations);
		}

        //train every tick if bulkTrain is false
		if (!bulkTrain && y != null)
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
