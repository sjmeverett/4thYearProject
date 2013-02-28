package pacman.entries.pacman2.mcts.backpropagationstrategies;

import pacman.entries.pacman2.mcts.GameNode;
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
	public void backpropagate(GameNode node, int survived, int pillscore, int ghostscore, int gamescore)
	{
		while (node != null)
		{
			node.score = ((node.visitCount - 1) * node.score + survived * gamescore) / node.visitCount;
			node = node.parent;
		}
	}
	
}
