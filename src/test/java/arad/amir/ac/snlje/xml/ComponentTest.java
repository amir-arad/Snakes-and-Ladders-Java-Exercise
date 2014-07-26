package arad.amir.ac.snlje.xml;

import arad.amir.ac.snlje.game.bl.GameBuilderTest;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.xml.mapper.Bl2XmlMapper;
import arad.amir.ac.snlje.xml.mapper.Xml2BlMapper;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author amira
 * @since 25/07/2014
 */
public class ComponentTest {
    private static final Logger log = LoggerFactory.getLogger(ComponentTest.class);
    private URI sampleXml;

    @Before
    public void init() throws URISyntaxException {
        this.sampleXml = getClass().getClassLoader().getResource("snakesandladders.xml").toURI();
    }

    @Test
    public void suppliedXmlSampleCarbonCopyTest() throws Exception {
        File tempFile = makeTempFile();
        JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
        File sampleXml = new File(this.sampleXml);
        Snakesandladders origXmlModel = (Snakesandladders) jc.createUnmarshaller().unmarshal(sampleXml);
        Game blModel = new Xml2BlMapper().convert(origXmlModel);
        Snakesandladders newXmlModel =  new Bl2XmlMapper().convert(blModel, "game1");
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(newXmlModel, tempFile);
        printFile(tempFile);

        // assert tempFile XmlEquals sampleXml
        Diff diff = XMLUnit.compareXML(new FileReader(sampleXml), new FileReader(tempFile));
        if (!diff.similar()){
            Assert.fail ("xml differs : " + diff.toString());
        }
    }

    @Test
    public void randomBlGameCarbonCopyTest() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
        File tempFile = makeTempFile();

        for (int i = 0; i < 100; i++) {
            Game origBlModel = GameBuilderTest.buildRandomGame(false);
            Snakesandladders origXmlModel = new Bl2XmlMapper().convert(origBlModel, "test");
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(origXmlModel, tempFile);
            // printFile(tempFile);
            Snakesandladders newXmlModel = (Snakesandladders) jc.createUnmarshaller().unmarshal(tempFile);
            Game newBlModel = new Xml2BlMapper().convert(newXmlModel);
            Assert.assertEquals(origBlModel, newBlModel);
        }
    }

    private File makeTempFile() throws IOException {
        File tempFile = File.createTempFile("JaxB", "ComponentTest");
        tempFile.deleteOnExit();
        return tempFile;
    }

    private void printFile(File tempFile)  {
        try(BufferedReader r = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            log.error("error printing file", e);
        }
    }

}
