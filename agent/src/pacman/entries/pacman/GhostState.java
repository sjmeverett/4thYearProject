package pacman.entries.pacman;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostState
{
	public boolean requiresAction;
	public int levelTime;
	public int edibleScore;
	public int activePowerPills;
	public int pacmanLives;
	
	public int edibleTime;
	
	public MOVE avoidPacman;
	public MOVE chasePacman;
	
	public double distanceToPacman;
	
	public GhostState(Game game, GHOST ghost)
	{
		requiresAction = game.doesGhostRequireAction(ghost);
		
		levelTime = game.getCurrentLevelTime();
		edibleScore = game.getGhostCurrentEdibleScore();
		activePowerPills = game.getNumberOfActivePowerPills();
		pacmanLives = game.getPacmanNumberOfLivesRemaining();
		
		edibleTime = game.getGhostEdibleTime(ghost);
		
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		int pacmanIndex = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		avoidPacman = game.getNextMoveAwayFromTarget(ghostIndex, pacmanIndex, lastMove, DM.MANHATTAN);
		chasePacman = game.getNextMoveTowardsTarget(ghostIndex, pacmanIndex, lastMove, DM.MANHATTAN);
		
		distanceToPacman = game.getDistance(ghostIndex, pacmanIndex, DM.MANHATTAN);
	}
	
	
	public double[] toArray()
	{
		//normalise to between -1 and +1
		return new double[]
		{
			(double)levelTime / 4000,
			(Math.log(edibleScore / 100) / Math.log(2)) / 5,
			(double)activePowerPills / 4,
			(double)pacmanLives / 4,
			
			(double)edibleTime / 200,
			(avoidPacman == MOVE.UP ? 1 : 0),
			(avoidPacman == MOVE.DOWN ? 1 : 0),
			(avoidPacman == MOVE.LEFT ? 1 : 0),
			(avoidPacman == MOVE.RIGHT ? 1 : 0),
			
			(chasePacman == MOVE.UP ? 1 : 0),
			(chasePacman == MOVE.DOWN ? 1 : 0),
			(chasePacman == MOVE.LEFT ? 1 : 0),
			(chasePacman == MOVE.RIGHT ? 1 : 0),
			
			distanceToPacman / 100
		};
	}
}
