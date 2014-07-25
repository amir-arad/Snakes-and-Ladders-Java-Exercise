package arad.amir.ac.snlje.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 19/07/2014
 */
public class Cell {
    private static final Logger log = LoggerFactory.getLogger(Cell.class);

    private int index;
    private Passage toPassage;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Passage getToPassage() {
        return toPassage;
    }

    public void setToPassage(Passage toPassage) {
        this.toPassage = toPassage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;

        Cell cell = (Cell) o;

        if (index != cell.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "index=" + index +
                '}';
    }
}
