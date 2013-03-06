package pacman.entries.pacman.neuralnetworks;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NeuralNetworkGhostController extends Controller<EnumMap<GHOST, MOVE>>
{
	private Map<GHOST, GhostNeuralNetwork> networks;
	private int iterations;
	
	public NeuralNetworkGhostController(MoveSelectionStrategy selectionStrategy, int iterations)
	{
		this.iterations = iterations;
		networks = new HashMap<GHOST, GhostNeuralNetwork>();
		
		for (GHOST ghost: GHOST.values())
		{
			networks.put(ghost, new GhostNeuralNetwork(ghost, selectionStrategy));
		}
	}
	
	public NeuralNetworkGhostController(String pinkyWeights, String inkyWeights, String blinkyWeights, String sueWeights)
	{
		networks = new HashMap<GHOST, GhostNeuralNetwork>();
		loadGhost(pinkyWeights, GHOST.PINKY);
		loadGhost(inkyWeights, GHOST.INKY);
		loadGhost(blinkyWeights, GHOST.BLINKY);
		loadGhost(sueWeights, GHOST.SUE);
	}
	
	private void loadGhost(String file, GHOST ghost)
	{
		try
		{
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
			double[] theta1 = (double[])reader.readObject();
			double[] theta2 = (double[])reader.readObject();
			reader.close();
			
			GhostNeuralNetwork network = new GhostNeuralNetwork(ghost, new RouletteMoveSelectionStrategy(), theta1, theta2);
			networks.put(ghost, network);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timedue)
	{
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		for (GHOST ghost: GHOST.values())
		{
			if (game.doesGhostRequireAction(ghost))
				moves.put(ghost, networks.get(ghost).getMove(game));
		}
		
		return moves;
	}
	
	
	public void train(Game game)
	{
		for (GHOST ghost: GHOST.values())
		{
			networks.get(ghost).train(game, iterations);
		}
	}
}
