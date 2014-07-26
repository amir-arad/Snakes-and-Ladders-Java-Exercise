package arad.amir.ac.snlje.xml.mapper;

import arad.amir.ac.snlje.game.model.*;
import arad.amir.ac.snlje.xml.model.Ladders;
import arad.amir.ac.snlje.xml.model.Players;
import arad.amir.ac.snlje.xml.model.Snakes;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Xml2BlMapper {
    private static final Logger log = LoggerFactory.getLogger(Xml2BlMapper.class);

    public Game convert(Snakesandladders source){
        Game result = new Game();
        List<Cell> cells = makeCells(source.getBoard().getSize() * source.getBoard().getSize());
        List<Player> players = new ArrayList<>(source.getPlayers().getPlayer().size());
        result.setPlayers(players);
        result.setBoard(convertBoard(source.getBoard()));
        result.getBoard().setCells(cells);
        result.setNumberOfSoldiersToWin(source.getNumberOfSoldiers());
        result.getBoard().setPassages(new ArrayList<Passage>(
                source.getBoard().getSnakes().getSnake().size() +
                        source.getBoard().getLadders().getLadder().size()));
        for (Players.Player player : source.getPlayers().getPlayer()) {
            result.getPlayers().add(convertPlayer(player));
        }
        result.setCurrentTurn(players.indexOf(getDestPlayerByName(source.getCurrentPlayer(), players)));
        for (arad.amir.ac.snlje.xml.model.Cell cell : source.getBoard().getCells().getCell()) {
            Cell destinationCell = cells.get(convertCellId(cell.getNumber()));
            convertSoliders(players, destinationCell, cell.getSoldiers());
        }
        for (Snakes.Snake snake : source.getBoard().getSnakes().getSnake()) {
            result.getBoard().getPassages().add(convertSnake(snake, cells));
        }
        for (Ladders.Ladder ladder : source.getBoard().getLadders().getLadder()) {
            result.getBoard().getPassages().add(convertLadder(ladder, cells));
        }
        return result;
    }

    private List<Cell> makeCells(int numOfCells) {
        List<Cell> result = new ArrayList<>(numOfCells);
        for (int i = 0; i < numOfCells; i++) {
            Cell c = new Cell();
            c.setIndex(i);
            result.add(c);
        }
        return result;
    }

    private Board convertBoard(arad.amir.ac.snlje.xml.model.Board source) {
        Board result = new Board();
        result.setSize(source.getSize());
        return result;
    }

    private Player convertPlayer(Players.Player source) {
        Player result = new Player();
        result.setName(source.getName());
        result.setType(Player.Type.valueOf(source.getType().name()));
        result.setSoldierPositions(new ArrayList<Cell>(Game.NUMBER_OF_SOLDIERS)); // populated in convertSoliders
        return result;
    }

    private void convertSoliders(List<Player> players, Cell destinationCell, List<arad.amir.ac.snlje.xml.model.Cell.Soldiers> soldiersList) {
        for (arad.amir.ac.snlje.xml.model.Cell.Soldiers soldiers : soldiersList) {
            Player soldiersPlayer = getDestPlayerByName(soldiers.getPlayerName(), players);
            for (int i = 0; i < soldiers.getCount(); i++) {
                soldiersPlayer.getSoldierPositions().add(destinationCell);
            }
        }
    }

    private int convertCellId(BigInteger source) {
        return source.intValue() - 1;
    }

    private Player getDestPlayerByName(String playerName, List<Player> players) {
        for (Player player : players) {
            if (player.getName().equals(playerName)){
                return player;
            }
        }
        throw new IllegalStateException("player should be present : '" + playerName + "' not in " + players);
    }

    private Passage convertLadder(Ladders.Ladder source, List<Cell> cells) {
        Passage passage = new Passage();
        passage.setType(Passage.Type.LADDER);
        passage.setFrom(cells.get(convertCellId(source.getFrom())));
        passage.setTo(cells.get(convertCellId(source.getTo())));
        return passage;
    }

    private Passage convertSnake(Snakes.Snake source, List<Cell> cells) {
        Passage passage = new Passage();
        passage.setType(Passage.Type.SNAKE);
        passage.setFrom(cells.get(convertCellId(source.getFrom())));
        passage.setTo(cells.get(convertCellId(source.getTo())));
        return passage;
    }
}
