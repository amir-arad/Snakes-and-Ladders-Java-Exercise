package org.dozer;

import arad.amir.ac.snlje.xml.mapper.BaseConverter;
import org.dozer.classmap.MappingFileData;
import org.dozer.loader.DozerBuilder;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author amira
 * @since 25/07/2014
 */
public class MyMapper extends DozerBeanMapper {
    private static final Logger log = LoggerFactory.getLogger(MyMapper.class);


    private Set<DozerConverter> alreadyAddedconvereters = new HashSet<>();
    private DozerBuilder globalConfigurationBuilder = new DozerBuilder();
    private DozerBuilder.ConfigurationBuilder configuration = globalConfigurationBuilder.configuration();

    @Override
    public void map(Object source, Object destination) throws MappingException {
        if (source == null) return;
        if (source.getClass().equals(destination.getClass())) {
            destination = source;
        }
        super.map(source, destination);
    }

    @Override
    public void map(Object source, Object destination, String mapId) throws MappingException {
        if (source == null) return;
        if (source.getClass().equals(destination.getClass())) {
            destination = source;
        }
        super.map(source, destination, mapId);
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        if (source == null) return null;
        if (source.getClass().equals(destinationClass)) {
            return (T)source;
        }
        return super.map(source, destinationClass);
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass, String mapId) throws MappingException {
        if (source == null) return null;
        if (source.getClass().equals(destinationClass)) {
            return (T)source;
        }
        return super.map(source, destinationClass, mapId);
    }

    /**
     * a simple, ineffecient way to easily register a custom mapper between two types
     */
    public synchronized void addMapping(final DozerConverter converter, final Class classA, final Class classB) {
        if (alreadyAddedconvereters.add(converter)){
            addConverter(converter);
        }

        DozerBuilder.CustomConverterBuilder customConverterBuilder = configuration.customConverter(converter.getClass());
        customConverterBuilder.classA(classA);
        customConverterBuilder.classB(classB);
    }


    /**
     * a simple, ineffecient way to easily register a custom mapper to a field
     */
    public synchronized void addMapping(final DozerConverter converter, final Class containerA, final Class containerB, final String fieldA, final String fieldB) {
        if (alreadyAddedconvereters.add(converter)){
            addConverter(converter);
        }
        // add mapping to super
        BeanMappingBuilder mappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(containerA, containerB).fields(fieldA, fieldB, FieldsMappingOptions.customConverter(converter.getClass()));
            }
        };

        super.addMapping(mappingBuilder);
    }

    public void addMapping(final BaseConverter converter) {
        addMapping(converter, converter.getPrototypeA(), converter.getPrototypeB());
    }


    void loadCustomMappings() {
        log.info("loading dozer mappings");

        // add mapping to super
        // this is an ugly, contra-design way to use the builder, but it's the only way to achieve the same functionality of the xml configuration
        BeanMappingBuilder mappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {}
            public MappingFileData build() {
                return globalConfigurationBuilder.build();
            }
        };
        super.addMapping(mappingBuilder);

        // now continue with mappings loading
        super.loadCustomMappings();
    }

    /**
     * add a converter
     */
    public synchronized void addConverter(DozerConverter converter) {
        // add converter to super
        List<CustomConverter> customConverters = new ArrayList<>();
        customConverters.addAll(super.getCustomConverters());
        customConverters.add(converter);
        super.setCustomConverters(customConverters);
    }

}
