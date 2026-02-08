package org.apache.roller.weblogger.ui.rendering;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.biz.WebloggerFactory;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeblogRequestMapper {

    private static final Log log = LogFactory.getLog(WeblogRequestMapper.class);
    private static final int DATE_FORMAT_LENGTH = 10;
    private static final int COMMENT_LIMIT = 10;

    public void mapRequest() {
        Weblog weblog = getWeblog();
        User user = getUser();
        String action = getAction();

        switch (action) {
            case "view":
                viewWeblog(weblog, user);
                break;
            case "viewEntry":
                viewWeblogEntry(weblog, user);
                break;
            default:
                handleUnknownAction(action);
                break;
        }
    }

    private Weblog getWeblog() {
        // logic to get the weblog
        return WebloggerFactory.getWeblogger().getWeblog("weblog-handle");
    }

    private User getUser() {
        // logic to get the user
        return WebloggerFactory.getWeblogger().getUser("username");
    }

    private String getAction() {
        // logic to get the action
        return "view";
    }

    private void viewWeblog(Weblog weblog, User user) {
        List<WeblogEntry> entries = getWeblogEntries(weblog);
        renderWeblog(weblog, user, entries);
    }

    private List<WeblogEntry> getWeblogEntries(Weblog weblog) {
        List<WeblogEntry> entries = new ArrayList<>();
        // logic to get the weblog entries
        for (int i = 0; i < 5; i++) {
            WeblogEntry entry = new WeblogEntry();
            entry.setDate(new Date());
            entry.setSubject("subject-" + i);
            entries.add(entry);
        }
        return entries;
    }

    private void renderWeblog(Weblog weblog, User user, List<WeblogEntry> entries) {
        // logic to render the weblog
    }

    private void viewWeblogEntry(Weblog weblog, User user) {
        WeblogEntry entry = getWeblogEntry(weblog);
        List<WeblogEntryComment> comments = getWeblogEntryComments(entry);
        renderWeblogEntry(weblog, user, entry, comments);
    }

    private WeblogEntry getWeblogEntry(Weblog weblog) {
        // logic to get the weblog entry
        return new WeblogEntry();
    }

    private List<WeblogEntryComment> getWeblogEntryComments(WeblogEntry entry) {
        List<WeblogEntryComment> comments = new ArrayList<>();
        // logic to get the weblog entry comments
        for (int i = 0; i < COMMENT_LIMIT; i++) {
            WeblogEntryComment comment = new WeblogEntryComment();
            comment.setDate(new Date());
            comment.setSubject("subject-" + i);
            comments.add(comment);
        }
        return comments;
    }

    private void renderWeblogEntry(Weblog weblog, User user, WeblogEntry entry, List<WeblogEntryComment> comments) {
        // logic to render the weblog entry
    }

    private void handleUnknownAction(String action) {
        // logic to handle unknown action
    }

    public static void main(String[] args) {
        WeblogRequestMapper mapper = new WeblogRequestMapper();
        mapper.mapRequest();
    }
}