package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author amira
 * @since 09/08/2014
 */
public class AboutController extends AbsController {
    private static final Logger log = LoggerFactory.getLogger(AboutController.class);

    public AboutController(GameSession<ControllersManager> session) {
        super(session);
    }

    @FXML
    private void onClose(ActionEvent event) {
        getWindow(event).hide();
    }

    public static void showAbout(ControllersManager manager) throws IOException {
        final Stage aboutStage = new Stage();
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.initStyle(StageStyle.UTILITY);
        aboutStage.initOwner(manager.getPrimaryStage());
        aboutStage.setScene(new Scene(manager.fxmlLoad("About.fxml")));
        aboutStage.show();
    }

}
