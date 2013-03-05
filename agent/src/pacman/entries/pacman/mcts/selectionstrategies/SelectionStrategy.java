package pacman.entries.pacman.mcts.selectionstrategies;

import pacman.entries.pacman.mcts.GameNode;

/**
 * Represents a strategy for selecting a child node of a given game node,
 * usually in a manner which balances exploration and exploitation.
 */
public interface SelectionStrategy
{
	/**
	 * Selects a child of the given node according to the represented strategy.
	 * @param node The node to select a child from.
	 * @return
	 */
	GameNode selectChild(GameNode node);
}
