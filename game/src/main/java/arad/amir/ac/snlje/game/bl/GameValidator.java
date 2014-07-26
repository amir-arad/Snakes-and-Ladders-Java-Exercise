package arad.amir.ac.snlje.game.bl;

import arad.amir.ac.snlje.game.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author amira
 * @since 19/07/2014
 */
public class GameValidator{
    private static final Logger log = LoggerFactory.getLogger(GameValidator.class);

    public Collection<String> validateGame(Game game){
        Collection<String> errors = new LinkedList<>();
        validateGame(game, errors);
        return errors;
    }

    public void validateGame(Game game, Collection<String> errors){
        if (game.getPlayers() == null){
            errors.add("no players object defined");
        } else {
            validatePlayers(game, errors);
            validateCurrentTurn(game, errors);
        }
        validateWinningConditions(game, errors);
        validateBoard(game, errors);
    }

    private void validateBoard(Game game, Collection<String> errors) {
        if (game.getBoard() == null){
            errors.add("no board object defined");
        } else {
            int size = game.getBoard().getSize();
            if (size > Board.MAX_SIZE){
                errors.add("bad board size: too high " + size);
            } else if (size < Board.MIN_SIZE){
                errors.add("bad board size: too low " + size);
            }
            validateNumberOfCells(game, errors);
            validatePassages(game, errors);
        }
    }

    private void validatePassages(Game game, Collection<String> errors) {
        Set<Passage> snakes = new HashSet<>();
        Set<Passage> ladders = new HashSet<>();
        Set<Cell> pasageEdges = new HashSet<>(game.getBoard().getPassages().size() * 2);
        for (Passage passage : game.getBoard().getPassages()) {
            Set<Passage> typeCollection = getPassageTypeCollection(snakes, ladders, passage);
            validatePassage(errors, passage, typeCollection, pasageEdges, game.getBoard().getCells().size());
            if (!game.getBoard().getCells().contains(passage.getFrom())){
                errors.add("passage starts in a cell not referenced by the board");
            }
            if (!game.getBoard().getCells().contains(passage.getTo())){
                errors.add("passage leads to a cell not referenced by the board (redundant check)");
            }
        }
        if (snakes.size() != ladders.size()){
            errors.add("number of snakes does not match number of ladders");
        }
        int idx = 0;
        for (Cell cell : game.getBoard().getCells()) {
            validateCell(errors, snakes, ladders, idx++, cell);
        }
    }

    private int validateCell(Collection<String> errors, Set<Passage> snakes, Set<Passage> ladders, int idx, Cell cell) {
        if (idx != cell.getIndex()){
            errors.add("wrong cell index (should be "+idx+"): " + cell);
        }
        Passage toPassage = cell.getToPassage();
        if (toPassage != null){
            Set<Passage> collection = getPassageTypeCollection(snakes, ladders, toPassage);
            if (!collection.remove(toPassage)){
                errors.add("toPassage referenced by two cells or not referenced by board : " + toPassage);
            }
            if (!cell.equals(toPassage.getFrom())){
                errors.add("toPassage referenced by cell but not originate from it : " + cell + ", " + toPassage);
            }
        }
        return idx;
    }

    private void validatePassage(Collection<String> errors, Passage passage, Set<Passage> typeCollection, Set<Cell> pasageEdges, int numOfCells) {
        if (!typeCollection.add(passage)){
            errors.add("duplicate passage found : " + passage);
        }
        boolean missingEnd = false;
        if (passage.getTo() == null){
            missingEnd = true;
            errors.add("passage with no destination : " + passage);
        } else {
            if (!pasageEdges.add(passage.getTo())){
                errors.add("passage leads to a cell with another passage edge : " + passage.getTo());
            }
            if (passage.getTo().getNumber() == numOfCells){
                errors.add("passage leads to the last cell : " + passage);
            }
            if (passage.getTo().getIndex() == 0){
                errors.add("passage leads to the first cell : " + passage);
            }
        }
        if (passage.getFrom() == null){
            missingEnd = true;
            errors.add("passage with no source : " + passage);
        } else {
            if (passage.getFrom().getToPassage() != passage){
                errors.add("passage with no reference from source : " + passage + ", " + passage.getFrom());
            }
            if (passage.getFrom().equals(passage.getTo())){
                errors.add("passage leads from and to the same cell : " + passage);
            }

            if (passage.getFrom().getNumber() == numOfCells){
                errors.add("passage starts in the last cell : " + passage);
            }
            if (passage.getFrom().getIndex() == 0){
                errors.add("passage starts in the first cell : " + passage);
            }
        }
        if (!missingEnd && passage.getHigh().getIndex() < passage.getLow().getIndex()){
            errors.add("passage from and to cells are inverted : " + passage);
        }
    }

    private Set<Passage> getPassageTypeCollection(Set<Passage> snakes, Set<Passage> ladders, Passage passage) {
        switch (passage.getType()){
            case SNAKE: return snakes;
            case LADDER: return ladders;
        }
        throw new IllegalArgumentException("unexpected type : " + passage.getType());
    }

    private void validateNumberOfCells(Game game, Collection<String> errors) {
        int numberOfCells = new HashSet<>(game.getBoard().getCells()).size();
        if (numberOfCells < game.getBoard().getCells().size()){
            errors.add("duplicate cells found");
        }
        int expectedNumOfCells = game.getBoard().getSize() * game.getBoard().getSize();
        if (numberOfCells != expectedNumOfCells){
            errors.add("wrong number of cells, expected " + expectedNumOfCells);
        }
    }

    private void validatePlayers(Game game, Collection<String> errors) {
        int numberOfPlayers =  new HashSet<>(game.getPlayers()).size();
        if (numberOfPlayers < game.getPlayers().size()){
            errors.add("duplicate players found ");
        }
        if (numberOfPlayers > Game.MAX_NUM_OF_PLAYERS){
            errors.add("bad number of players: too high " + numberOfPlayers);
        } else if (numberOfPlayers < Game.MIN_NUM_OF_PLAYERS){
            errors.add("bad number of players: too low " + numberOfPlayers);
        }
        for (Player player : game.getPlayers()) {
            if (player.getName() == null || player.getName().isEmpty()){
                errors.add("nameless player");
            }
            if (player.getSoldierPositions() == null){
                errors.add("null soldiers for player " + player.getName());
            } else {
                if (player.getSoldierPositions().size() != Game.NUMBER_OF_SOLDIERS){
                    errors.add("bad number of soldiers for player: " + player);
                }
                for (Cell cell : player.getSoldierPositions()) {
                    if (cell == null){
                        errors.add("bad number of soldiers for player: " + player);
                    }
                    if (!game.getBoard().getCells().contains(cell)){
                        errors.add("solider on cell not from this board: " + cell);
                    }
                }
            }
        }
    }


    private void validateWinningConditions(Game game, Collection<String> errors) {
        if (game.getNumberOfSoldiersToWin() > Game.MAX_WINNING_CONDITION){
            errors.add("bad winning condition: too high " + game.getNumberOfSoldiersToWin());
        } else if (game.getNumberOfSoldiersToWin() < Game.MIN_WINNING_CONDITION){
            errors.add("bad winning condition: too low " + game.getNumberOfSoldiersToWin());
        }
    }

    private void validateCurrentTurn(Game game, Collection<String> errors) {
        if (game.getCurrentTurn() >= game.getPlayers().size()){
            errors.add("bad turn index: too high " + game.getCurrentTurn());
        } else if (game.getCurrentTurn() < 0){
            errors.add("bad turn index: too low " + game.getCurrentTurn());
        }
    }

}
