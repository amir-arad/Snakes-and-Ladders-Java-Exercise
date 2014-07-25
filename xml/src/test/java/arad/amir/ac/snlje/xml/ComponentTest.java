package arad.amir.ac.snlje.xml;

import arad.amir.ac.snlje.game.bl.GameBuilderTest;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.dozer.MyMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author amira
 * @since 25/07/2014
 */
public class ComponentTest {
    private static final Logger log = LoggerFactory.getLogger(ComponentTest.class);

    @Test
    public void testHappyFlow() throws Exception {
        MyMapper mapper = new MyMapper();
        JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
        File tempFile = File.createTempFile("JaxB", "ComponentTest");
        tempFile.deleteOnExit();

        Game origBlModel = GameBuilderTest.buildRandomGame(false);

        Snakesandladders origXmlModel = mapper.map(origBlModel, Snakesandladders.class);
        jc.createMarshaller().marshal(origXmlModel, tempFile);
        printFile(tempFile);
        Snakesandladders newXmlModel = (Snakesandladders) jc.createUnmarshaller().unmarshal(tempFile);
        Game newBlModel = mapper.map(newXmlModel, Game.class);

        Assert.assertEquals(origBlModel, newBlModel);

    }

    private void printFile(File tempFile)  {
        try(BufferedReader r = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = r.readLine()) != null) {
                log.info(line);
            }
        } catch (IOException e) {
            log.error("error printing file", e);
        }
    }

}
