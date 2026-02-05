```java
package org.apache.roller.planet.business;

import org.apache.roller.planet.RollerException;

/**
 * Exception thrown when there is a problem bootstrapping the Planet business layer.
 *
 * <p>
 * This class is identified as a "Lazy Class" because it adds no new fields, methods, or specific behavior
 * beyond delegating directly to its superclass {@link RollerException}.
 * </p>
 * <p>
 * If no unique information or behavior is required for bootstrap-specific failures
 * that cannot be conveyed by {@link RollerException} itself,
 * it is recommended to refactor code to use {@link RollerException} directly and consider
 * deprecating and eventually removing this class. This helps reduce unnecessary abstraction
 * and maintain a clearer exception hierarchy.
 * </p>
 *
 * @author davidm
 * @deprecated This class is considered lazy as it provides no unique functionality over {@link RollerException}.
 *             Prefer using {@link org.apache.roller.planet.RollerException} directly unless specific
 *             unique attributes or behavior are added to this exception type.
 */
@Deprecated
public class BootstrapException extends RollerException {

    public BootstrapException() {
        super();
    }

    public BootstrapException(String message) {
        super(message);
    }

    public BootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public BootstrapException(Throwable cause) {
        super(cause);
    }
}
```