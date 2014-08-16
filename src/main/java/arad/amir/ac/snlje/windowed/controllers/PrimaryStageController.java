package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.ControllerSession;
import arad.amir.ac.snlje.game.bl.GameSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
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
    private VBox mainScreen;

    @FXML
    private MenuBar topMenu;

    @FXML
    private Label errorMessageLabel;

    protected PrimaryStageController(GameSession<ControllersManager> session) {
        super(session);
    }


    @FXML
    private void handleTopMenuAbout(ActionEvent actionEvent) throws IOException {
        AboutController.showAbout(session.getController());
    }

    @FXML
    private void handleNew(ActionEvent actionEvent) throws IOException {
        Parent gameForm = session.getController().fxmlLoad("NewGame.fxml");
        setMainScreen(gameForm);
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

    @FXML
    private void handleLoad(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        File file = fileChooser.showOpenDialog(getMainWindow());
        session.loadGame(file);
        changeToGameScreen();
    }

    public void changeToGameScreen() throws IOException {
        Parent gameScreen = session.getController().fxmlLoad("GameScreen.fxml");
        setMainScreen(gameScreen);
    }

    private void setMainScreen(final Parent root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                root.requestLayout();
                mainScreen.getChildren().clear();
                mainScreen.getChildren().add(root);
                log.info("main screen changed");
            }
        });
    }

    @FXML
    private void handleSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        File file = fileChooser.showSaveDialog(getMainWindow());
        session.saveGame(file);
    }

    @FXML
    private void handleExit(ActionEvent actionEvent) {
        getMainWindow().close();
    }
}
