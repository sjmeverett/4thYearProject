package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.mcts.GameTree;
import pacman.entries.pacman.mcts.TurnBasedGameAdapter;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	private Parameters parameters;
	private GameTree tree;
	
	
	public MyPacMan(Parameters parameters)
	{
		this.parameters = parameters;
	}
	

	@Override
	public MOVE getMove(Game game, long timedue)
	{
		//depending on the game mode, we don't get the right arguments sometimes
		if (timedue == -1)
			timedue = System.currentTimeMillis() + 39;
		
		//run the game tasks
		if (parameters.gameTasks != null)
		{
			for (GameTask task: parameters.gameTasks)
				task.run(game);
		}
		
		//tree will equal null on the first turn
		if (tree == null)
			tree = new GameTree(parameters);
		
		//allow tactics to change depending on the game state
		if (tree.updateBackpropagationStrategy(game))
		{
			tree = new GameTree(parameters);
			tree.updateBackpropagationStrategy(game);
		}
		
		//run mcts
		while (System.currentTimeMillis() < timedue)
		{
			tree.runMCTS(game.copy());
		}
		
		//if we're not allowed to reverse, we only return a move if we're at a node
		if (!parameters.allowReversing)
		{
			TurnBasedGameAdapter g = new TurnBasedGameAdapter(game, parameters.ghostController);
			
			if (!g.isAtNode())
				return MOVE.NEUTRAL;
		}
		
		//get the best move and make a new game tree
		MOVE move = tree.getBestMove();
		tree = new GameTree(parameters);
		return move;
	}
}
