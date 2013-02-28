package pacman.entries.pacman2.mcts.selectionstrategies;

import java.util.List;
import pacman.entries.pacman2.mcts.GameNode;

public abstract class UcbSelectionStrategyBase implements SelectionStrategy
{

	@Override
	public GameNode selectChild(GameNode node)
	{
		List<GameNode> children = node.children;
		GameNode selectedChild = null;
		double max = Double.NEGATIVE_INFINITY;
		double currentUcb;
		
		if (children == null)
			throw new IllegalStateException("Cannot call selectChild on a leaf node.");
		
		for (GameNode child: children)
		{
			currentUcb = getUcbValue(child);
			
			if (Double.isNaN(currentUcb))
			{
				throw new IllegalStateException("UCB is not a number");
			}
			else if (currentUcb > max)
			{
				max = currentUcb;
				selectedChild = child;
			}
		}
		
		return selectedChild;
	}

	
	public abstract double getUcbValue(GameNode node);
}
