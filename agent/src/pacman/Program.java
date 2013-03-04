package pacman;

import java.util.ArrayList;

import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.RandomNonRevPacMan;
import pacman.entries.pacman2.MyPacMan;
import pacman.entries.pacman2.Parameters;
import pacman.entries.pacman2.mcts.backpropagationstrategies.AverageScoreBackpropagationStrategy;
import pacman.entries.pacman2.mcts.backpropagationstrategies.AverageSurvivalBackpropagationStrategy;
import pacman.entries.pacman2.mcts.backpropagationstrategies.BackpropagationStrategy;
import pacman.entries.pacman2.mcts.selectionstrategies.LevineUcbSelectionStrategy;


public class Program
{
	public static void main(String[] args)
	{
		Executor executor = new Executor();
		
//		MonteCarloPacManParameters parameters = new MonteCarloPacManParameters();
//		parameters.moveSelectionStrategy = new RouletteMoveSelectionStrategy();
//		parameters.nodeExpansionThreshold = 3;
//		//parameters.ghostModel = new NeuralNetworkGhostController("weights/PINKY.dat", "weights/INKY.dat", "weights/BLINKY.dat", "weights/SUE.dat");
//		//parameters.opponent = new NeuralNetworkGhostController("weights/PINKY.dat", "weights/INKY.dat", "weights/BLINKY.dat", "weights/SUE.dat");
//		executor.runGame(new MyPacMan(parameters), parameters.opponent, true, 40);
		
		Parameters parameters = new Parameters();
		parameters.allowReversing = false;
		
		parameters.backpropagationStrategies = new ArrayList<BackpropagationStrategy>();
		parameters.backpropagationStrategies.add(new AverageScoreBackpropagationStrategy());
		
		parameters.expansionThreshold = 10;
		parameters.ghostController = new Legacy();
		parameters.maxPathLength = 20;
		parameters.maxRolloutTime = 100;
		parameters.pacmanController = new RandomNonRevPacMan();
		parameters.selectionStrategy = new LevineUcbSelectionStrategy();
		
		MyPacMan pacman = new MyPacMan(parameters);
		executor.runGame(pacman, new Legacy(), true, 40);
	}
}
