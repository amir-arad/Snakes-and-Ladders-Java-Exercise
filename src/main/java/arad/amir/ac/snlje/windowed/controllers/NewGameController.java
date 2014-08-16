package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameBuilder;
import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Board;
import arad.amir.ac.snlje.game.model.Player;
import arad.amir.ac.snlje.windowed.PlayerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author amira
 * @since 14/08/2014
 */
public class NewGameController extends AbsController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(NewGameController.class);

    @FXML
    private Button startGameButton;
    @FXML
    private Slider winningConditions;
    @FXML
    private NewPlayerController bluePlayerDefController;
    @FXML
    private NewPlayerController purplePlayerDefController;
    @FXML
    private NewPlayerController greenPlayerDefController;
    @FXML
    private NewPlayerController redPlayerDefController;

    private Map<PlayerColor, NewPlayerController> controllerMap = new EnumMap<>(PlayerColor.class);

    @FXML
    private Slider numberOfPassages;

    @FXML
    private ToggleGroup boardSize;

    protected NewGameController(GameSession<ControllersManager> session) {
        super(session);
    }

    private void checkEligbleForStartGame() {
        int numOfEnabledPlayers = 0;
        int numOfHumanPlayers = 0;
        for (NewPlayerController newPlayerController : controllerMap.values()) {
            Player player = newPlayerController.getPlayer();
            if (player != null){
                ++numOfEnabledPlayers;
                if (player.getType() == Player.Type.HUMAN){
                    ++numOfHumanPlayers;
                }
            }
        }
        startGameButton.setDisable(numOfEnabledPlayers < 2 || numOfHumanPlayers <= 0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controllerMap.put(PlayerColor.BLUE, bluePlayerDefController);
        bluePlayerDefController.init(this, PlayerColor.BLUE);
        controllerMap.put(PlayerColor.PURPLE, purplePlayerDefController);
        purplePlayerDefController.init(this, PlayerColor.PURPLE);
        controllerMap.put(PlayerColor.GREEN, greenPlayerDefController);
        greenPlayerDefController.init(this, PlayerColor.GREEN);
        controllerMap.put(PlayerColor.RED, redPlayerDefController);
        redPlayerDefController.init(this, PlayerColor.RED);
        enableColorPlayer(PlayerColor.values()[0], true);
    }

    private void enableColorPlayer(PlayerColor color, boolean enabled) {
        controllerMap.get(color).setEnabled(enabled);
    }

    @FXML
    private void onBoardSize(ActionEvent actionEvent) {
        numberOfPassages.setMax(Board.calcMaxPassages(getBoardSizeValue()));
    }

    private int getBoardSizeValue() {
        return Integer.valueOf(((RadioButton) boardSize.getSelectedToggle()).getId());
    }

    @FXML
    private void onStartGame(ActionEvent actionEvent) throws IOException {
        GameBuilder builder = new GameBuilder();
        builder.setNewGame(true);
        builder.setBoardSize(getBoardSizeValue());
        builder.setNumOfPassages((int)numberOfPassages.getValue());
        builder.setWinningCondition((int) winningConditions.getValue());
        builder.setPlayers(new ArrayList<Player>(PlayerColor.values().length));
        for (NewPlayerController newPlayerController : controllerMap.values()) {
            Player player = newPlayerController.getPlayer();
            if (player != null) {
                builder.getPlayers().add(player);
            }
        }
        if (session.newGame(builder)){
            session.getController().getPrimaryStageController().changeToGameScreen();
        }
    }

    public void handlePlayerActive(PlayerColor color, boolean valid) {
        PlayerColor next = color.next();
        if (next != null ){
            enableColorPlayer(next, valid);
        }
        checkEligbleForStartGame();
    }
}
