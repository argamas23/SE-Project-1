package org.apache.roller.weblogger.ui.rendering.util;

/**
 * Exception thrown when an invalid request is made.
 */
public class InvalidRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorMessage;

    public InvalidRequestException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}