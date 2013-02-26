package pacman.entries.pacman.neuralnetworks;

import pacman.game.Constants.MOVE;

/**
 * A strategy which selects the move which has the maximum score.
 */
public class MaxMoveSelectionStrategy implements MoveSelectionStrategy
{
	@Override
	public MOVE selectMove(double[] y)
	{
		MOVE[] classes = new MOVE[] { MOVE.UP, MOVE.DOWN, MOVE.LEFT, MOVE.RIGHT };
		MOVE move = null;
		double max = 0;
		
		for (int i = 0; i < y.length; i++)
		{
			if (y[i] > max)
			{
				max = y[i];
				move = classes[i];
			}
		}
		
		return move;
	}
}
