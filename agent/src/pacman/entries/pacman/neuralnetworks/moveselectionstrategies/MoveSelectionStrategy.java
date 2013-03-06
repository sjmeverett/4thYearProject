package pacman.entries.pacman.neuralnetworks.moveselectionstrategies;

import pacman.game.Constants.MOVE;

/**
 * Represents a strategy for selecting a move to play given the outputs from
 * the neural network.
 */
public interface MoveSelectionStrategy
{
	/**
	 * Select a move, given the outputs from the neural network.
	 * @param y
	 * @return
	 */
	MOVE selectMove(double[] y);
}
