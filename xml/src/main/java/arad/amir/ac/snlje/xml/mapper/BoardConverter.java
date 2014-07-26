package arad.amir.ac.snlje.xml.mapper;

import arad.amir.ac.snlje.game.model.Cell;
import arad.amir.ac.snlje.game.model.Passage;
import arad.amir.ac.snlje.xml.model.Board;
import arad.amir.ac.snlje.xml.model.Ladders;
import arad.amir.ac.snlje.xml.model.Snakes;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author amira
 * @since 26/07/2014
 */
public class BoardConverter extends BaseConverter<arad.amir.ac.snlje.game.model.Board, Board>{
    private static final Logger log = LoggerFactory.getLogger(BoardConverter.class);

    public BoardConverter() {
        super(arad.amir.ac.snlje.game.model.Board.class, Board.class);
    }

    @Override
    public Board convertTo(arad.amir.ac.snlje.game.model.Board source, Board destination) {
        if (source == null) {
            return null;
        }
        for (Cell cell : source.getCells()) {
            destination.getCells().getCell().add(convertCellTo(cell));
        }
        for (Passage passage : source.getPassages()) {
            switch(passage.getType()){
                case SNAKE:
                    destination.getSnakes().getSnake().add(getMapper().map(passage, Snakes.Snake.class));
                    break;
                case LADDER:
                    destination.getLadders().getLadder().add(getMapper().map(passage, Ladders.Ladder.class));
                    break;
                default:
                    throw new MappingException("unknown type : " + passage.getType());
            }
        }
        destination.setSize(source.getSize());
        return destination;
    }

    private arad.amir.ac.snlje.xml.model.Cell convertCellTo(Cell cell) {
        return null;  //todo implement. To change body of created methods use File | Settings | File Templates.
    }

    private Cell convertCellFrom(arad.amir.ac.snlje.xml.model.Cell cell) {
        return null;  //todo implement. To change body of created methods use File | Settings | File Templates.
    }


    @Override
    public arad.amir.ac.snlje.game.model.Board convertFrom(Board source, arad.amir.ac.snlje.game.model.Board destination) {
        if (source == null) {
            return null;
        }
        destination.setCells(new ArrayList<Cell>(source.getCells().getCell().size()));
        for (arad.amir.ac.snlje.xml.model.Cell cell : source.getCells().getCell()) {
            destination.getCells().add(convertCellFrom(cell));
        }
        destination.setPassages(new ArrayList<Passage>(source.getSnakes().getSnake().size() + source.getLadders().getLadder().size()));
        for (Snakes.Snake snake : source.getSnakes().getSnake()) {
            destination.getPassages().add(getMapper().map(snake, Passage.class));
        }
        for (Ladders.Ladder ladder : source.getLadders().getLadder()) {
            destination.getPassages().add(getMapper().map(ladder, Passage.class));
        }
        destination.setSize(source.getSize());
        return destination;
    }

}
