package arad.amir.ac.snlje.console;

import arad.amir.ac.snlje.game.bl.GameBuilderTest;
import arad.amir.ac.snlje.game.model.Game;
import org.junit.Ignore;
import org.junit.Test;

public class DisplayerTest {

    @Test
    @Ignore
    public void testPrintGame() throws Exception {
        Game g = GameBuilderTest.buildRandomGame(false);
        Displayer app = new Displayer();
        app.printGame(g);
    }

    @Test
    @Ignore
    public void testMenu() throws Exception {
        Displayer app = new Displayer();
        int choice = app.menu("one", "two", "three");
        System.out.println(choice);
    }
}