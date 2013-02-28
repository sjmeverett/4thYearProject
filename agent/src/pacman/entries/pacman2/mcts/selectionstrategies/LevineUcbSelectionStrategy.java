package pacman.entries.pacman2.mcts.selectionstrategies;

import java.util.Random;

import pacman.entries.pacman2.mcts.GameNode;

/**
 * Provides UCB calculation based on the UCB1 formula found in Levine's paper.
 */
public class LevineUcbSelectionStrategy extends UcbSelectionStrategyBase
{
	private static final double DEFAULT_BALANCE = 4000;
	private double balanceParameter;
	
	/**
	 * Constructor.  Uses the default balance parameter.
	 */
	public LevineUcbSelectionStrategy()
	{
		this.balanceParameter = DEFAULT_BALANCE;
	}
	
	
	/**
	 * Constructor.
	 * @param balanceParameter The balance parameter (balance between exploration and exploitation) to use.
	 */
	public LevineUcbSelectionStrategy(double balanceParameter)
	{
		this.balanceParameter = balanceParameter;
	}


	@Override
	public double getUcbValue(GameNode node)
	{
		//if the node hasn't been visited yet, just return a random number
		//close to zero
		if (node.visitCount == 0)
		{
			return new Random().nextDouble() * 0.2;
		}
		else
		{
			return node.score + balanceParameter * 
				Math.sqrt(Math.log(node.parent.visitCount) / node.visitCount);
		}
	}
}
