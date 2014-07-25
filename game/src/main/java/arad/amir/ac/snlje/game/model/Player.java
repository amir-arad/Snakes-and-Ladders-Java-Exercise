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
    private List<Cell> soldierPositions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Cell> getSoldierPositions() {
        return soldierPositions;
    }

    public void setSoldierPositions(List<Cell> soldierPositions) {
        this.soldierPositions = soldierPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (!name.equals(player.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
