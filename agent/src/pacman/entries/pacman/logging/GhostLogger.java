package pacman.entries.pacman.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostLogger
{
	private GHOST ghost;
	private BufferedWriter writer;
	private GhostState lastState;
	
	public GhostLogger(GHOST ghost, String controller)
	{
		this.ghost = ghost;

		try
		{
			writer = new BufferedWriter(new FileWriter(String.format("logs/%s-%s.txt", controller, ghost.name())));
		}
		catch (IOException ex)
		{
			System.out.println("Could not open ghost log file.");
			ex.printStackTrace();
		}
	}
	
	
	public void log(Game game)
	{
		try
		{
			if (lastState != null && lastState.requiresAction)
			{			
				writeArray(lastState.toArray());
				
				MOVE move = game.getGhostLastMoveMade(ghost);
				writer.write(String.format("%.6f, ", move == MOVE.UP ? 1.0 : 0.0));
				writer.write(String.format("%.6f, ", move == MOVE.DOWN ? 1.0 : 0.0));
				writer.write(String.format("%.6f, ", move == MOVE.LEFT ? 1.0 : 0.0));
				writer.write(String.format("%.6f\n", move == MOVE.RIGHT ? 1.0 : 0.0));
				writer.flush();
			}
			
			lastState = new GhostState(game, ghost);
		}
		catch (IOException ex)
		{
			System.out.println("Could not log ghost data.");
			ex.printStackTrace();
		}
	}
	
	
	private void writeArray(double[] d) throws IOException
	{
		for (int i = 0; i < d.length; i++)
		{
			writer.write(String.format("%.6f", d[i]));
			writer.write(", ");
		}
	}
}
