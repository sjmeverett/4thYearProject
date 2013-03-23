package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Random;

/**
 *
 */
public class PansyGhosts extends Controller<EnumMap<Constants.GHOST, Constants.MOVE>>
{
    private Random random;
    private double probability;

    public PansyGhosts(double probability)
    {
        this.probability = probability;
    }


    public PansyGhosts()
    {
        this(1.0);
    }

    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game,long timeDue)
    {
        EnumMap<Constants.GHOST, Constants.MOVE> moves = new EnumMap<Constants.GHOST, Constants.MOVE>(Constants.GHOST.class);
        Constants.MOVE[] values = Constants.MOVE.values();

        for (Constants.GHOST ghost: Constants.GHOST.values())
        {
            if (random.nextDouble() < probability)
            {
                moves.put(ghost, game.getNextMoveAwayFromTarget(
                    game.getGhostCurrentNodeIndex(ghost),
                    game.getPacmanCurrentNodeIndex(),
                    game.getGhostLastMoveMade(ghost),
                    Constants.DM.PATH));
            }
            else
            {
                moves.put(ghost, values[random.nextInt(values.length)]);
            }
        }

        return moves;
    }
}
