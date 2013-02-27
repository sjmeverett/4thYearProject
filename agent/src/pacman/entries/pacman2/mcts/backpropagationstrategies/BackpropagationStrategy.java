package pacman.entries.pacman2.mcts.backpropagationstrategies;

import pacman.entries.pacman2.mcts.GameNode;
import pacman.game.Game;

/**
 * The strategy to use when backpropagating scores after a rollout.
 */
public interface BackpropagationStrategy
{
	/**
	 * Determines if the strategy is active or not.  Different scoring
	 * strategies may be active at different times, depending on the desired
	 * goal in the current game state.
	 * @param game The current game state.
	 * @return True if the strategy should currently be active; otherwise, false.
	 */
	boolean isActive(Game game);
	
	
	/**
	 * Propagate scores back from the specified node, using the represented strategy. 
	 * @param node The node to propagate scores back from.
	 * @param survived 1 if Pac-Man survived the simulated game; otherwise, 0.
	 * @param pillscore The number of pills eaten during the simulated game.
	 * @param ghostscore The number of ghosts eaten during the simulated game.
	 */
	void backpropagate(GameNode node, int survived, int pillscore, int ghostscore);
}
