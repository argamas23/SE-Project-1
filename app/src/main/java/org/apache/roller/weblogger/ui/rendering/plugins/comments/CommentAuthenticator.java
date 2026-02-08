package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerContext;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.ui.rendering.plugins.comments.CommentAuthenticator;

public class CommentAuthenticator {

    public boolean authenticate(WeblogEntryComment comment, WebloggerContext context) {
        // Get the current user
        User user = context.getUser();

        // If the comment is anonymous, it's not authenticated by default
        if (comment.getCreator().getUsername() == null || comment.getCreator().getUsername().isEmpty()) {
            return false;
        }

        // If the user is not logged in, it's not authenticated
        if (user == null) {
            return false;
        }

        // If the comment creator is the same as the current user, it's authenticated
        if (comment.getCreator().getUsername().equals(user.getUsername())) {
            return true;
        }

        // If the user is an admin, they can authenticate any comment
        if (user.hasRole("admin")) {
            return true;
        }

        // If none of the above conditions are met, it's not authenticated
        return false;
    }
}