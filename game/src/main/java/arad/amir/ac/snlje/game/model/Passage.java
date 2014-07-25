package arad.amir.ac.snlje.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 19/07/2014
 */
public class Passage {
    private static final Logger log = LoggerFactory.getLogger(Passage.class);
    public static enum Type{SNAKE, LADDER}

    private Type type;
    private Cell from;
    private Cell to;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Cell getFrom() {
        return from;
    }

    public void setFrom(Cell from) {
        this.from = from;
        from.setToPassage(this);
    }

    public Cell getTo() {
        return to;
    }

    public void setTo(Cell to) {
        this.to = to;
    }

    public Cell getHigh() {
        switch (type){
            case SNAKE: return from;
            case LADDER: return to;
        }
        throw new IllegalArgumentException("unexpected type : " + type);
    }

    public Cell getLow() {
        switch (type){
            case SNAKE: return to;
            case LADDER: return from;
        }
        throw new IllegalArgumentException("unexpected type : " + type);
    }

    public void setHigh(Cell high) {
        switch (type){
            case SNAKE: setFrom(high);
                break;
            case LADDER: setTo(high);
                break;
            default:
                throw new IllegalArgumentException("unexpected type : " + type);
        }
    }

    public void setLow(Cell low) {
        switch (type){
            case LADDER: setFrom(low);
                break;
            case SNAKE: setTo(low);
                break;
            default:
                throw new IllegalArgumentException("unexpected type : " + type);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passage)) return false;

        Passage passage = (Passage) o;

        if (!from.equals(passage.from)) return false;
        if (!to.equals(passage.to)) return false;
        if (type != passage.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Passage{" +
                "type=" + type +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
