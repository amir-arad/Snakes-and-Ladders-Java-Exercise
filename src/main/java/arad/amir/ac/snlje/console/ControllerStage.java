package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.bl.GameManager;
import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.bl.GameBuilder;
import arad.amir.ac.snlje.game.model.Board;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Passage;
import arad.amir.ac.snlje.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
* @author amira
* @since 26/07/2014
*/
enum ControllerStage {
    CHOOSE_GAME {
        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            int choice = session.getController().menu("Please choose:", "new game", "load game", "exit");
            switch(choice){
                case 0:
                    return NEW_GAME_SCREEN;
                case 1:
                    return LOAD_GAME_SCREEN;
                case 2:
                    return null;
                default:
                    throw new IllegalArgumentException("wrong choice : " + choice);
            }
        }
    },
    NEW_GAME_SCREEN {
        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            session.getController().printTitle("configuring new game");
            GameBuilder builder = new GameBuilder();
            builder.setBoardSize(session.getController().inputInteger("choose board size", Board.MIN_SIZE, Board.MAX_SIZE));
            int maxPassages = Board.calcMaxPassages(builder.getBoardSize());
            builder.setNumOfPassages(session.getController().inputInteger("choose number of passages", 0, maxPassages));
            builder.setNewGame(true);
            builder.setWinningCondition(session.getController().inputInteger("choose winning conditions", Game.MIN_WINNING_CONDITION, Game.MAX_WINNING_CONDITION));
            int numOfPlayers = session.getController().inputInteger("choose number of players", Game.MIN_NUM_OF_PLAYERS, Game.MAX_NUM_OF_PLAYERS);
            builder.setPlayers(new ArrayList<Player>(numOfPlayers));
            for (int i = 0; i < numOfPlayers; i++) {
                String name =  session.getController().inputString("choose a name for player " + (i + 1));
                int typeIdx = session.getController().menu("what is " + name + " : ", "human player", "computer player");
                Player.Type type = Player.Type.values()[typeIdx];
                builder.getPlayers().add(new Player(name, type));
            }
            if (session.newGame(builder)){
                return ADVANCE_TURN_LOOP;
            }
            return NEW_GAME_SCREEN;
        }
    },
    LOAD_GAME_SCREEN {
        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            File loadFrom;
            if (session.getLastSave() == null) {
                loadFrom = getLoadFromFile(session.getController());
            } else {
                int saveType = session.getController().menu("choose load action : ", "Load from "+session.getLastSave().getName(), "Load new file", "Cancel");
                switch(saveType){
                    case 0:
                        loadFrom = session.getLastSave();
                        break;
                    case 1:
                        loadFrom = getLoadFromFile(session.getController());
                        break;
                    case 2:
                        loadFrom = null;
                        break;
                    default:
                        throw new IllegalArgumentException("wrong save action : " + saveType);
                }
            }
            if (loadFrom == null){
                return ADVANCE_TURN_LOOP;
            }
            if (session.loadGame(loadFrom)){
                return ADVANCE_TURN_LOOP;
            }
            return LOAD_GAME_SCREEN;
        }

        private File getLoadFromFile(Displayer displayer) {
            String fileName =  displayer.inputString("choose a file name");
            File file = new File(fileName);
            if (file.exists()) {
                return file;
            } else {
                int missingFileChoice = displayer.menu("File "+fileName+" does not exist", "Retry", "Cancel");
                if (missingFileChoice == 0){
                    return getLoadFromFile(displayer);
                }
            }
            return null;
        }
    },
    SAVE_GAME_SCREEN {
        Pattern pattern = Pattern.compile("[()\\s\\w\\-\\.]+");

        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            File saveTo;
            if (session.getLastSave() == null) {
                saveTo = getSaveToFile(session.getController());
            } else {
                int saveType = session.getController().menu("choose save action : ", "Save to "+session.getLastSave().getName(), "Save As", "Cancel");
                switch(saveType){
                    case 0:
                        saveTo = session.getLastSave();
                        break;
                    case 1:
                        saveTo = getSaveToFile(session.getController());
                        break;
                    case 2:
                        saveTo = null;
                        break;
                    default:
                        throw new IllegalArgumentException("wrong save action : " + saveType);
                }
            }
            if (saveTo == null){
                return ADVANCE_TURN_LOOP;
            }
            session.saveGame(saveTo);
            return ADVANCE_TURN_LOOP;
        }


        private File getSaveToFile(Displayer displayer) {
            String fileName =  displayer.inputString("choose a file name");
            File file = null;
            if (pattern.matcher(fileName).matches()) {
                try {
                    file = new File(fileName);
                    file.getCanonicalPath();
                } catch (IOException e) {
                    file = null;
                }
            }
            if (file == null){
                int overriteChoice = displayer.menu("Illegal name", "Choose different name", "Forget it");
                if (overriteChoice == 0){
                    return getSaveToFile(displayer);
                } else {
                    return null;
                }
            }
            if (file.exists()){
                int overriteChoice = displayer.menu("File "+fileName+" already exists", "Overwrite", "Choose different name");
                if (overriteChoice == 1){
                    return getSaveToFile(displayer);
                }
            }
            return file;
        }
    },
    ADVANCE_TURN_LOOP {
        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            if (session.getManager().getWinner() != null){
                return VICTORY_SCREEN;
            }
            Player player = session.getManager().getCurrentPlayer();
            session.getController().printTitle("It is " +player.getName()+ "'s turn");
            session.getController().printGame(session.getGame());
            int choice = session.getController().menu("choose your action:", "save game", "let " +player.getName()+ " play", "end game");
            switch(choice){
                case 0:
                    return SAVE_GAME_SCREEN;
                case 1:
                    return PLAYER_TURN;
                case 2:
                    return CHOOSE_GAME;
                default:
                    throw new IllegalArgumentException("wrong choice : " + choice);
            }
        }
    },
    PLAYER_TURN {
        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            Player player = session.getManager().getCurrentPlayer();
            int dieRoll = session.getManager().rollDie();
            List <Integer> movementChoices = new ArrayList<>(session.getManager().getCurrentPlayerSoliderCellIndexes());
            int choice;
            switch(player.getType()){
                case HUMAN:
                    session.getController().printLine(player.getName() + ", you have rolled a " + dieRoll);
                    List<String> options = new ArrayList<>(movementChoices.size());
                    for (Integer movementChoice : movementChoices) {
                        options.add("cell " + (movementChoice + 1));
                    }
                    int menuChoice = session.getController().menu("choose soldier to move ", options.toArray(new String[options.size()]));
                    choice = movementChoices.get(menuChoice);
                    break;
                case COMPUTER:
                    choice = movementChoices.get((int) (Math.random() * movementChoices.size()));
                    break;
                default:
                    throw new IllegalArgumentException("unknown player type : " + player);
            }
            GameManager.Move move = session.getManager().calcMove(choice);
            session.getManager().executeMove(move);
            Passage passageUsed = move.getThrough();
            if (player.getType() == Player.Type.COMPUTER) {
                session.getController().printTitle(player.getName() + " rolled " + dieRoll + " and chose to move a soldier from " + (choice + 1));
                if (passageUsed != null){
                    session.getController().printLine(
                            player.getName() + "'s soldier passed through a " + passageUsed.getType().toString().toLowerCase()
                                    + " and landed in " + passageUsed.getTo().getNumber());
                }
                // session.getController().printGame(session.getGame());
                session.getController().pause();
            } else if (passageUsed != null){
                session.getController().printTitle(
                        player.getName() + "'s soldier passed through a " + passageUsed.getType().toString().toLowerCase()
                                + " and landed in " + passageUsed.getTo().getNumber());
                session.getController().pause();
            }
            return ADVANCE_TURN_LOOP;
        }
    },
    VICTORY_SCREEN {
        @Override
        ControllerStage execute(GameSession<Displayer> session) {
            Player winner = session.getManager().getWinner();
            session.getController().printTitle(winner.getName() + " has won!");
            session.getController().printGame(session.getGame());
            session.getController().pause();
            return CHOOSE_GAME;
        }
    };
    private static final Logger log = LoggerFactory.getLogger(ControllerStage.class);

    abstract ControllerStage execute(GameSession<Displayer> session);

}
