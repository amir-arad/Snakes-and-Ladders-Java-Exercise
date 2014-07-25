package arad.amir.ac.snlje.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author amira
 * @since 19/07/2014
 */
public class Board {
    private static final Logger log = LoggerFactory.getLogger(Board.class);

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 8;

    private int size;
    private List<Cell> cells;
    private Collection<Passage> passages;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Collection<Passage> getPassages() {
        return passages;
    }

    public void setPassages(Collection<Passage> passages) {
        this.passages = passages;
    }
}
