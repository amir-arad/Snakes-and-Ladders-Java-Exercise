package arad.amir.ac.snlje.game.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
        Board rhs = (Board) obj;
        return new EqualsBuilder()
                .append(this.size, rhs.size)
                .append(this.cells, rhs.cells)
                .append(this.passages, rhs.passages)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(size)
                .append(cells)
                .append(passages)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("size", size)
                .append("passages", passages)
                .toString();
    }
}
