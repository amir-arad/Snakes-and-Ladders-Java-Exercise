package arad.amir.ac.snlje.game.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author amira
 * @since 18/07/2014
 */
public class Game {
    private static final Logger log = LoggerFactory.getLogger(Game.class);
    public static final int NUMBER_OF_SOLDIERS = 4;
    public static final int MIN_WINNING_CONDITION = 1;
    public static final int MAX_WINNING_CONDITION = 4;
    public static final int MIN_NUM_OF_PLAYERS = 2;
    public static final int MAX_NUM_OF_PLAYERS = 4;

    private List<Player> players;
    private Board board;
    private int currentTurn;
    private int numberOfSoldiersToWin;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getNumberOfSoldiersToWin() {
        return numberOfSoldiersToWin;
    }

    public void setNumberOfSoldiersToWin(int numberOfSoldiersToWin) {
        this.numberOfSoldiersToWin = numberOfSoldiersToWin;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Game rhs = (Game) obj;
        return new EqualsBuilder()
                .append(this.players, rhs.players)
                .append(this.board, rhs.board)
                .append(this.currentTurn, rhs.currentTurn)
                .append(this.numberOfSoldiersToWin, rhs.numberOfSoldiersToWin)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(players)
                .append(board)
                .append(currentTurn)
                .append(numberOfSoldiersToWin)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("players", players)
                .append("board", board)
                .append("currentTurn", currentTurn)
                .append("numberOfSoldiersToWin", numberOfSoldiersToWin)
                .toString();
    }
}
