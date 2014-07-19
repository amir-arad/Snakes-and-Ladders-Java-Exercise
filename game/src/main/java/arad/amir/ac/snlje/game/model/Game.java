package arad.amir.ac.snlje.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author amira
 * @since 18/07/2014
 */
public class Game {
    private static final Logger log = LoggerFactory.getLogger(Game.class);

    private List<Player> players;
    private Board board;
    private int currentTurn;
    protected int numberOfSoldiersToWin;

}
