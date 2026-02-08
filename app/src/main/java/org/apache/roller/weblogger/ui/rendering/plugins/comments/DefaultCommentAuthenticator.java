package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerContext;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogComment;
import org.apache.roller.weblogger.util.Util;

public class DefaultCommentAuthenticator {

    private static final int MAX_COMMENT_LENGTH = 1024;

    public DefaultCommentAuthenticator() {
    }

    public boolean authenticate(WebloggerContext context, WeblogEntry entry, WeblogComment comment) {
        if (comment == null) {
            return false;
        }

        if (Util.isEmpty(comment.getName())) {
            return false;
        }

        if (Util.isEmpty(comment.getEmail())) {
            return false;
        }

        if (Util.isEmpty(comment.getContent())) {
            return false;
        }

        if (comment.getContent().length() > MAX_COMMENT_LENGTH) {
            return false;
        }

        return true;
    }

    public void init() {
        // Intentionally left blank, initialization is not required.
    }

    public void destroy() {
        // Intentionally left blank, cleanup is not required.
    }
}