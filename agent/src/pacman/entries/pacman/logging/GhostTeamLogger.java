package pacman.entries.pacman.logging;

import java.util.HashMap;
import java.util.Map;

import pacman.entries.pacman.GameTask;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

/**
 * Logs ghost data for the whole ghost team.
 */
public class GhostTeamLogger implements GameTask
{
	private Map<GHOST, GhostLogger> loggers;
	
	/**
	 * Constructor.
	 * @param controller The name of the ghost controller, e.g. Legacy.
	 */
	public GhostTeamLogger(String controller)
	{
		loggers = new HashMap<GHOST, GhostLogger>();
		
		for (GHOST ghost: GHOST.values())
		{
			loggers.put(ghost, new GhostLogger(ghost, controller));
		}
	}
	
	@Override
	public void run(Game game)
	{
		for (GHOST ghost: GHOST.values())
		{
			loggers.get(ghost).log(game);
		}
	}
}
