package arad.amir.ac.snlje.game.model;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.collection.AbstractCollectionDecorator;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;

/**
* @author amira
* @since 26/07/2014
*/
public class ElementsOnlyEqualityCollection<E> extends AbstractCollectionDecorator<E> {
    private final Collection<E> inner;

    public ElementsOnlyEqualityCollection(Collection<E> inner) {
        super(inner);
        this.inner = inner;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        for (E passage : inner) {
            hashCodeBuilder.append(passage);
        }
        return hashCodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this || obj == inner) {
            return true;
        }
        if (obj instanceof Collection) {
            return CollectionUtils.isEqualCollection(this, (Collection) obj);
        }
        return false;
    }
}
