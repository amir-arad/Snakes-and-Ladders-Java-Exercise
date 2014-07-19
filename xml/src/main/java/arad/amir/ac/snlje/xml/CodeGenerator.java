package arad.amir.ac.snlje.xml;

import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

/**
 * @author amira
 * @since 19/07/2014
 */
public class CodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(CodeGenerator.class);


    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();

        try (InputStream fis = CodeGenerator.class.getClassLoader().getResourceAsStream("snakesandladders.xml")) {
            System.out.println("Total file size to read (in bytes) : " + fis.available());
            JAXBElement<Snakesandladders> feed = unmarshaller.unmarshal(new StreamSource(fis), Snakesandladders.class);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(feed, System.out);

        } catch (Exception e) {
            log.error("CodeGenerator error", e);
        }

    }
}
