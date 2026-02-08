package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.roller.weblogger.util.UIUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeblogCommentRequest {

    private static final Logger LOGGER = Logger.getLogger(WeblogCommentRequest.class.getName());
    private static final String APPROVED = "approved";
    private static final String PENDING = "pending";
    private static final String SPAM = "spam";
    private static final String DELETED = "deleted";

    private WeblogCommentRequest() {
    }

    public static void processCommentRequest(HttpServletRequest request) {
        Weblog weblog = getWeblog(request);
        WeblogEntry entry = getEntry(request);
        if (entry == null || weblog == null) {
            return;
        }

        String action = request.getParameter("action");
        CommentAction commentAction = getCommentAction(action);
        switch (commentAction) {
            case APPROVE:
                approveComment(request, entry, weblog);
                break;
            case DELETE:
                deleteComment(request, entry, weblog);
                break;
            case SPAM:
                markCommentAsSpam(request, entry, weblog);
                break;
            case PENDING:
                markCommentAsPending(request, entry, weblog);
                break;
            default:
                break;
        }
    }

    private static Weblog getWeblog(HttpServletRequest request) {
        RollerContext rollerContext = UIUtil.getRollerContext(request);
        return WebloggerFactory.getWeblogger().getWeblogManager().getWeblog(rollerContext.getWeblogHandle());
    }

    private static WeblogEntry getEntry(HttpServletRequest request) {
        WeblogManager weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
        String entryId = request.getParameter("entryId");
        return weblogManager.getWeblogEntry(entryId);
    }

    private static CommentAction getCommentAction(String action) {
        for (CommentAction commentAction : CommentAction.values()) {
            if (commentAction.name().equalsIgnoreCase(action)) {
                return commentAction;
            }
        }
        return CommentAction.NONE;
    }

    private static void approveComment(HttpServletRequest request, WeblogEntry entry, Weblog weblog) {
        WeblogEntryComment comment = getCommentById(request, entry);
        if (comment != null) {
            comment.setStatus(APPROVED);
            WebloggerFactory.getWeblogger().getWeblogEntryManager().saveComment(comment);
        }
    }

    private static void deleteComment(HttpServletRequest request, WeblogEntry entry, Weblog weblog) {
        WeblogEntryComment comment = getCommentById(request, entry);
        if (comment != null) {
            comment.setStatus(DELETED);
            WebloggerFactory.getWeblogger().getWeblogEntryManager().saveComment(comment);
        }
    }

    private static void markCommentAsSpam(HttpServletRequest request, WeblogEntry entry, Weblog weblog) {
        WeblogEntryComment comment = getCommentById(request, entry);
        if (comment != null) {
            comment.setStatus(SPAM);
            WebloggerFactory.getWeblogger().getWeblogEntryManager().saveComment(comment);
        }
    }

    private static void markCommentAsPending(HttpServletRequest request, WeblogEntry entry, Weblog weblog) {
        WeblogEntryComment comment = getCommentById(request, entry);
        if (comment != null) {
            comment.setStatus(PENDING);
            WebloggerFactory.getWeblogger().getWeblogEntryManager().saveComment(comment);
        }
    }

    private static WeblogEntryComment getCommentById(HttpServletRequest request, WeblogEntry entry) {
        String commentId = request.getParameter("commentId");
        List<WeblogEntryComment> comments = new ArrayList<>(entry.getComments());
        for (WeblogEntryComment comment : comments) {
            if (StringUtils.equals(comment.getId(), commentId)) {
                return comment;
            }
        }
        return null;
    }

    private enum CommentAction {
        APPROVE,
        DELETE,
        SPAM,
        PENDING,
        NONE
    }
}