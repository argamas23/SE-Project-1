package org.apache.roller.weblogger.ui.rendering.plugins.comments;

/**
 * Responsible for validating the size of comments to prevent excess size comments.
 */
public class ExcessSizeCommentValidator {

    private static final int MAX_COMMENT_SIZE = 1024;
    private static final int MAX_COMMENT_DEPTH = 5;

    public CommentValidationResult validate(Comment comment) {
        if (isCommentTooLarge(comment)) {
            return new CommentValidationResult(false, "Comment is too large");
        }
        if (isCommentTooDeep(comment)) {
            return new CommentValidationResult(false, "Comment is too deep");
        }
        return new CommentValidationResult(true, "");
    }

    private boolean isCommentTooLarge(Comment comment) {
        return comment.getSize() > MAX_COMMENT_SIZE;
    }

    private boolean isCommentTooDeep(Comment comment) {
        return comment.getDepth() > MAX_COMMENT_DEPTH;
    }

    public static class CommentValidationResult {
        private boolean isValid;
        private String message;

        public CommentValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class Comment {
        private int size;
        private int depth;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }
    }
}