package pacman.entries.pacman2.mcts;

import java.util.EnumMap;
import java.util.HashSet;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Adapter class which wraps the Pac-Man game class to make it appear as a
 * turn-based game.
 */
public class TurnBasedGameAdapter
{
	private Game game;
	private Controller<EnumMap<GHOST, MOVE>> ghostController;
	
	/**
	 * Constructor.
	 * @param game The game object to wrap.
	 * @param ghostController The controller to use for ghost behaviour.
	 */
	public TurnBasedGameAdapter(Game game, Controller<EnumMap<GHOST, MOVE>> ghostController)
	{
		this.game = game;
		this.ghostController = ghostController;
	}
		
	
	/**
	 * Advances the game by playing the move specified for Ms. Pac-Man.
	 * @param node
	 * @returns True if pac-man lost a life during the move; otherwise, false.
	 */
	public boolean playMove(MOVE move)
	{
		int lives = game.getPacmanNumberOfLivesRemaining();
		
		//we should be at a decision point before making a decision
		if (!isAtNode())
			advanceGameToNextNode();
		
		//play the move that the node represents
		game.advanceGame(move, ghostController.getMove(game, 0));
		
		//run up till the next decision point
		advanceGameToNextNode();
		
		return game.getPacmanNumberOfLivesRemaining() < lives;
	}
	
	
	/**
	 * Gets the current node index of Ms Pac-Man.
	 * @return
	 */
	public int getPacmanCurrentNodeIndex()
	{
		return game.getPacmanCurrentNodeIndex();
	}
	
	
	/**
	 * Gets the path distance from the specified point to Ms Pac-Man's current position.
	 * @param index
	 * @return
	 */
	public double getDistanceFromPoint(int index)
	{
		return game.getDistance(index, game.getPacmanCurrentNodeIndex(), DM.PATH);
	}
	
	
	/**
	 * Advances the specified game object to the next node in the graph, that is, the next Pac-Man decision point (or game over).
	 * @param ghostModel The controller to use to model the ghost's behaviour.
	 * @return True if Ms Pac-Man made it to the next node without being eaten; otherwise, false.
	 */
	private void advanceGameToNextNode()
	{
		MOVE move = game.getPacmanLastMoveMade();

		while (!isAtNode())
		{
			//advance the game
			//opponent models that care about the amount of time they have to return an answer won't work here
			//but we can't allow lengthy simulations to run for ghost behaviour or we'll run out of time
			game.advanceGame(move, ghostController.getMove(game, 0));
		}
	}
	
	
	/**
	 * Determines if the current game position is a node in the graph; i.e., if it is a Pac-Man decision point (or game over).
	 * @return
	 */
	public boolean isAtNode()
	{
		int nodeIndex = game.getPacmanCurrentNodeIndex();
		boolean powerpill = false;
		
		for (int index: game.getActivePowerPillsIndices())
		{
			if (index == nodeIndex)
			{
				powerpill = true;
				break;
			}
		}
		
		return game.gameOver() 
			|| game.isJunction(nodeIndex)
			//|| game.isPowerPillStillAvailable(nodeIndex)
			|| powerpill
			|| againstWall();
	}
	
	
	/**
	 * Determines if Ms. Pac-Man has ran into a wall based on the direction she was previously going in.
	 * @return
	 */
	private boolean againstWall()
	{
		MOVE move = game.getPacmanLastMoveMade();
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		
		for (int i = 0; i < possibleMoves.length; i++)
		{
			if (possibleMoves[i] == move)
				return false;
		}
		
		return true;
	}
}
