package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.List;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public void printGame(Game game, PrintStream out){
        StringBuilder sb = new StringBuilder();
        int size = game.getBoard().getSize();
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                Cell cell = game.getBoard().getCells().get(row * size + col);
                printCell(cell, game.getPlayers(), sb);
                sb.append(' ');
            }
            sb.append(System.getProperty("line.separator"));
        }
        printLine(sb, size * (6 + game.getPlayers().size()) + (size - 1));
        sb.append(System.getProperty("line.separator"));
        printPlayers(sb, game.getPlayers());
        sb.append(System.getProperty("line.separator"));
        out.append(sb);
    }

    private void printLine(StringBuilder sb, int lineWidth) {
        for (int i = 0; i < lineWidth; i++) {
            sb.append('-');
        }
    }

    private void printPlayers(StringBuilder sb, List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            sb.append("Player ").append(i + 1).append(": ")
                    .append(players.get(i).getName()).append('\t');
        }
    }

    private void printCell(Cell cell, List<Player> players, StringBuilder sb) {
        sb.append(formatCellNumber(cell)).append('|');
        sb.append(cell.getToPassage() == null ?
                "--" :
                formatCellNumber(cell.getToPassage().getTo()))
                .append('|');
        for (Player player : players) {
            sb.append(player.getSolidersCountInCell(cell));
        }

    }
    private String formatCellNumber(Cell cell) {
        return String.format("%02d", cell.getNumber());
    }
}
