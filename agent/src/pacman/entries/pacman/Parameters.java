package pacman.entries.pacman;

import java.util.EnumMap;
import java.util.List;

import pacman.controllers.Controller;
import pacman.entries.pacman.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.entries.pacman.mcts.selectionstrategies.SelectionStrategy;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Parameters
{
	/**
	 * The node selection strategy to use during the selection phase of MCTS.
	 */
	public SelectionStrategy selectionStrategy;
	
	/**
	 * The strategies to consider using when backpropagating the results of a
	 * rollout during MCTS. 
	 */
	public List<BackpropagationStrategy> backpropagationStrategies;
	
	/**
	 * A list of tasks to be run each game tick.
	 */
	public List<GameTask> gameTasks;
	
	/**
	 * The number of times a node must be visited before it is expanded.
	 */
	public int expansionThreshold;
	
	/**
	 * The maximum distance at which nodes are to be expanded.
	 */
	public double maxPathLength;
	
	/**
	 * The maximum number of game ticks a roll out is to last.
	 */
	public int maxRolloutTime;
	
	/**
	 * The Pac-Man controller to use during rollouts.
	 */
	public Controller<MOVE> pacmanController;
	
	/**
	 * The ghost controller to use during rollouts.
	 */
	public Controller<EnumMap<GHOST, MOVE>> ghostController;
	
	/**
	 * True if Pac-Man is to be allowed to reverse between decision points;
	 * otherwise, false.  If she is not to be allowed to reverse, this has
	 * the added side-effect of allowing the same tree to be worked on up
	 * until a decision point; otherwise a new tree is created every tick.
	 */
	public boolean allowReversing;
}
