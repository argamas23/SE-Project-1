package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import java.util.regex.Pattern;

public class ExcessLinksCommentValidator implements CommentValidator {

    private static final int MAX_LINKS_ALLOWED = 15;
    private static final int MIN_COMMENT_LENGTH = 24;
    private static final int MAX_LINK_LENGTH = 28;

    @Override
    public void validate(Comment comment) {
        validateLinkCount(comment);
        validateCommentLength(comment);
        validateLinkLength(comment);
    }

    private void validateLinkCount(Comment comment) {
        int linkCount = countLinks(comment.getBody());
        if (linkCount > MAX_LINKS_ALLOWED) {
            comment.addValidationError("excess.links");
        }
    }

    private void validateCommentLength(Comment comment) {
        if (comment.getBody().length() < MIN_COMMENT_LENGTH) {
            comment.addValidationError("comment.too.short");
        }
    }

    private void validateLinkLength(Comment comment) {
        String[] links = comment.getBody().split("http");
        for (String link : links) {
            if (link.length() > MAX_LINK_LENGTH) {
                comment.addValidationError("link.too.long");
            }
        }
    }

    private int countLinks(String body) {
        String linkPattern = "http://\\S+";
        Pattern pattern = Pattern.compile(linkPattern);
        return (body.length() - body.replaceAll(linkPattern, "").length()) / linkPattern.length();
    }

    private static class Comment {
        private String body;
        private java.util.List<String> errors = new java.util.ArrayList<>();

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void addValidationError(String error) {
            errors.add(error);
        }
    }

    private interface CommentValidator {
        void validate(Comment comment);
    }
}