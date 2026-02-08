package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.WeblogComment;
import org.apache.roller.weblogger.ui.rendering.Renderer;
import org.apache.roller.weblogger.ui.rendering_comments.WeblogCommentForm;
import org.apache.roller.weblogger.util.RollerConstants;
import org.apache.roller.weblogger.util.Utilities;

public class WeblogEntryCommentForm {

    private static final int MAX_COMMENT_LENGTH = 4096;
    private WeblogEntry weblogEntry;
    private WeblogComment comment;
    private User user;
    private String error;
    private boolean previewMode;
    private boolean commentDeleted;

    public WeblogEntryCommentForm() {}

    public void setWeblogEntry(WeblogEntry weblogEntry) {
        this.weblogEntry = weblogEntry;
    }

    public WeblogEntry getWeblogEntry() {
        return weblogEntry;
    }

    public void setComment(WeblogComment comment) {
        this.comment = comment;
    }

    public WeblogComment getComment() {
        return comment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean isPreviewMode() {
        return previewMode;
    }

    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
    }

    public boolean isCommentDeleted() {
        return commentDeleted;
    }

    public void setCommentDeleted(boolean commentDeleted) {
        this.commentDeleted = commentDeleted;
    }

    public String getCommentContent() {
        if (comment != null) {
            return comment.getComment();
        } else {
            return null;
        }
    }

    public String getCommentName() {
        if (comment != null) {
            return comment.getName();
        } else {
            return null;
        }
    }

    public String getCommentEmail() {
        if (comment != null) {
            return comment.getEmail();
        } else {
            return null;
        }
    }

    public String getCommentUrl() {
        if (comment != null) {
            return comment.getUrl();
        } else {
            return null;
        }
    }

    public void saveComment() throws WebloggerException {
        if (comment == null) {
            comment = new WeblogComment();
        }

        comment.setWeblogEntry(weblogEntry);
        comment.setComment(getCommentContent());
        comment.setName(getCommentName());
        comment.setEmail(getCommentEmail());
        comment.setUrl(getCommentUrl());
    }

    public String renderCommentForm() {
        Renderer renderer = new Renderer();
        WeblogCommentForm commentForm = new WeblogCommentForm();

        if (previewMode) {
            return renderer.renderCommentPreview(comment);
        } else {
            return renderer.renderCommentForm(weblogEntry, comment, user);
        }
    }

    private void validateCommentContent(String commentContent) {
        if (commentContent.length() > MAX_COMMENT_LENGTH) {
            error = "Comment is too long, please limit to " + MAX_COMMENT_LENGTH + " characters.";
        }
    }

    private void validateCommentDetails(String name, String email, String url) {
        if (Utilities.isEmpty(name)) {
            error = "Name is required.";
        }
        if (Utilities.isEmpty(email)) {
            error = "Email is required.";
        }
        if (Utilities.isEmpty(url)) {
            error = "URL is required.";
        }
    }

    public void processCommentRequest(String commentContent, String name, String email, String url) {
        validateCommentContent(commentContent);
        validateCommentDetails(name, email, url);

        if (error == null) {
            try {
                saveComment();
            } catch (WebloggerException e) {
                error = "Error saving comment: " + e.getMessage();
            }
        }
    }
}