package arad.amir.ac.snlje.game.bl;

import arad.amir.ac.snlje.game.model.Board;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Passage;
import arad.amir.ac.snlje.game.model.Player;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameBuilderTest {
    private static final Logger log = LoggerFactory.getLogger(GameBuilderTest.class);
    public static final int NUM_ILLEGAL_PASSAGE_CELLS = 2;
    public static final int NUM_EDGES_PER_PASSAGE = 2;

    private static int randInRange(int min, int max) {
        return (int) (min + Math.round(Math.random() * (max - min)));
    }

    @Test
    public void testValidBuild() throws Exception {
        log.debug("starting");
        for (int i = 0; i < 10000; i++) {
            GameValidatorTest.testValidGame(buildRandomGame(false));
        }
        log.debug("done");
    }


    @Test
    public void testValidSampleXml() throws Exception {
        log.debug("starting");
        for (int i = 0; i < 10000; i++) {
            GameValidatorTest.testValidGame(buildRandomGame(false));
        }
        log.debug("done");
    }


    public static Game buildRandomGame(boolean newGame) {
        int boardSize = randInRange(Board.MIN_SIZE, Board.MAX_SIZE);
        int numOfPassages = randInRange(0, boardSize * boardSize - NUM_ILLEGAL_PASSAGE_CELLS) / (Passage.Type.values().length * NUM_EDGES_PER_PASSAGE);
        int winningCondition = randInRange(Game.MIN_WINNING_CONDITION, Game.MAX_WINNING_CONDITION);
        int numOfPlayers = randInRange(Game.MIN_NUM_OF_PLAYERS, Game.MAX_NUM_OF_PLAYERS);
        List<Player> players = new ArrayList<>(numOfPlayers);
        for (int i = 0; i < numOfPlayers; i++) {
            Player player = new Player();
            player.setName("player " + i);
            player.setType(Player.Type.values()[i % 2]);
            players.add(player);
        }
        GameBuilder uut = new GameBuilder();
        uut.setBoardSize(boardSize);
        uut.setNewGame(newGame);
        uut.setNumOfPassages(numOfPassages);
        uut.setPlayers(players);
        uut.setWinningCondition(winningCondition);

        return uut.build();
    }
}