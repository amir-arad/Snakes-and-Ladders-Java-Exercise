package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.ControllerSession;
import arad.amir.ac.snlje.game.bl.GameManager;
import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private boolean waitingForPlayerChoice = false;

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
        } else if (type.equals(GameBoardCellController.class)){
            return new GameBoardCellController(session);
        } else {
            throw new IllegalArgumentException("unexpected controller type : " + type.getName());
        }
    }

    public Parent fxmlLoad(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this);
        fxmlLoader.setLocation(getClass().getClassLoader().getResource(fileName));
        Parent result = (Parent) fxmlLoader.load();
        result.setUserData(fxmlLoader.getController());
        return result;
    }

    public PrimaryStageController getPrimaryStageController() {
        return primaryStageController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void loadTurnData(){
        Player player = session.getManager().getCurrentPlayer();
        switch(player.getType()){
            case HUMAN:
                gameScreenController.loadTurnData();
                break;
            case COMPUTER:
                List <Integer> movementChoices = new ArrayList<>(session.getManager().getCurrentPlayerSoliderCellIndexes());
                int choice = movementChoices.get((int) (Math.random() * movementChoices.size()));
                GameManager.Move move = session.getManager().calcMove(choice);
                handlePlayerChoice(move);
                // TODO add pause
                break;
            default:
                throw new IllegalArgumentException("unknown player type : " + player);
        }
    }

    public void handleDieRoll(int dieRoll){
        waitingForPlayerChoice = true;
    }

    public void handlePlayerChoice(GameManager.Move move){
        waitingForPlayerChoice = false;
        session.getManager().executeMove(move);
        gameScreenController.handlePlayerChoice(move);
        loadTurnData();
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
