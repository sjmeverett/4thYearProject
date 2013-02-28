package pacman.entries.pacman2;

import pacman.game.Game;

/**
 * Represents a task that should be run every game tick.
 */
public interface GameTask
{
	/**
	 * Runs the task.
	 * @param game The current game state.
	 */
	void run(Game game);
}
