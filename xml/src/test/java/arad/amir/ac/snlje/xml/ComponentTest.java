package arad.amir.ac.snlje.xml;

import arad.amir.ac.snlje.game.bl.GameBuilderTest;
import arad.amir.ac.snlje.game.model.Game;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 25/07/2014
 */
public class ComponentTest {
    private static final Logger log = LoggerFactory.getLogger(ComponentTest.class);


    @Test
    public void testHappyFlow() throws Exception {
        Game game = GameBuilderTest.buildRandomGame(false);

    }

}
