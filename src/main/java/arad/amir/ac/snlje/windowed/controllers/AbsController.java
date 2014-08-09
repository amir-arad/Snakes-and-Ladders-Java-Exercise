package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameSession;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 09/08/2014
 */
abstract class AbsController {
    private static final Logger log = LoggerFactory.getLogger(AbsController.class);
    protected final GameSession<ControllersManager> session;

    protected AbsController(GameSession<ControllersManager> session) {
        this.session = session;
    }

    protected Window getWindow(ActionEvent event) {
        return ((Node) (event.getSource())).getScene().getWindow();
    }

    protected Stage getMainWindow() {
        return session.getController().getPrimaryStage();
    }
}
