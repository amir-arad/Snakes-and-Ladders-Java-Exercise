package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameManager;
import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Passage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author amira
 * @since 12/08/2014
 */
public class GameScreenController extends AbsController implements Initializable{
    private static final Logger log = LoggerFactory.getLogger(GameScreenController.class);
    private static final String DIE_DEFAULT_TEXT = "Roll";
    public static final int MIN_CELL_WIDTH = 80;
    public static final int MIN_CELL_HEIGHT = 80;
    private static final boolean SNAKE_STYLE_ORDERING = true;

    @FXML
    private AnchorPane board;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Button actionButton;
    private GridPane boardGrid;
    private List<GameBoardCellController> cellControllers;

    protected GameScreenController(GameSession<ControllersManager> session) {
        super(session);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardGrid = new GridPane();
        int boardSize = session.getGame().getBoard().getSize();
        for (int i = 0; i < boardSize; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(MIN_CELL_WIDTH);
            boardGrid.getColumnConstraints().add(col);
            RowConstraints row = new RowConstraints();
            row.setMinHeight(MIN_CELL_HEIGHT);
            boardGrid.getRowConstraints().add(row);
        }
        List<Cell> blCells = session.getGame().getBoard().getCells();
        cellControllers = new ArrayList<>(blCells.size());
        try {
            for (Cell cell : blCells) {
                Parent cellUi = session.getController().fxmlLoad("BoardCell.fxml");
                GameBoardCellController cellController = (GameBoardCellController) cellUi.getUserData();
                int rowIndex = cell.getNumber() / boardSize;
                GridPane.setRowIndex(cellUi, boardSize - rowIndex);
                int colIndex = cell.getNumber() % boardSize;
                if (SNAKE_STYLE_ORDERING && rowIndex%2 == 1){
                    colIndex = boardSize - colIndex;
                }
                GridPane.setColumnIndex(cellUi, colIndex);
                cellControllers.add(cellController);
                boardGrid.getChildren().add(cell.getIndex(), cellUi);
                cellController.init(cell, cellControllers, rowIndex, colIndex);
            }
        } catch (IOException e) {
            log.error("error creating cell UI", e);
        }
        boardGrid.requestLayout();
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

    public void handlePlayerChoice(GameManager.Move move) {
        Passage passage = move.getThrough();
        if (passage != null) {
            infoLabel.setText(infoLabel.getText() + "\n" +
                    playerNameLabel.getText() +
                    "'s solider has just passed through a " + passage.getType().toString().toLowerCase());
        }
        cellControllers.get(move.getFrom().getIndex()).calcSoliders(move.getWho());
        cellControllers.get(move.getTo().getIndex()).calcSoliders(move.getWho());
    }
}
