```java
package org.apache.roller.planet.business;

import org.apache.roller.RollerException;

/**
 * Exception for errors during Planet initialization.
 * This class is a standard implementation of a custom checked exception,
 * extending a presumably project-specific base exception (`RollerException`)
 * to provide more semantic meaning for specific error conditions (initialization failures).
 * This pattern is generally considered good practice for clearer error handling
 * and distinct exception catching.
 */
public class InitializationException extends RollerException {

    /**
     * Constructs an InitializationException with no detail message.
     */
    public InitializationException() {
        super();
    }

    /**
     * Constructs an InitializationException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InitializationException(String message) {
        super(message);
    }

    /**
     * Constructs an InitializationException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     */
    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an InitializationException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the getCause() method).
     */
    public InitializationException(Throwable cause) {
        super(cause);
    }
}
```