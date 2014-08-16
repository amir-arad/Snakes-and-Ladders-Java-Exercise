package arad.amir.ac.snlje.game.bl;

import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Passage;
import arad.amir.ac.snlje.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author amira
 * @since 26/07/2014
 */
public class GameManager {
    private static final Logger log = LoggerFactory.getLogger(GameManager.class);
    public static final int MAX_DIE_ROLL = 6;
    public static final int MIN_DIE_ROLL = 1;
    public static final int NULL_DIE_ROLL = -1;

    private Random r = new Random();
    private int dieRoll;
    private final Game game;
    private int lastCellNumber;
    private Player winner;

    public static class Move {
        private final Player who;
        private final Cell from;
        private final Passage through;
        private final Cell to;

        public Move(Player who, Cell from, Passage through, Cell to) {
            this.who = who;
            this.from = from;
            this.through = through;
            this.to = to;
        }

        public Player getWho() {
            return who;
        }

        public Cell getFrom() {
            return from;
        }

        public Passage getThrough() {
            return through;
        }

        public Cell getTo() {
            return to;
        }
    }

    public GameManager(Game game) {
        this.game = game;
        lastCellNumber = game.getBoard().getCells().size();
        dieRoll = NULL_DIE_ROLL;
        calculateWinner();
    }

    public int rollDie(){
        if (dieRoll == NULL_DIE_ROLL){
            dieRoll = r.nextInt(MAX_DIE_ROLL - MIN_DIE_ROLL + 1) + MIN_DIE_ROLL;
        }
        return dieRoll;
    }

    public Collection<Integer> getCurrentPlayerSoliderCellIndexes(){
        Player player = getCurrentPlayer();
        Set<Integer> result = new HashSet<>(player.getSoldierPositions().size());
        for (Cell cell : player.getSoldierPositions()) {
            if (cell.getNumber() != lastCellNumber) {
                result.add(cell.getIndex());
            }
        }
        return result;
    }

    public Move calcMove(int cellIndexOfSoliderToMove){
        if (winner != null){
            throw new IllegalStateException("game already won by " + winner.getName());
        }
        Cell cellOfSoliderToMove = game.getBoard().getCells().get(cellIndexOfSoliderToMove);
        Player player = getCurrentPlayer();
        if (!player.getSoldierPositions().contains(cellOfSoliderToMove)){
            throw new IllegalArgumentException("wrong soldier cell index " + cellIndexOfSoliderToMove + " for player " + player);
        }
        int newPositionIndex = Math.min(
                cellIndexOfSoliderToMove + dieRoll,
                lastCellNumber - 1);
        Cell destination = game.getBoard().getCells().get(newPositionIndex);
        Passage p = destination.getToPassage();
        if (p != null){
            destination = p.getTo();
        }
        return new Move(player, cellOfSoliderToMove, p, destination);
    }

    public void executeMove(Move move){
        move.getWho().getSoldierPositions().remove(move.getFrom());
        move.getWho().getSoldierPositions().add(move.getTo());
        dieRoll = NULL_DIE_ROLL;
        calculateWinner();
        game.setCurrentTurn((game.getCurrentTurn() + 1) % game.getPlayers().size());
    }

    public Player getCurrentPlayer() {
        return game.getPlayers().get(game.getCurrentTurn());
    }

    private void calculateWinner(){
        for (Player player : game.getPlayers()) {
            int numOfSolidersInLastCell = 0;
            for (Cell cell : player.getSoldierPositions()) {
                if (cell.getNumber() == lastCellNumber) {
                    numOfSolidersInLastCell++;
                }
            }
            if (numOfSolidersInLastCell >= game.getNumberOfSoldiersToWin()) {
                winner = player;
                return;
            }
        }
    }
    public Player getWinner(){
        return winner;
    }

}
