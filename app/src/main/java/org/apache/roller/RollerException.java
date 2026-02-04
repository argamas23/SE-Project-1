package org.apache.roller;

import java.util.logging.Logger;

/**
 * Custom exception class for Roller application.
 */
public class RollerException extends Exception {

    private static final Logger LOGGER = Logger.getLogger(RollerException.class.getName());
    private static final String UNKNOWN_ERROR = "Unknown Error";

    /**
     * Constructs a new RollerException with the given message.
     *
     * @param message the detail message
     */
    public RollerException(String message) {
        super(message);
        LOGGER.severe(message);
    }

    /**
     * Constructs a new RollerException with the given message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public RollerException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.severe(message);
    }

    /**
     * Handles the given throwable by logging and re-throwing it as a RollerException.
     *
     * @param throwable the throwable to handle
     * @throws RollerException the handled exception
     */
    public static void handleThrowable(Throwable throwable) {
        if (throwable instanceof RollerException) {
            throw (RollerException) throwable;
        } else {
            LOGGER.severe(UNKNOWN_ERROR);
            throw new RollerException(UNKNOWN_ERROR, throwable);
        }
    }

    /**
     * Returns a formatted error message with the given code and parameters.
     *
     * @param code      the error code
     * @param params    the error message parameters
     * @return the formatted error message
     */
    public static String getErrorMessage(String code, Object... params) {
        // Using a more robust way to handle error messages
        // For example, using a ResourceBundle or a dedicated error message handler
        return String.format("Error %s: %s", code, params);
    }
}