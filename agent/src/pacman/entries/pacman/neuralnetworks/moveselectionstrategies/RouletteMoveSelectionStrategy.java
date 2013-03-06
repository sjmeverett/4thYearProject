package pacman.entries.pacman.neuralnetworks.moveselectionstrategies;

import java.util.Random;

import pacman.game.Constants.MOVE;

/**
 * A strategy which selects a move with a probability related to its score
 * from the neural network.
 */
public class RouletteMoveSelectionStrategy implements MoveSelectionStrategy
{

	@Override
	public MOVE selectMove(double[] y)
	{
		//add up the outputs
		double total = 0;
		
		for (int i = 0; i < y.length; i++)
		{
			total += y[i];
		}
		
		//pick a number on the scale
		Random random = new Random();
		double rnd = random.nextDouble() * total;
		
		//pick the move that it corresponds to
		MOVE[] classes = new MOVE[] { MOVE.UP, MOVE.DOWN, MOVE.LEFT, MOVE.RIGHT };
		double sum = 0;
		
		for (int i = 0; i < y.length; i++)
		{
			sum += y[i];
			
			if (rnd < sum)
				return classes[i];
		}
		
		//shouldn't get here
		throw new IllegalStateException("Something wrong with roulette selection.");
	}
	
}
