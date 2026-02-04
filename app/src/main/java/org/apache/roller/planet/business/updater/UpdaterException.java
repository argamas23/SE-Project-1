package org.apache.roller.planet.business.updater;

/**
 * Represents an exception that occurs during the updating process of a planet.
 */
public class UpdaterException extends Exception {

    private static final String MISSING_PUBLISHER = "Missing publisher";
    private static final String MISSING_PLANET = "Missing planet";
    private static final String MISSING_FEED = "Missing feed";
    private static final String PLANET_NOT_FOUND = "Planet not found";
    private static final String FEED_NOT_FOUND = "Feed not found";

    /**
     * Constructs an UpdaterException with the specified message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UpdaterException(String message) {
        super(message);
    }

    /**
     * Constructs an UpdaterException with the specified message and cause.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public UpdaterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an UpdaterException with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public UpdaterException(Throwable cause) {
        super(cause);
    }

    /**
     * Returns a new UpdaterException representing a missing publisher.
     *
     * @return a new UpdaterException
     */
    public static UpdaterException missingPublisher() {
        return new UpdaterException(MISSING_PUBLISHER);
    }

    /**
     * Returns a new UpdaterException representing a missing planet.
     *
     * @return a new UpdaterException
     */
    public static UpdaterException missingPlanet() {
        return new UpdaterException(MISSING_PLANET);
    }

    /**
     * Returns a new UpdaterException representing a missing feed.
     *
     * @return a new UpdaterException
     */
    public static UpdaterException missingFeed() {
        return new UpdaterException(MISSING_FEED);
    }

    /**
     * Returns a new UpdaterException representing a planet not found.
     *
     * @return a new UpdaterException
     */
    public static UpdaterException planetNotFound() {
        return new UpdaterException(PLANET_NOT_FOUND);
    }

    /**
     * Returns a new UpdaterException representing a feed not found.
     *
     * @return a new UpdaterException
     */
    public static UpdaterException feedNotFound() {
        return new UpdaterException(FEED_NOT_FOUND);
    }
}