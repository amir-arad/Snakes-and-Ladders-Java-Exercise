package arad.amir.ac.snlje.game.bl;

import java.util.Collection;

/**
 * @author amira
 * @since 09/08/2014
 */
public interface ControllerSession {
    void printErrors(String error, Collection<String> errors);

    void printNewError(String error);

    void clearError();
}
