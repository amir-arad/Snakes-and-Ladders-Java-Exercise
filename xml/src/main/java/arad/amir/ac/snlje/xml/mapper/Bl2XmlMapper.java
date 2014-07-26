package arad.amir.ac.snlje.xml.mapper;

import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Bl2XmlMapper {
    private static final Logger log = LoggerFactory.getLogger(Bl2XmlMapper.class);

    private Game source;
    private Snakesandladders destination;

    public Bl2XmlMapper(Game source) {
        this.source = source;
        this.destination = new Snakesandladders();
    }

    public Snakesandladders convert(){
        // TODO implement
        return destination;
    }
}
