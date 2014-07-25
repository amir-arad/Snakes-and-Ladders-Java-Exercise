package arad.amir.ac.snlje.xml.mapper;

import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 12/16/13
 */
public abstract class BaseConverter<T, C>  extends DozerConverter<T, C> implements MapperAware {
    private static final Logger log = LoggerFactory.getLogger(BaseConverter.class);
    private final Class<T> prototypeA;
    private final Class<C> prototypeB;

    private Mapper mapper;

    public BaseConverter(Class<T> prototypeA, Class<C> prototypeB) {
        super(prototypeA, prototypeB);
        this.prototypeA = prototypeA;
        this.prototypeB = prototypeB;
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public Class<T> getPrototypeA() {
        return prototypeA;
    }

    public Class<C> getPrototypeB() {
        return prototypeB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseConverter)) return false;

        BaseConverter that = (BaseConverter) o;

        if (!prototypeA.equals(that.prototypeA)) return false;
        if (!prototypeB.equals(that.prototypeB)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = prototypeA.hashCode();
        result = 31 * result + prototypeB.hashCode();
        return result;
    }
}
