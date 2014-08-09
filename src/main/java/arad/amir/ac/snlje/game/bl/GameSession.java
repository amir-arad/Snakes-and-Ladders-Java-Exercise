package arad.amir.ac.snlje.game.bl;

import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.xml.mapper.Bl2XmlMapper;
import arad.amir.ac.snlje.xml.mapper.Xml2BlMapper;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import java.io.File;

/**
 * @author amira
 * @since 26/07/2014
 */
public class GameSession<T extends ControllerSession> {
    private static final Logger log = LoggerFactory.getLogger(GameSession.class);

    private Game game;
    private GameManager manager;
    private T controller;
    private File lastSave;

    public GameSession() {
    }

    public GameSession(T controller) {
        this.controller = controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public Game getGame() {
        return game;
    }

    private void setGame(Game game) {
        this.game = game;
        manager = new GameManager(game);
    }

    public GameManager getManager() {
        return manager;
    }

    public T getController() {
        return controller;
    }

    public File getLastSave() {
        return lastSave;
    }

    public boolean loadGame(File loadFrom) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
            Snakesandladders origXmlModel = (Snakesandladders) jc.createUnmarshaller().unmarshal(loadFrom);
            Game game = new Xml2BlMapper().convert(origXmlModel);
            if (!GameValidator.validateGame(game, getController())) {
                return false;
            }
            setGame(game);
            lastSave = loadFrom;
        } catch (UnmarshalException e) {
            getController().printNewError("Not a legal game file");
            log.error("error loading game from xml", e);
            return false;
        }catch (JAXBException e) {
            getController().printNewError("game was not loaded.");
            log.error("error loading game from xml", e);
            return false;
        }
        return true;
    }

    public boolean newGame(GameBuilder builder) {
        Game game = builder.build();
        if (!GameValidator.validateGame(game, getController())) {
            return false;
        }
        setGame(game);
        return true;
    }

    public void saveGame(File saveTo) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new Bl2XmlMapper().convert(getGame(), saveTo.getName()), saveTo);
            lastSave = saveTo;
        } catch (JAXBException e) {
            getController().printNewError("game was not saved.");
            log.error("error saving game to xml", e);
        }
    }
}
