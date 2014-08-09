package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.ControllerSession;
import arad.amir.ac.snlje.game.bl.GameSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

/**
 * @author amira
 * @since 09/08/2014
 */
public class ControllersManager implements Callback<Class<?>, Object>, ControllerSession {
    private static final Logger log = LoggerFactory.getLogger(ControllersManager.class);

    private final GameSession<ControllersManager> session;
    private final Stage primaryStage;

    private PrimaryStageController primaryStageController;

    public ControllersManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.session = new GameSession<>(this);
    }

    @Override
    public Object call(Class<?> type) {
        if (type.equals(PrimaryStageController.class)){
            primaryStageController = new PrimaryStageController(session);
            return primaryStageController;
        } else if (type.equals(AboutController.class)){
            return new AboutController(session);
        } else {
            throw new IllegalArgumentException("unexpected controller type : " + type.getName());
        }
    }

    public Scene fxmlLoad(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this);
        Parent load = (Parent) fxmlLoader.load(getClass().getClassLoader().getResourceAsStream(fileName));
        return new Scene(load);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void printErrors(String error, Collection<String> errors) {
        primaryStageController.printErrors(error, errors);
    }

    @Override
    public void printNewError(String error) {
        primaryStageController.printNewError(error);
    }

    @Override
    public void clearError() {
        primaryStageController.clearError();
    }
}
