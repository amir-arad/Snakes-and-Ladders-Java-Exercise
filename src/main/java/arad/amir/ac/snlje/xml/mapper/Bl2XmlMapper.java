package arad.amir.ac.snlje.xml.mapper;

import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Passage;
import arad.amir.ac.snlje.game.model.Player;
import arad.amir.ac.snlje.xml.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amira
 * @since 26/07/2014
 */
public class Bl2XmlMapper {
    private static final Logger log = LoggerFactory.getLogger(Bl2XmlMapper.class);

    public Snakesandladders convert(Game source, String name){
        Snakesandladders result = new Snakesandladders();
        result.setBoard(new Board());
        result.setName(name);
        result.setNumberOfSoldiers(source.getNumberOfSoldiersToWin());
        result.setPlayers(new Players());
        result.setCurrentPlayer(source.getPlayers().get(source.getCurrentTurn()).getName());
        result.getBoard().setCells(new Cells());
        result.getBoard().setLadders(new Ladders());
        result.getBoard().setSnakes(new Snakes());
        result.getBoard().setSize(source.getBoard().getSize());
        // temp cells list that contains all possible cells
        List<arad.amir.ac.snlje.xml.model.Cell> cells = new ArrayList<>(source.getBoard().getCells().size());
        for (Cell cell : source.getBoard().getCells()) {
           // result.getBoard().getCells().getCell()
            cells.add(convertCell(cell));
        }
        for (Player player : source.getPlayers()) {
            result.getPlayers().getPlayer().add(convertPlayer(player, cells));
        }
        // add to xml model only cells with soldiers
        for (arad.amir.ac.snlje.xml.model.Cell cell : cells) {
            if (!cell.getSoldiers().isEmpty()){
                result.getBoard().getCells().getCell().add(cell);
            }
        }
        for (Passage passage : source.getBoard().getPassages()) {
            switch (passage.getType()) {
                case SNAKE:
                    result.getBoard().getSnakes().getSnake().add(convertSnake(passage));
                    break;
                case LADDER:
                    result.getBoard().getLadders().getLadder().add(convertLadder(passage));
                    break;
                default:
                    throw new IllegalArgumentException("unknown passage type : " + passage.getType());
            }
        }
        return result;
    }

    private Ladders.Ladder convertLadder(Passage source) {
        Ladders.Ladder result = new Ladders.Ladder();
        result.setFrom(BigInteger.valueOf(source.getFrom().getNumber()));
        result.setTo(BigInteger.valueOf(source.getTo().getNumber()));
        return result;
    }

    private Snakes.Snake convertSnake(Passage source) {
        Snakes.Snake result = new Snakes.Snake();
        result.setFrom( BigInteger.valueOf((source.getFrom().getNumber())));
        result.setTo( BigInteger.valueOf((source.getTo().getNumber())));
        return result;
    }

    private arad.amir.ac.snlje.xml.model.Cell convertCell(Cell source) {
        arad.amir.ac.snlje.xml.model.Cell result = new arad.amir.ac.snlje.xml.model.Cell();
        result.setNumber(BigInteger.valueOf(source.getNumber()));
        // soldiers populated in convertCell
        return result;
    }

    private Players.Player convertPlayer(Player source,  List<arad.amir.ac.snlje.xml.model.Cell> cells) {
        Players.Player result = new Players.Player();
        result.setName(source.getName());
        result.setType(PlayerType.valueOf(source.getType().name()));
        // update cells of player soldiers
        for (Cell cell : source.getSoldierPositions()) {
            List<arad.amir.ac.snlje.xml.model.Cell.Soldiers> soldiersList = cells.get(cell.getIndex()).getSoldiers();
            arad.amir.ac.snlje.xml.model.Cell.Soldiers soldiers = getSoldiers(soldiersList, result.getName());
            soldiers.setCount(soldiers.getCount() + 1);
        }
        return result;
    }

    private arad.amir.ac.snlje.xml.model.Cell.Soldiers getSoldiers(List<arad.amir.ac.snlje.xml.model.Cell.Soldiers> soldiersList, String playerName) {
        arad.amir.ac.snlje.xml.model.Cell.Soldiers soldiers;
        if (!soldiersList.isEmpty() && playerName.equals(soldiersList.get(soldiersList.size() - 1).getPlayerName())) {
            soldiers = soldiersList.get(soldiersList.size() - 1);
        } else {
            soldiers = new arad.amir.ac.snlje.xml.model.Cell.Soldiers();
            soldiers.setCount(0);
            soldiers.setPlayerName(playerName);
            soldiersList.add(soldiers);
        }
        return soldiers;
    }
}
