package org.apache.roller.weblogger.ui.rendering;

/**
 * Custom exception for rendering-related errors.
 */
public class RenderingException extends Exception {

    private static final long serialVersionUID = 1L;

    public RenderingException() {
        super();
    }

    public RenderingException(String message) {
        super(message);
    }

    public RenderingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderingException(Throwable cause) {
        super(cause);
    }
}