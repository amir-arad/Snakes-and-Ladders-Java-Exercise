package arad.amir.ac.snlje.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author amira
 * @since 19/07/2014
 */
public class Player {
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    public static enum Type{HUMAN, COMPUTER}

    private String name;
    private Type type;
    private List<Cell> soliderPositions;
}
