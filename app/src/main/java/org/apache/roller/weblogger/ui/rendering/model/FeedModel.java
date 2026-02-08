package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogCategory;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.ui.rendering.model.utils.WeblogCategoryUtil;
import org.apache.roller.weblogger.ui.rendering.model.utils.WeblogEntryUtil;
import org.apache.roller.weblogger.ui.rendering.model.utils.WeblogUtil;

import java.util.ArrayList;
import java.util.List;

public class FeedModel {

    private static final int MAX_ENTRY_COUNT = 10;

    private Weblog weblog;
    private WeblogEntryManager weblogEntryManager;
    private WeblogManager weblogManager;

    public FeedModel(Weblog weblog, WeblogEntryManager weblogEntryManager, WeblogManager weblogManager) {
        this.weblog = weblog;
        this.weblogEntryManager = weblogEntryManager;
        this.weblogManager = weblogManager;
    }

    public List<WeblogEntry> getRecentEntries() {
        List<WeblogEntry> recentEntries = new ArrayList<>();
        try {
            recentEntries = weblogEntryManager.getRecentWeblogEntries(weblog, MAX_ENTRY_COUNT);
        } catch (WebloggerException e) {
            // Handle exception
        }
        return recentEntries;
    }

    public List<WeblogCategory> getCategories() {
        List<WeblogCategory> categories = new ArrayList<>();
        try {
            categories = WeblogCategoryUtil.getCategories(weblog);
        } catch (WebloggerException e) {
            // Handle exception
        }
        return categories;
    }

    public Weblog getWeblog() {
        return weblog;
    }

    public void setWeblog(Weblog weblog) {
        this.weblog = weblog;
    }

    public WeblogEntryManager getWeblogEntryManager() {
        return weblogEntryManager;
    }

    public void setWeblogEntryManager(WeblogEntryManager weblogEntryManager) {
        this.weblogEntryManager = weblogEntryManager;
    }

    public WeblogManager getWeblogManager() {
        return weblogManager;
    }

    public void setWeblogManager(WeblogManager weblogManager) {
        this.weblogManager = weblogManager;
    }

    public List<WeblogEntryComment> getComments(WeblogEntry entry) {
        List<WeblogEntryComment> comments = new ArrayList<>();
        try {
            comments = weblogEntryManager.getCommentsForEntry(entry);
        } catch (WebloggerException e) {
            // Handle exception
        }
        return comments;
    }

    private boolean isCommentable(WeblogEntry entry) {
        return WeblogEntryUtil.isCommentable(entry);
    }

    private String getEntryPermalink(WeblogEntry entry) {
        return WeblogEntryUtil.getPermalink(entry);
    }

    private String getEntryTitle(WeblogEntry entry) {
        return WeblogEntryUtil.getTitle(entry);
    }

    private String getWeblogTitle() {
        return WeblogUtil.getTitle(weblog);
    }

    private String getWeblogDescription() {
        return WeblogUtil.getDescription(weblog);
    }
}