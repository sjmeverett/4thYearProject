package pacman;

import pacman.controllers.examples.Legacy;
import pacman.entries.pacman.GameTask;
import pacman.entries.pacman.MyPacMan;
import pacman.entries.pacman.Parameters;
import pacman.entries.pacman.logging.GhostTeamLogger;
import pacman.entries.pacman.neuralnetworks.ghosts.NeuralNetworkGhostController;
import pacman.entries.pacman.neuralnetworks.moveselectionstrategies.RouletteMoveSelectionStrategy;

public class Program
{
	public static void main(String[] args)
	{
		Parameters parameters = new Parameters();
		parameters.ghostModel = new NeuralNetworkGhostController(new RouletteMoveSelectionStrategy(), 200, true, true);
        parameters.simulationCount = 1000;
		parameters.tasks = new GameTask[] { (GameTask)parameters.ghostModel };
		
		Executor executor = new Executor();
		executor.runGame(new MyPacMan(parameters), parameters.opponent, true, 40);
	}
}
