package pacman.entries.pacman2.mcts;

import java.util.EnumMap;

import pacman.controllers.Controller;
import pacman.entries.pacman2.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.entries.pacman2.mcts.selectionstrategies.SelectionStrategy;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class GameTree
{
	private SelectionStrategy selectionStrategy;
	private Game state;
	private TurnBasedGameAdapter game;
	private GameNode root;
	private int expansionThreshold;
	private double maxPathLength;
	private BackpropagationStrategy backpropagationStrategy;
	private int maxRolloutTime;
	private Controller<MOVE> pacmanController;
	private Controller<EnumMap<GHOST, MOVE>> ghostController;
	
	/**
	 * Run the selection phase on the tree.
	 * @return
	 */
	private GameNode selection()
	{
		GameNode node = root;
		boolean died;
		
		int startIndex = game.getPacmanCurrentNodeIndex();
		
		while (!node.isLeafNode())
		{
			//select a child according to our strategy
			node = selectionStrategy.selectChild( node);
			
			//play the move that the node represents
			died = game.playMove(node.move);
			
			//quit the selection and revert to the parent node if we died
			if (died)
			{
				node = node.parent;
				break;
			}
		}
		
		//expand if we're at a leaf, the expansion threshold has been reached,
		//and we're not past the maximum depth
		double pathlength = game.getDistanceFromPoint(startIndex);
				
		if (node.isLeafNode() && node.visitCount > expansionThreshold && pathlength > maxPathLength)
		{
			//add the children to the node
			expand(node);
			
			//pick a child and play it
			node = selectionStrategy.selectChild( node);
			died = game.playMove(node.move);
			
			if (died)
				node = node.parent;
		}
		
		return node;
	}
	
	
	/**
	 * Expands a game node with children representing available moves from the
	 * current point in the game.
	 * @param node The node to expand.
	 */
	private void expand(GameNode node)
	{
		MOVE[] availableMoves;
		int index = game.getPacmanCurrentNodeIndex();
		
		//only consider reverse moves at the top of the tree
		if (node == root)
			availableMoves = state.getPossibleMoves(index);
		else
			availableMoves = state.getPossibleMoves(index, state.getPacmanLastMoveMade());
		
		//add the children
		for (MOVE move: availableMoves)
		{
			node.children.add(new GameNode(node, move));
		}
	}
	
	
	/**
	 * Plays a simulated game from the current point until a stopping condition
	 * is reached.
	 * @param node The node the rollout is starting from.
	 */
	private void rollout(GameNode node)
	{
		int lives = state.getPacmanNumberOfLivesRemaining();
		int level = state.getCurrentLevel();
		int starttime = state.getCurrentLevelTime();
		int pills = state.getNumberOfActivePills();
		int ghostscore = 0;
		
		while (!isStoppingCondition(lives, level, starttime))
		{
			state.advanceGame(pacmanController.getMove(state, 0), ghostController.getMove(state, 0));
			ghostscore += state.getNumGhostsEaten();
		}
		
		int pillscore;
		
		//if the level has completed, our pills eaten count might be wrong
		//so just make it 100 as a reward for completing the level
		if (state.getCurrentLevel() != level)
			pillscore = 100;
		else
			pillscore = pills - state.getNumberOfActivePills();
		
		//survived is 1 if we avoided being eaten, and 0 otherwise
		int survived = state.getPacmanNumberOfLivesRemaining() < lives ? 0 : 1;
		
		//backpropagate the score according to whatever strategy is active
		backpropagationStrategy.backpropagate(node, survived, pillscore, ghostscore);
	}
	
	
	
	private boolean isStoppingCondition(int lives, int level, int starttime)
	{
		return state.getPacmanNumberOfLivesRemaining() < lives
			 || state.getCurrentLevel() != level
			 || state.getCurrentLevelTime() - starttime > maxRolloutTime
			 || state.gameOver();
	}
}
