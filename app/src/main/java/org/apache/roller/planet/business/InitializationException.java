// InitializationException.java
package org.apache.roller.planet.business;

/**
 * Represents an exception that occurs during the initialization process.
 */
public class InitializationException extends Exception {

    private final String message;

    /**
     * Constructs a new InitializationException with the given message.
     *
     * @param message the detail message
     */
    public InitializationException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Returns the detail message of this exception.
     *
     * @return the detail message
     */
    @Override
    public String getMessage() {
        return message;
    }
}