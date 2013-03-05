package pacman.entries.pacman.mcts.backpropagationstrategies.test;

import pacman.entries.pacman.mcts.GameNode;
import pacman.entries.pacman.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.game.Game;

public class AverageSurvivalBackpropagationStrategy implements BackpropagationStrategy
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
			node.score = ((node.visitCount - 1) * node.score + survived) / node.visitCount;
			node = node.parent;
		}
	}
}