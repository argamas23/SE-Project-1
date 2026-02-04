package org.apache.roller.planet.business;

/**
 * Represents an exception that occurs during the bootstrapping process.
 */
public class BootstrapException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new BootstrapException with the specified detail message.
     * 
     * @param message the detail message.
     */
    public BootstrapException(String message) {
        super(message);
    }

    /**
     * Constructs a new BootstrapException with the specified detail message and cause.
     * 
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public BootstrapException(String message, Throwable cause) {
        super(message, cause);
    }
}