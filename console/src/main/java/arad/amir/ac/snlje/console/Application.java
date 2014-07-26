package arad.amir.ac.snlje.console;

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
        ControllerSession session = new ControllerSession();
        session.setDisplayer(new Displayer());
        ControllerStage stage = ControllerStage.CHOOSE_GAME;
        session.getDisplayer().printTitle(WELCOME_MSG);
        session.getDisplayer().pause();
        while (stage != null){
            stage = stage.execute(session);
        }
        session.getDisplayer().printTitle("Thank you for playing!");
        session.getDisplayer().pause();
    }
}
