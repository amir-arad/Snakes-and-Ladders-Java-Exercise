package arad.amir.ac.snlje.xml.mapper;

import arad.amir.ac.snlje.game.model.*;
import arad.amir.ac.snlje.game.model.Board;
import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.xml.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Xml2BlMapper {
    private static final Logger log = LoggerFactory.getLogger(Xml2BlMapper.class);


    private Snakesandladders source;

    private List<Cell> cells;
    private List<Player> players;
    private Game destination;

    public Xml2BlMapper(Snakesandladders source) {
        this.source = source;
        this.destination = new Game();
        cells = new ArrayList<>(source.getBoard().getCells().getCell().size());
        players = new ArrayList<>(source.getPlayers().getPlayer().size());
    }

    public Game convert(){
        destination.setNumberOfSoldiersToWin(source.getNumberOfSoldiers());
        destination.setPlayers(players);
        destination.setBoard(convertBoard());
        destination.getBoard().setCells(cells);
        destination.getBoard().setPassages(new ArrayList<Passage>(
                source.getBoard().getSnakes().getSnake().size() +
                        source.getBoard().getLadders().getLadder().size()));
        for (Players.Player player : source.getPlayers().getPlayer()) {
            destination.getPlayers().add(convertPlayer(player));
        }
        destination.setCurrentTurn(players.indexOf(getDestPlayerByName(source.getCurrentPlayer())));
        for (arad.amir.ac.snlje.xml.model.Cell cell : source.getBoard().getCells().getCell()) {
            destination.getBoard().getCells().add(convertCell(cell));
        }
        for (Snakes.Snake snake : source.getBoard().getSnakes().getSnake()) {
            destination.getBoard().getPassages().add(convertSnake(snake));
        }
        for (Ladders.Ladder ladder : source.getBoard().getLadders().getLadder()) {
            destination.getBoard().getPassages().add(convertLadder(ladder));
        }
        return destination;
    }

    private Board convertBoard() {
        Board result = new Board();
        result.setSize(source.getBoard().getSize());
        return result;
    }

    private Player convertPlayer(Players.Player source) {
        Player result = new Player();
        result.setName(source.getName());
        result.setType(Player.Type.valueOf(source.getType().name()));
        result.setSoldierPositions(new ArrayList<Cell>(Game.NUMBER_OF_SOLDIERS)); // populated in convertCell
        return result;
    }

    private Cell convertCell(arad.amir.ac.snlje.xml.model.Cell source) {
        Cell result = new Cell();
        result.setIndex(source.getNumber().intValue() - 1); // convert cell num to cell index
        for (arad.amir.ac.snlje.xml.model.Cell.Soldiers soldiers : source.getSoldiers()) {
            Player solidersPlayer = getDestPlayerByName(soldiers.getPlayerName());
            for (int i = 0; i < soldiers.getCount(); i++) {
                solidersPlayer.getSoldierPositions().add(result);
            }
        }
        return result;
    }

    private Player getDestPlayerByName(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)){
                return player;
            }
        }
        throw new IllegalStateException("player should be present : '" + playerName + "' not in " + players);
    }

    private Passage convertLadder(Ladders.Ladder source) {
        Passage passage = new Passage();
        return passage;
    }

    private Passage convertSnake(Snakes.Snake source) {
        Passage result = new Passage();
        return result;
    }


}
