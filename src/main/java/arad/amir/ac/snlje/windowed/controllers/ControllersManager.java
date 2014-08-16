package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.ControllerSession;
import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Passage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private GameScreenController gameScreenController;

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
        } else if (type.equals(GameScreenController.class)){
            gameScreenController = new GameScreenController(session);
            return gameScreenController;
        } else if (type.equals(NewGameController.class)){
            return new NewGameController(session);
        } else if (type.equals(NewPlayerController.class)){
            return new NewPlayerController(session);
        } else {
            throw new IllegalArgumentException("unexpected controller type : " + type.getName());
        }
    }

    public Parent fxmlLoad(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this);
        fxmlLoader.setLocation(getClass().getClassLoader().getResource(fileName));
        return (Parent) fxmlLoader.load();
    }

    public PrimaryStageController getPrimaryStageController() {
        return primaryStageController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void loadTurnData(){
        gameScreenController.loadTurnData();
     // todo load current player data to UI
    }

    public void handleDieRoll(int dieRoll){
        // todo handleDieRoll
    }

    public void handlePlayerChoice(int cellIndex){
        Passage passage = session.getManager().playTurn(cellIndex);
        gameScreenController.handlePlayerChoice(passage);
        // TODO handle player choice
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
