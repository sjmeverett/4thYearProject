package pacman;

import pacman.controllers.examples.AggressiveGhosts;
import pacman.entries.pacman.GameTask;
import pacman.entries.pacman.MyPacMan;
import pacman.entries.pacman.Parameters;
import pacman.entries.pacman.logging.GhostTeamLogger;

public class Program
{
	public static void main(String[] args)
	{
		Parameters parameters = new Parameters();
		parameters.ghostModel = new AggressiveGhosts();
		parameters.tasks = new GameTask[] { new GhostTeamLogger("AggressiveGhosts") };
		
		Executor executor = new Executor();
		executor.runGame(new MyPacMan(parameters), parameters.opponent, true, 40);
	}
}
