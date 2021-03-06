package ai.baby.util.exception;

import ai.scribble.License;

import javax.ejb.ApplicationException;

/**
 * Throw this exception if the DB is being dishonored by callers
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@ApplicationException(rollback = true)
final public class DBFetchDataException extends AbstractEjbApplicationException {

    final static private String MSG = "SORRY! I ENCOUNTERED AN OPERATION WHICH FAILED TO REFRESH DATA FROM THE DB. THE SOURCES SHOULD REFLECT THIS WHERE THE EXCEPTION WAS THROWN. SEE BELOW FOR MORE DETAILS.\n";

    /**
     * @param message
     */
    public DBFetchDataException(final String message) {
        super(MSG + message);
    }

    /**
     * @param e
     */
    public DBFetchDataException(final Exception e) {
        super(MSG, e);
    }
}
