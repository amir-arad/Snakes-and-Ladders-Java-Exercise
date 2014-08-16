package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 11/08/2014
 */
public class GameBoardController extends AbsController{
    private static final Logger log = LoggerFactory.getLogger(GameBoardController.class);

    protected GameBoardController(GameSession<ControllersManager> session) {
        super(session);
    }

    // TODO player details, game display, roll die / die result, movement choices, continue button
}
