package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.pojos.Comment;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.util.ReflectionUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

public class CommentValidationManager {

    private static final int MAX_COMMENT_LENGTH = 1000;

    private CommentValidatorFactory commentValidatorFactory;

    public CommentValidationManager(CommentValidatorFactory commentValidatorFactory) {
        this.commentValidatorFactory = commentValidatorFactory;
    }

    public List<CommentValidationError> validateComment(Comment comment) {
        List<CommentValidationError> errors = new ArrayList<>();

        validateCommentLength(comment, errors);
        validateCommentContent(comment, errors);

        return errors;
    }

    private void validateCommentLength(Comment comment, List<CommentValidationError> errors) {
        if (comment.getContent().length() > MAX_COMMENT_LENGTH) {
            errors.add(new CommentValidationError("comment.too.long", "Comment is too long"));
        }
    }

    private void validateCommentContent(Comment comment, List<CommentValidationError> errors) {
        List<CommentValidator> commentValidators = commentValidatorFactory.createCommentValidators();

        for (CommentValidator validator : commentValidators) {
            validator.validateComment(comment, errors);
        }
    }

    public static class CommentValidationError {
        private String key;
        private String message;

        public CommentValidationError(String key, String message) {
            this.key = key;
            this.message = message;
        }

        public String getKey() {
            return key;
        }

        public String getMessage() {
            return message;
        }
    }

    public interface CommentValidator {
        void validateComment(Comment comment, List<CommentValidationError> errors);
    }

    public static class CommentValidatorFactory {
        public List<CommentValidator> createCommentValidators() {
            try {
                return ReflectionUtilities.newInstancesFromProperty("comment.validators");
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new WebloggerException("Error creating comment validators", e);
            }
        }
    }
}