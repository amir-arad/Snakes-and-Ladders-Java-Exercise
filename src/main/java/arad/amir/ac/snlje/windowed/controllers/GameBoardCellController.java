package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameManager;
import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Player;
import arad.amir.ac.snlje.windowed.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author amira
 * @since 16/08/2014
 */
public class GameBoardCellController extends AbsController{
    private static final Logger log = LoggerFactory.getLogger(GameBoardCellController.class);
    private static final String FX_BACKGROUND_COLOR_EVEN = "-fx-background-color: seashell;";
    private static final String FX_BACKGROUND_COLOR_ODD = "-fx-background-color: darkkhaki;";
    private static final String FX_BACKGROUND_COLOR_HIGHLIGHT = "-fx-background-color: orangered";
    private static final int NUM_OF_ROWS = 4;

    @FXML
    private AnchorPane cellPane;
    @FXML
    private ImageView solider00;
    @FXML
    private ImageView solider01;
    @FXML
    private ImageView solider02;
    @FXML
    private ImageView solider03;
    @FXML
    private ImageView solider10;
    @FXML
    private ImageView solider11;
    @FXML
    private ImageView solider12;
    @FXML
    private ImageView solider13;
    @FXML
    private ImageView solider20;
    @FXML
    private ImageView solider21;
    @FXML
    private ImageView solider22;
    @FXML
    private ImageView solider23;
    @FXML
    private ImageView solider30;
    @FXML
    private ImageView solider31;
    @FXML
    private ImageView solider32;
    @FXML
    private ImageView solider33;

    private Cell cell;
    private List<GameBoardCellController> otherCells;
    private List<Set<ImageView>> cellsByRow = new ArrayList<>(NUM_OF_ROWS);
    private List<Set<ImageView>> cellsByPlayer;
    private boolean bgStyleEven;

    protected GameBoardCellController(GameSession<ControllersManager> session) {
        super(session);
    }

    public void init(Cell cell, List<GameBoardCellController> cellControllers, int rowIndex, int colIndex) {

        this.cell = cell;
        otherCells = cellControllers;
        // index cells by row
        cellsByRow.add(new HashSet<>(Arrays.asList(solider00, solider10, solider20, solider30)));
        cellsByRow.add(new HashSet<>(Arrays.asList(solider01, solider11, solider21, solider31)));
        cellsByRow.add(new HashSet<>(Arrays.asList(solider02, solider12, solider22, solider32)));
        cellsByRow.add(new HashSet<>(Arrays.asList(solider03, solider13, solider23, solider33)));
        // index cells by player
        cellsByPlayer = new ArrayList<>(session.getGame().getPlayers().size());
        for (int playerIdx = 0; playerIdx < session.getGame().getPlayers().size(); playerIdx++) {
            cellsByPlayer.add(new HashSet<ImageView>());
            calcSoliders(session.getGame().getPlayers().get(playerIdx));
        }

        bgStyleEven = (rowIndex + colIndex) % 2 == 0;
    }


    @FXML
    private void mouseClick(MouseEvent mouseEvent){
        if (!cellsByPlayer.get(session.getGame().getCurrentTurn()).isEmpty()){
            GameManager.Move move = session.getManager().calcMove(cell.getIndex());
            session.getController().handlePlayerChoice(move);
        }
    }

    @FXML
    private void mouseEntered(MouseEvent mouseEvent){
        if (!cellsByPlayer.get(session.getGame().getCurrentTurn()).isEmpty()){
            GameManager.Move move = session.getManager().calcMove(cell.getIndex());
            otherCells.get(move.getTo().getIndex()).highlight(true);
        }
    }

    private void highlight(boolean on) {
        if (on){
            cellPane.setStyle(FX_BACKGROUND_COLOR_HIGHLIGHT);
        } else {
            cellPane.setStyle(bgStyleEven ? FX_BACKGROUND_COLOR_EVEN : FX_BACKGROUND_COLOR_ODD);
        }
    }

    @FXML
    private void mouseExited(MouseEvent mouseEvent){
        if (!cellsByPlayer.get(session.getGame().getCurrentTurn()).isEmpty()){
            GameManager.Move move = session.getManager().calcMove(cell.getIndex());
            otherCells.get(move.getTo().getIndex()).highlight(false);
        }
    }

    public void calcSoliders(Player player) {
        int playerIdx = session.getGame().getPlayers().indexOf(player);
        PlayerColor color = PlayerColor.values()[playerIdx];
        Set<ImageView> soliders = cellsByPlayer.get(playerIdx);
        for (ImageView solider : soliders) {
            solider.setImage(null);
        }
        soliders.clear();
        int count = player.getSolidersCountInCell(cell);
        for (int soliderIdx = 0; soliderIdx < count; soliderIdx++) {
            for (ImageView soliderSpotInRow : cellsByRow.get(soliderIdx)) {
                if (soliderSpotInRow.getImage() == null){
                    soliderSpotInRow.setImage(color.getImage());
                    soliders.add(soliderSpotInRow);
                    break;
                }
            }
        }
    }
}
