package pacman.entries.pacman.mcts.backpropagationstrategies.test;

import pacman.entries.pacman.mcts.GameNode;
import pacman.entries.pacman.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.game.Game;

/**
 * Propagates average game scores up the tree.
 */
public class AverageScoreBackpropagationStrategy implements BackpropagationStrategy
{
	@Override
	public boolean isActive(Game game)
	{
		//this is not a cooperative strategy
		return true;
	}

	@Override
	public void backpropagate(GameNode node, int survived, int pillscore, int powerpills, int ghostscore, int gamescore)
	{
		while (node != null)
		{
			node.score = ((node.visitCount - 1) * node.score + survived * gamescore) / node.visitCount;
			node = node.parent;
		}
	}
	
}
