package pacman.entries.pacman.mcts.backpropagationstrategies;

import pacman.entries.pacman.mcts.GameNode;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

/**
 * If there are edible ghosts, eat as many of them as possible.
 */
public class HuntingStrategy implements BackpropagationStrategy
{
	@Override
	public boolean isActive(Game game)
	{
		for (GHOST ghost: GHOST.values())
		{
			if (game.isGhostEdible(ghost))
				return true;
		}
		
		return false;
	}

	@Override
	public void backpropagate(GameNode node, int survived, int pillscore, int powerpills, int ghostscore, int gamescore)
	{
		System.out.printf("survived: %d | powerpills: %d | ghostscore: %d\n", survived, powerpills, ghostscore);
		
		//don't want to kamikaze or eat a powerpill if ghosts are already edible
		if (survived == 0 || powerpills > 0)
			ghostscore = 0;
		
		while (node != null)
		{
			if (ghostscore > node.score)
				node.score = ghostscore;
			
			//node.score = ((node.visitCount - 1) * node.score + survived * ghostscore) / node.visitCount;
			
			node = node.parent;
		}
	}

}
