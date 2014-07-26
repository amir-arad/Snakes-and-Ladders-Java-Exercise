package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.bl.GameBuilderTest;
import arad.amir.ac.snlje.game.model.Game;
import org.junit.Test;

public class ApplicationTest {

    @Test
    public void testPrintGame() throws Exception {
        Game g = GameBuilderTest.buildRandomGame(false);

        Application app = new Application();

        app.printGame(g, System.out);

    }
}