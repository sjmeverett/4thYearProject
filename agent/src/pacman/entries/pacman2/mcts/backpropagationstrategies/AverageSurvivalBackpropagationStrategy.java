package pacman.entries.pacman2.mcts.backpropagationstrategies;

import pacman.entries.pacman2.mcts.GameNode;
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