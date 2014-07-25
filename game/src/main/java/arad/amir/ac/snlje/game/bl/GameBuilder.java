package arad.amir.ac.snlje.game.bl;

import arad.amir.ac.snlje.game.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author amira
 * @since 25/07/2014
 */
public class GameBuilder {
    private static final Logger log = LoggerFactory.getLogger(GameBuilder.class);
    private int boardSize;
    private int numOfPassages;
    private int winningCondition;
    private List<Player> players;
    private boolean newGame = true;

    public void setNewGame(boolean newGame) {
        this.newGame = newGame;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setNumOfPassages(int numOfPassages) {
        this.numOfPassages = numOfPassages;
    }

    public void setWinningCondition(int winningCondition) {
        this.winningCondition = winningCondition;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }


    public Game build() {
        int numOfCells = this.boardSize * boardSize;
        List<Cell> cells = new ArrayList<>(numOfCells);
        for (int i = 0; i < numOfCells; i++) {
            Cell cell = new Cell();
            cell.setIndex(i);
            cells.add(cell);
        }
        int totalNumOfPassages = numOfPassages * Passage.Type.values().length;
        Collection<Passage> passages = new HashSet<>(totalNumOfPassages);
        List<Cell> cellsForPassages = new ArrayList<>(cells);
        cellsForPassages.remove(numOfCells - 1);
        cellsForPassages.remove(0);
        for (int i = 0; i < totalNumOfPassages; i++) {
            Passage passage = makePassage(cellsForPassages, Passage.Type.values()[i % 2]);
            passages.add(passage);
        }
        Board board = new Board();
        board.setSize(boardSize);
        board.setCells(cells);
        board.setPassages(passages);
        for (Player player : this.players) {
            initPlayer(cells, player);
        }
        Game game = new Game();
        game.setBoard(board);
        game.setPlayers(players);
        game.setCurrentTurn(0);
        game.setNumberOfSoldiersToWin(this.winningCondition);
        return game;
    }

    private int randInRange(int min, int max) {
        return (int) (min + Math.round(Math.random() * (max - min)));
    }

    private Cell getRandomStoppingCell(List<Cell> cells) {
        Cell cell;
        do {
            cell = cells.get(randInRange(0, cells.size() - 1));
        } while (cell.getToPassage() != null);
        return cell;
    }

    private void initPlayer(List<Cell> cells, Player player) {
        List<Cell> soldierPositions = new ArrayList<>(Game.NUMBER_OF_SOLDIERS);
        for (int i = 0; i < Game.NUMBER_OF_SOLDIERS; i++) {
            Cell startingPoint = newGame ? cells.iterator().next() : getRandomStoppingCell(cells);
            soldierPositions.add(startingPoint);
        }
        player.setSoldierPositions(soldierPositions);
    }

    private Passage makePassage(List<Cell> cells, Passage.Type type) {
        Passage passage = new Passage();
        passage.setType(type);
        Cell cell1;
        Cell cell2;
        do {
            cell1 = getRandomStoppingCell(cells);
            cell2 = getRandomStoppingCell(cells);
        } while (cell1.equals(cell2));
        passage.setHigh(cell1.getIndex() > cell2.getIndex() ? cell1 : cell2);
        passage.setLow(cell1.getIndex() < cell2.getIndex() ? cell1 : cell2);
        cells.remove(cell1);
        cells.remove(cell2);
        return passage;
    }
}
