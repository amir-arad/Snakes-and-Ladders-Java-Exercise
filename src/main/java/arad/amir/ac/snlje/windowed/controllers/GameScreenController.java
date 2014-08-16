package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Passage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author amira
 * @since 12/08/2014
 */
public class GameScreenController extends AbsController implements Initializable{
    private static final Logger log = LoggerFactory.getLogger(GameScreenController.class);
    private static final String DIE_DEFAULT_TEXT = "Roll";

    @FXML
    private AnchorPane board;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Button actionButton;
    private GridPane boardGrid;

    protected GameScreenController(GameSession<ControllersManager> session) {
        super(session);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardGrid = new GridPane();
        for (int i = 0; i < session.getGame().getBoard().getSize(); i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(80);
            boardGrid.getColumnConstraints().add(col);
        }
        boardGrid.addColumn();
        board.getChildren().add(boardGrid);
    }

    public void loadTurnData(){
        playerNameLabel.setText(session.getManager().getCurrentPlayer().getName());
        actionButton.setText(DIE_DEFAULT_TEXT);
        actionButton.setDisable(false);
    }

    @FXML
    private synchronized void onDieAction(ActionEvent actionEvent) {
        if (!actionButton.isDisabled()) {
            int result = session.getManager().rollDie();
            actionButton.setText(Integer.toString(result));
            actionButton.setDisable(true);
            session.getController().handleDieRoll(result);
        }
    }

    public void handlePlayerChoice(Passage passage) {
        if (passage == null) {
            infoLabel.setText("");
        } else {
            infoLabel.setText(playerNameLabel.getText() +
                    "'s solider has just passed through a " + passage.getType().toString().toLowerCase());
        }
    }

}
