package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.bl.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static final String WELCOME_MSG = "Welcome to Snakes and Ladders!";

    public static void main(String[] args) {
        GameSession<Displayer> session = new GameSession<>(new Displayer());
        ControllerStage stage = ControllerStage.CHOOSE_GAME;
        session.getController().printTitle(WELCOME_MSG);
        session.getController().pause();
        while (stage != null){
            stage = stage.execute(session);
        }
        session.getController().printTitle("Thank you for playing!");
        session.getController().pause();
    }
}
