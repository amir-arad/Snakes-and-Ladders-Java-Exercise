package arad.amir.ac.snlje.game.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
        Cell rhs = (Cell) obj;
        return new EqualsBuilder()
                .append(this.index, rhs.index)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(index)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("index", index)
                .toString();
    }
}
