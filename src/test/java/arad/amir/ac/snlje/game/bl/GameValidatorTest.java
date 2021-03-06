package arad.amir.ac.snlje.game.bl;

import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.xml.mapper.Xml2BlMapper;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.net.URI;
import java.util.Collection;

public class GameValidatorTest {
    private static final Logger log = LoggerFactory.getLogger(GameValidatorTest.class);

    private URI sampleXml;
    private Game sampleXmlGame;

    @Before
    public void setUp() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
        File sampleXml = new File(getClass().getClassLoader().getResource("snakesandladders.xml").toURI());
        Snakesandladders origXmlModel = (Snakesandladders) jc.createUnmarshaller().unmarshal(sampleXml);
        sampleXmlGame = new Xml2BlMapper().convert(origXmlModel);
    }

    @Test
    public void testValidateGame() throws Exception {
        testValidGame(sampleXmlGame);
    }



    static void testValidGame(Game game) {
        GameValidator.validateGame(game, new ControllerSession() {
            @Override
            public void printErrors(String error, Collection<String> errors) {
                for (String string : errors) {
                    log.error(string);
                }
                Assert.fail("validation failed : " + errors);
            }

            @Override
            public void printNewError(String error) {
                Assert.fail("validation failed : " + error);
            }

            @Override
            public void clearError() {}
        });
    }
}