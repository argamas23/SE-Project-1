package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.pojos_comment.WeblogEntryComment;
import org.apache.roller.weblogger.util.URLUtilities;

public class MathCommentAuthenticator {

    private static final Log log = LogFactory.getLog(MathCommentAuthenticator.class);
    private static final int MAX_ADDITION_VALUE = 10;
    private static final int MAX_SUBTRACTION_VALUE = 10;

    public boolean isValidComment(WeblogEntryComment comment) {
        try {
            String expression = comment.getCommentText();
            boolean isValid = validateMathExpression(expression);
            return isValid;
        } catch (Exception e) {
            log.error("Error validating comment", e);
            return false;
        }
    }

    private boolean validateMathExpression(String expression) {
        String[] parts = expression.split("=");
        if (parts.length != 2) {
            return false;
        }

        String[] operands = parts[0].split("\\+");
        if (operands.length != 2) {
            return false;
        }

        int num1;
        int num2;
        try {
            num1 = Integer.parseInt(operands[0].trim());
            num2 = Integer.parseInt(operands[1].trim());
        } catch (NumberFormatException e) {
            log.error("Invalid numbers in math expression", e);
            return false;
        }

        int result = num1 + num2;
        if (result < 0 || result > MAX_ADDITION_VALUE + MAX_SUBTRACTION_VALUE) {
            return false;
        }

        String expectedResult = parts[1].trim();
        return String.valueOf(result).equals(expectedResult);
    }

    public boolean isValidSubtractionExpression(String expression) {
        String[] parts = expression.split("=");
        if (parts.length != 2) {
            return false;
        }

        String[] operands = parts[0].split("-");
        if (operands.length != 2) {
            return false;
        }

        int num1;
        int num2;
        try {
            num1 = Integer.parseInt(operands[0].trim());
            num2 = Integer.parseInt(operands[1].trim());
        } catch (NumberFormatException e) {
            log.error("Invalid numbers in math expression", e);
            return false;
        }

        int result = num1 - num2;
        if (result < -MAX_SUBTRACTION_VALUE || result > MAX_ADDITION_VALUE) {
            return false;
        }

        String expectedResult = parts[1].trim();
        return String.valueOf(result).equals(expectedResult);
    }
}