package pacman.entries.pacman2.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Represents the state of the game when Pac-Man is at a junction or power
 * pill.
 */
public class GameNode
{
	public GameNode parent;
	public Game state;
	public MOVE move;
	public List<GameNode> children;
	public double score;
	public int visitCount;
	
	/**
	 * 
	 * @param parent
	 * @param state
	 * @param move
	 */
	public GameNode(GameNode parent, MOVE move)
	{
		this.parent = parent;
		this.move = move;
		this.children = new ArrayList<GameNode>();
	}
	
	
	/**
	 * Determines if this node is a leaf node.
	 * @return True if this node is a leaf node; otherwise, false.
	 */
	public boolean isLeafNode()
	{
		return children.size() == 0;
	}
}
