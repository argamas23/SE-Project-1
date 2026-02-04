package org.apache.roller PLANET.business;

/**
 * Exception thrown when an error occurs during bootstrapping.
 */
public class BootstrapException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new BootstrapException with the given message.
     * 
     * @param message the detail message
     */
    public BootstrapException(String message) {
        super(message);
    }

    /**
     * Constructs a new BootstrapException with the given message and cause.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public BootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new BootstrapException with the given cause.
     * 
     * @param cause the cause of the exception
     */
    public BootstrapException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new BootstrapException with the given message, cause, 
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     * 
     * @param message            the detail message
     * @param cause             the cause of the exception
     * @param enableSuppression whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public BootstrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}