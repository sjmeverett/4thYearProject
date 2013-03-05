package pacman;

import java.util.ArrayList;

import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.RandomNonRevPacMan;
import pacman.entries.pacman.MyPacMan;
import pacman.entries.pacman.Parameters;
import pacman.entries.pacman.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.entries.pacman.mcts.backpropagationstrategies.EatingStrategy;
import pacman.entries.pacman.mcts.backpropagationstrategies.HuntingStrategy;
import pacman.entries.pacman.mcts.selectionstrategies.LevineUcbSelectionStrategy;


public class Program
{
	public static void main(String[] args)
	{
		Executor executor = new Executor();

		Parameters parameters = new Parameters();
		parameters.allowReversing = false;
		
		parameters.backpropagationStrategies = new ArrayList<BackpropagationStrategy>();
		//parameters.backpropagationStrategies.add(new HuntingStrategy());
		parameters.backpropagationStrategies.add(new EatingStrategy());
		
		parameters.expansionThreshold = 10;
		parameters.ghostController = new Legacy();
		parameters.maxPathLength = 30;
		parameters.maxRolloutTime = 100;
		parameters.pacmanController = new RandomNonRevPacMan();
		parameters.selectionStrategy = new LevineUcbSelectionStrategy();
		
		MyPacMan pacman = new MyPacMan(parameters);
		executor.runGame(pacman, new Legacy(), true, 40);
	}
}
