package pacman.entries.pacman;

import pacman.game.Game;

/**
 * Represents a task which runs every game tick.
 */
public interface GameTask
{
	/**
	 * Runs the task.
	 * @param game The current game state.
	 */
	void run(Game game);
}
