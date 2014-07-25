package arad.amir.ac.snlje.game.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("type", type)
                .append("soldierPositions", soldierPositions)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Player rhs = (Player) obj;
        return new EqualsBuilder()
                .append(this.name, rhs.name)
                .append(this.type, rhs.type)
                .append(this.soldierPositions, rhs.soldierPositions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(type)
                .append(soldierPositions)
                .toHashCode();
    }

}
