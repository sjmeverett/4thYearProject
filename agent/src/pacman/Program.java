package pacman;

import pacman.entries.pacman.MonteCarloPacManParameters;
import pacman.entries.pacman.MyPacMan;
import pacman.entries.pacman.neuralnetworks.NeuralNetworkGhostController;
import pacman.entries.pacman.neuralnetworks.RouletteMoveSelectionStrategy;

public class Program
{
	public static void main(String[] args)
	{
		Executor executor = new Executor();
		
		MonteCarloPacManParameters parameters = new MonteCarloPacManParameters();
		parameters.moveSelectionStrategy = new RouletteMoveSelectionStrategy();
		parameters.ghostModel = new NeuralNetworkGhostController("weights/PINKY.dat", "weights/INKY.dat", "weights/BLINKY.dat", "weights/SUE.dat");
		//parameters.opponent = new NeuralNetworkGhostController("weights/PINKY.dat", "weights/INKY.dat", "weights/BLINKY.dat", "weights/SUE.dat");
		executor.runGame(new MyPacMan(parameters), parameters.opponent, true, 40);
	}
}
