package pacman.entries.pacman.mcts;

import pacman.entries.pacman.Parameters;
import pacman.entries.pacman.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Represents the game of Ms Pac-Man in a tree, with a node for each decision
 * point.
 */
public class GameTree
{
	private Game state;
	private TurnBasedGameAdapter game;
	private GameNode root;
	
	private Parameters parameters;
	private BackpropagationStrategy backpropagationStrategy;
	
	
	/**
	 * Constructor.
	 * @param parameters An object containing the parameters for the algorithm.
	 */
	public GameTree(Parameters parameters)
	{
		this.parameters = parameters;
		this.root = new GameNode(null, MOVE.NEUTRAL);
	}
	
	
	/**
	 * Runs an iteration of Monte Carlo tree search on the game tree.
	 */
	public void runMCTS(Game state)
	{
		this.state = state;
		this.game = new TurnBasedGameAdapter(state, parameters.ghostController);
		
		GameNode node = selection();
		rollout(node);
	}
	
	
	/**
	 * Gets the move represented by the immediate child of the root node with
	 * the highest score.
	 * @return
	 */
	public MOVE getBestMove()
	{
		if (root.children == null)
			return MOVE.NEUTRAL;
		
		MOVE move = MOVE.NEUTRAL;
		double max = 0;
		
		for (GameNode node: root.children)
		{
			if (node.score > max)
			{
				max = node.score;
				move = node.move;
			}
		}
		
		return move;
	}
	
	
	/**
	 * Updates the backpropagation strategy to the first in the list of
	 * strategies which returns true for isActive.
	 * @param game
	 * @returns True if the strategy was changed as a result of the update;
	 * otherwise, false.
	 */
	public boolean updateBackpropagationStrategy(Game game)
	{
		BackpropagationStrategy old = backpropagationStrategy;
		
		for (BackpropagationStrategy strategy: parameters.backpropagationStrategies)
		{
			if (strategy.isActive(game))
			{
				backpropagationStrategy = strategy;
				return old != strategy;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Run the selection phase on the tree.
	 * @return
	 */
	private GameNode selection()
	{
		GameNode node = root;
		boolean died;
		
		game.advanceGameToNextNode();
		
		int startIndex = game.getPacmanCurrentNodeIndex();
		node.visitCount++;
		
		while (!node.isLeafNode())
		{
			//select a child according to our strategy
			node = parameters.selectionStrategy.selectChild(node);
			
			//play the move that the node represents
			died = game.playMove(node.move);
			
			//quit the selection and revert to the parent node if we died
			if (died)
			{
				node = node.parent;
				break;
			}
			
			node.visitCount++;
		}
		
		//expand if we're at a leaf, the expansion threshold has been reached,
		//and we're not past the maximum depth
		double pathlength = game.getDistanceFromPoint(startIndex);
				
		if (node.isLeafNode() && node.visitCount > parameters.expansionThreshold && pathlength < parameters.maxPathLength)
		{
			//add the children to the node
			expand(node);
			
			//pick a child and play it
			node = parameters.selectionStrategy.selectChild( node);
			died = game.playMove(node.move);
			
			if (died)
				node = node.parent;
			else
				node.visitCount++;
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
		int gamescore = state.getScore();
		
		while (!isStoppingCondition(lives, level, starttime))
		{
			state.advanceGame(parameters.pacmanController.getMove(state, 0), parameters.ghostController.getMove(state, 0));
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
		backpropagationStrategy.backpropagate(node, survived, pillscore, ghostscore, state.getScore() - gamescore);
	}
	
	
	
	private boolean isStoppingCondition(int lives, int level, int starttime)
	{
		return state.getPacmanNumberOfLivesRemaining() < lives
			 || state.getCurrentLevel() != level
			 || state.getCurrentLevelTime() - starttime > parameters.maxRolloutTime
			 || state.gameOver();
	}
}
