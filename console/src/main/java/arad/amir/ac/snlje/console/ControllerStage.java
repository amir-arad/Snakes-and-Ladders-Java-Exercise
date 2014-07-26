package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.bl.GameBuilder;
import arad.amir.ac.snlje.game.bl.GameValidator;
import arad.amir.ac.snlje.game.model.Board;
import arad.amir.ac.snlje.game.model.Game;
import arad.amir.ac.snlje.game.model.Passage;
import arad.amir.ac.snlje.game.model.Player;
import arad.amir.ac.snlje.xml.mapper.Bl2XmlMapper;
import arad.amir.ac.snlje.xml.mapper.Xml2BlMapper;
import arad.amir.ac.snlje.xml.model.Snakesandladders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
* @author amira
* @since 26/07/2014
*/
enum ControllerStage {
    CHOOSE_GAME {
        @Override
        ControllerStage execute(ControllerSession session) {
            int choice = session.getDisplayer().menu("Please choose:", "new game", "load game", "exit");
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
        ControllerStage execute(ControllerSession session) {
            session.getDisplayer().printTitle("configuring new game");
            GameBuilder builder = new GameBuilder();
            builder.setBoardSize(session.getDisplayer().inputInteger("choose board size", Board.MIN_SIZE, Board.MAX_SIZE));
            int maxPassages = (builder.getBoardSize() * builder.getBoardSize() / 2 - 1) / 2;
            builder.setNumOfPassages(session.getDisplayer().inputInteger("choose number of passages", 0, maxPassages));
            builder.setNewGame(true);
            builder.setWinningCondition(session.getDisplayer().inputInteger("choose winning conditions", Game.MIN_WINNING_CONDITION, Game.MAX_WINNING_CONDITION));
            int numOfPlayers = session.getDisplayer().inputInteger("choose number of players", Game.MIN_NUM_OF_PLAYERS, Game.MAX_NUM_OF_PLAYERS);
            builder.setPlayers(new ArrayList<Player>(numOfPlayers));
            for (int i = 0; i < numOfPlayers; i++) {
                String name =  session.getDisplayer().inputString("choose a name for player " + (i + 1));
                int typeIdx = session.getDisplayer().menu("what is " + name + " : ", "human player", "computer player");
                Player.Type type = Player.Type.values()[typeIdx];
                builder.getPlayers().add(new Player(name, type));
            }
            Game game = builder.build();
            if (!validateGame(game, session.getDisplayer())) {
                return NEW_GAME_SCREEN;
            }
            session.setGame(game);
            return ADVANCE_TURN_LOOP;
        }
    },
    LOAD_GAME_SCREEN {
        @Override
        ControllerStage execute(ControllerSession session) {
            File loadFrom;
            if (session.getLastSave() == null) {
                loadFrom = getLoadFromFile(session.getDisplayer());
            } else {
                int saveType = session.getDisplayer().menu("choose load action : ", "Load from "+session.getLastSave().getName(), "Load new file", "Cancel");
                switch(saveType){
                    case 0:
                        loadFrom = session.getLastSave();
                        break;
                    case 1:
                        loadFrom = getLoadFromFile(session.getDisplayer());
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
            try {
                JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
                Snakesandladders origXmlModel = (Snakesandladders) jc.createUnmarshaller().unmarshal(loadFrom);
                Game game = new Xml2BlMapper().convert(origXmlModel);
                if (!validateGame(game, session.getDisplayer())) {
                    return LOAD_GAME_SCREEN;
                }
                session.setGame(game);
                session.setLastSave(loadFrom);
            } catch (JAXBException e) {
                session.getDisplayer().printLine("ERROR! game was not loaded.");
                log.error("error loading game from xml", e);
            }
            return ADVANCE_TURN_LOOP;
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
        ControllerStage execute(ControllerSession session) {
            File saveTo;
            if (session.getLastSave() == null) {
                saveTo = getSaveToFile(session.getDisplayer());
            } else {
                int saveType = session.getDisplayer().menu("choose save action : ", "Save to "+session.getLastSave().getName(), "Save As", "Cancel");
                switch(saveType){
                    case 0:
                        saveTo = session.getLastSave();
                        break;
                    case 1:
                        saveTo = getSaveToFile(session.getDisplayer());
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
            try {
                JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(new Bl2XmlMapper().convert(session.getGame(), saveTo.getName()), saveTo);
                session.setLastSave(saveTo);
            } catch (JAXBException e) {
                session.getDisplayer().printLine("ERROR! game was not saved.");
                log.error("error saving game to xml", e);
            }
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
        ControllerStage execute(ControllerSession session) {
            if (session.getManager().getWinner() != null){
                return VICTORY_SCREEN;
            }
            Player player = session.getManager().getCurrentPlayer();
            session.getDisplayer().printTitle("It is " +player.getName()+ "'s turn");
            session.getDisplayer().printGame(session.getGame());
            int choice = session.getDisplayer().menu("choose your action:", "save game", "let " +player.getName()+ " play", "end game");
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
        ControllerStage execute(ControllerSession session) {
            Player player = session.getManager().getCurrentPlayer();
            int dieRoll = session.getManager().rollDie();
            List <Integer> movementChoices = new ArrayList<>(session.getManager().getCurrentPlayerSoliderCellIndexes());
            int choice;
            switch(player.getType()){
                case HUMAN:
                    session.getDisplayer().printLine(player.getName() + ", you have rolled a " + dieRoll);
                    List<String> options = new ArrayList<>(movementChoices.size());
                    for (Integer movementChoice : movementChoices) {
                        options.add("cell " + (movementChoice + 1));
                    }
                    int menuChoice = session.getDisplayer().menu("choose soldier to move ", options.toArray(new String[options.size()]));
                    choice = movementChoices.get(menuChoice);
                    break;
                case COMPUTER:
                    choice = movementChoices.get((int) (Math.random() * movementChoices.size()));
                    break;
                default:
                    throw new IllegalArgumentException("unknown player type : " + player);
            }
            Passage passageUsed = session.getManager().playTurn(choice);
            if (player.getType() == Player.Type.COMPUTER) {
                session.getDisplayer().printTitle(player.getName() + " rolled " + dieRoll + " and chose to move a soldier from " + (choice + 1));
                if (passageUsed != null){
                    session.getDisplayer().printLine(
                            player.getName() + "'s soldier passed through a " + passageUsed.getType().toString().toLowerCase()
                            + " and landed in " + passageUsed.getTo().getNumber());
                }
                // session.getDisplayer().printGame(session.getGame());
                session.getDisplayer().pause();
            } else if (passageUsed != null){
                session.getDisplayer().printTitle(
                        player.getName() + "'s soldier passed through a " + passageUsed.getType().toString().toLowerCase()
                                + " and landed in " + passageUsed.getTo().getNumber());
                session.getDisplayer().pause();
            }
            return ADVANCE_TURN_LOOP;
        }
    },
    VICTORY_SCREEN {
        @Override
        ControllerStage execute(ControllerSession session) {
            Player winner = session.getManager().getWinner();
            session.getDisplayer().printTitle(winner.getName() + " has won!");
            session.getDisplayer().printGame(session.getGame());
            session.getDisplayer().pause();
            return CHOOSE_GAME;
        }
    };
    private static final Logger log = LoggerFactory.getLogger(ControllerStage.class);

    abstract ControllerStage execute(ControllerSession session);

    private static boolean validateGame(Game game, Displayer displayer) {
        Collection<String> errors = new GameValidator().validateGame(game);
        if (!errors.isEmpty()){
            displayer.printTitle("Illegal input detected");
            for (String error : errors) {
                displayer.printLine(error);
            }
            displayer.pause();
            return false;
        }
        return true;
    }
}
