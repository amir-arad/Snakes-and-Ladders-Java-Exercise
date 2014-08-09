package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.ControllerSession;
import arad.amir.ac.snlje.game.bl.GameSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PrimaryStageController extends AbsController implements ControllerSession {
    private static final Logger log = LoggerFactory.getLogger(PrimaryStageController.class);

    @FXML
    private AnchorPane mainScreen;

    @FXML
    private MenuBar topMenu;

    @FXML
    private Label errorMessageLabel;

    protected PrimaryStageController(GameSession<ControllersManager> session) {
        super(session);
    }


    public void handleTopMenuAbout(ActionEvent actionEvent) throws IOException {
        AboutController.showAbout(session.getController());
    }

    public void handleNew(ActionEvent actionEvent) {
        //todo implement. To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public void printErrors(String error, Collection<String> errors) {
        printNewError(error + '\n' + StringUtils.join(errors, '\n'));
    }

    @Override
    public void printNewError(String error){
        errorMessageLabel.setText(error);
    }

    @Override
    public void clearError(){
        errorMessageLabel.setText(null);
    }

    public void handleLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        File file = fileChooser.showOpenDialog(getMainWindow());
        session.loadGame(file);
        // TODO load game view
    }

    public void handleSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        File file = fileChooser.showOpenDialog(getMainWindow());
        session.loadGame(file);
    }

    public void handleExit(ActionEvent actionEvent) {
        getMainWindow().close();
    }
}
