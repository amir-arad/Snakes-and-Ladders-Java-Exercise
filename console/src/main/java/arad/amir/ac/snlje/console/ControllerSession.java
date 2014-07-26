package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.bl.GameManager;
import arad.amir.ac.snlje.game.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author amira
 * @since 26/07/2014
 */
public class ControllerSession {
    private static final Logger log = LoggerFactory.getLogger(ControllerSession.class);

    private Game game;
    private GameManager manager;
    private Displayer displayer;
    private File lastSave;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        manager = new GameManager(game);
    }

    public GameManager getManager() {
        return manager;
    }

    public Displayer getDisplayer() {
        return displayer;
    }

    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
    }

    public File getLastSave() {
        return lastSave;
    }

    public void setLastSave(File lastSave) {
        this.lastSave = lastSave;
    }
}
