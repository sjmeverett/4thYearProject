package pacman.entries.pacman.logging;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostState
{
	public static final int FEATURE_COUNT = 14;
	
	public GHOST ghost;
	public boolean requiresAction;
	public int levelTime;
	public int edibleScore;
	public int activePowerPills;
	public int pacmanLives;
	
	public int edibleTime;
	
	public MOVE avoidPacman;
	public MOVE chasePacmanM, chasePacmanP, chasePacmanE;
	
	public double distanceToPacman;
	
	public GhostState(Game game, GHOST ghost)
	{
		this.ghost = ghost;
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
		
		chasePacmanM = game.getNextMoveTowardsTarget(ghostIndex, pacmanIndex, lastMove, DM.MANHATTAN);
		chasePacmanP = game.getNextMoveTowardsTarget(ghostIndex, pacmanIndex, lastMove, DM.PATH);
		chasePacmanE = game.getNextMoveTowardsTarget(ghostIndex, pacmanIndex, lastMove, DM.EUCLID);
		
		distanceToPacman = game.getDistance(ghostIndex, pacmanIndex, DM.MANHATTAN);
	}
	
	
	public double[] toArray()
	{
		//normalise to between -1 and +1
		return new double[]
		{
			/* 1*/ (double)levelTime / 4000,
			/* 2*/ (Math.log(edibleScore / 100) / Math.log(2)) / 5,
			/* 3*/ (double)activePowerPills / 4,
			/* 4*/ (double)pacmanLives / 4,
			
			/* 5*/ (double)edibleTime / 200,
			/* 6*/ (avoidPacman == MOVE.UP ? 1 : 0),
			/* 7*/ (avoidPacman == MOVE.DOWN ? 1 : 0),
			/* 8*/ (avoidPacman == MOVE.LEFT ? 1 : 0),
			/* 9*/ (avoidPacman == MOVE.RIGHT ? 1 : 0),
			
			/*10*/ (chasePacmanM == MOVE.UP ? 1 : 0),
			/*11*/ (chasePacmanM == MOVE.DOWN ? 1 : 0),
			/*12*/ (chasePacmanM == MOVE.LEFT ? 1 : 0),
			/*13*/ (chasePacmanM == MOVE.RIGHT ? 1 : 0),
			
			/*14*/ (chasePacmanP == MOVE.UP ? 1 : 0),
			/*15*/ (chasePacmanP == MOVE.DOWN ? 1 : 0),
			/*16*/ (chasePacmanP == MOVE.LEFT ? 1 : 0),
			/*17*/ (chasePacmanP == MOVE.RIGHT ? 1 : 0),
			
			/*18*/ (chasePacmanE == MOVE.UP ? 1 : 0),
			/*19*/ (chasePacmanE == MOVE.DOWN ? 1 : 0),
			/*20*/ (chasePacmanE == MOVE.LEFT ? 1 : 0),
			/*21*/ (chasePacmanE == MOVE.RIGHT ? 1 : 0),
			
			/*22*/ distanceToPacman / 100
		};
	}
	
	
	public double[] getDirection(Game game)
	{
		MOVE move = game.getGhostLastMoveMade(ghost);
		
		return new double[] {
			move == MOVE.UP ? 1.0 : 0.0,
			move == MOVE.DOWN ? 1.0 : 0.0,
			move == MOVE.LEFT ? 1.0 : 0.0,
			move == MOVE.RIGHT ? 1.0 : 0.0
		};
	}
}
