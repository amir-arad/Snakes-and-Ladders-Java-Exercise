package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Displayer {
    private static final Logger log = LoggerFactory.getLogger(Displayer.class);

    private Scanner scanner = new Scanner(System.in);


    public void pause() {
        System.out.print("press enter to continue...");
        scanner.nextLine();
    }

    public int menu(String title, String... options){
        System.out.println(title);
        displayMenu(options);
        return inputInteger("your choice:", 1, options.length) - 1;
    }

    public int inputInteger(String message, int min, int max) {
        System.out.print(message);
        System.out.print("("+min+" to "+max+") : ");
        try {
            int result = scanner.nextInt();
            scanner.nextLine();    // consume the \n char
            if (result >= min && result <= max) {
                return result;
            }
        } catch(InputMismatchException e){}
        System.out.print("please choose a valid number");
        return inputInteger(message, min, max);
    }

    public String inputString(String message) {
        System.out.print(message);
        System.out.print(" : ");
        String result = scanner.next();
        return result;
    }

    private void displayMenu(String[] options) {
        String leftAlignFormat = "|% -2d | %-17s |%n";
        System.out.format("+---+-------------------+%n");
        for (int i = 0; i < options.length; i++) {
            System.out.format(leftAlignFormat, i + 1, options[i]);
        }
        System.out.format("+---+-------------------+%n");
    }

    public void printGame(Game game){
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
        System.out.append(sb);
    }


    public void printTitle(String title) {
        System.out.flush();
        System.out.println();
        System.out.println(title);
        printLine(title.length());
        System.out.println();
    }


    public void printLine(String line) {
        System.out.println(line);
    }


    public void printLine(int lineWidth) {
        StringBuilder sb = new StringBuilder();
        printLine(sb, lineWidth);
        System.out.append(sb);
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
