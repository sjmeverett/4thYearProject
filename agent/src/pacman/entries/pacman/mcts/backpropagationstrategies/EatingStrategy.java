package pacman.entries.pacman.mcts.backpropagationstrategies;

import pacman.entries.pacman.mcts.GameNode;
import pacman.game.Game;

/**
 * Default backpropagation strategy: get a good score.
 */
public class EatingStrategy implements BackpropagationStrategy
{
	@Override
	public boolean isActive(Game game)
	{
		//default strategy
		return true;
	}

	@Override
	public void backpropagate(GameNode node, int survived, int pillscore, int powerpills, int ghostscore, int gamescore)
	{
		if (survived == 0)
			gamescore = -1000;
		
		while (node != null)
		{
//			if (gamescore > node.score)
//				node.score = gamescore;
			
			node.score = ((node.visitCount - 1) * node.score + gamescore) / node.visitCount;
			
			node = node.parent;
		}
	}

}
