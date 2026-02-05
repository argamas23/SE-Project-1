```java
package org.apache.roller;

/**
 * Roller specific exception.
 *
 * @author <a href="mailto:anil@busypass.com">Anil Gangadharan</a>
 */
public class RollerException extends Exception {
    private static final long serialVersionUID = 8990428383321590747L;

    /**
     * Constructs a new <code>RollerException</code>.
     */
    public RollerException() {
        super();
    }

    /**
     * Constructs a new <code>RollerException</code> with the specified
     * detail message.
     * @param msg the detail message.
     */
    public RollerException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>RollerException</code> with the specified
     * detail message and cause.
     * @param msg the detail message.
     * @param t the cause
     */
    public RollerException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs a new <code>RollerException</code> with the specified
     * cause.
     * @param t the cause
     */
    protected RollerException(Throwable t) {
        super(t);
    }

    /**
     * Returns the message of the ultimate root cause exception in the chain.
     * If this exception has no cause, it returns this exception's message.
     * @return the message of the ultimate root cause exception.
     */
    public String getRootCauseMessage() {
        Throwable rootCause = this;
        // Traverse the cause chain to find the ultimate root cause
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause.getMessage();
    }
}
```