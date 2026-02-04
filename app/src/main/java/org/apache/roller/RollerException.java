package org.apache.roller;

import java.util.Arrays;

/**
 * Roller exception class.
 */
public class RollerException extends Exception {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_MESSAGE = "An error occurred";

    private int errorCode;
    private String errorMessage;

    /**
     * Enum for error codes.
     */
    public enum ErrorCode {
        UNKNOWN(0),
        VALIDATION_ERROR(1);

        private final int value;

        ErrorCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Constructor for RollerException.
     * 
     * @param errorCode    the error code
     * @param errorMessage the error message
     */
    public RollerException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Constructor for RollerException with ErrorCode enum.
     * 
     * @param errorCode    the error code
     * @param errorMessage the error message
     */
    public RollerException(ErrorCode errorCode, String errorMessage) {
        this(errorCode.getValue(), errorMessage);
    }

    /**
     * Constructor for RollerException with default error message.
     * 
     * @param errorCode the error code
     */
    public RollerException(ErrorCode errorCode) {
        this(errorCode.getValue(), DEFAULT_ERROR_MESSAGE);
    }

    /**
     * Gets the error code.
     * 
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Throws a RollerException with a validation error code.
     * 
     * @param messages the error messages
     */
    public static void throwValidationException(String... messages) {
        throw new RollerException(ErrorCode.VALIDATION_ERROR, String.join("\n", messages));
    }

    /**
     * Creates and returns a formatted error message.
     * 
     * @param format the format
     * @param args   the arguments
     * @return the formatted error message
     */
    public static String createErrorMessage(String format, Object... args) {
        return String.format(format, args);
    }

    @Override
    public String toString() {
        return "RollerException [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
    }

    /**
     * Handle error.
     * 
     * @param errorCode    the error code
     * @param errorMessage the error message
     */
    public static void handleError(ErrorCode errorCode, String errorMessage) {
        System.err.println("Error Code: " + errorCode.getValue());
        System.err.println("Error Message: " + errorMessage);
    }

    /**
     * Handle error with default error message.
     * 
     * @param errorCode the error code
     */
    public static void handleError(ErrorCode errorCode) {
        handleError(errorCode, DEFAULT_ERROR_MESSAGE);
    }
}