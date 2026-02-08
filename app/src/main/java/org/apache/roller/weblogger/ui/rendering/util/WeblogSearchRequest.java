package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class WeblogSearchRequest {

    private static final Logger LOGGER = Logger.getLogger(WeblogSearchRequest.class.getName());
    private static final String DEFAULT_ORDER = "date";
    private static final String DEFAULT_DIRECTORY = "/entry";
    private static final String DEFAULT_EXTENSION = ".html";

    private int categoryId;
    private int tagId;
    private Weblog weblog;
    private User user;
    private String keywords;
    private String orderBy;
    private int startIndex;
    private int resultsPerPage;

    public WeblogSearchRequest() {
        this.orderBy = DEFAULT_ORDER;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public Weblog getWeblog() {
        return weblog;
    }

    public void setWeblog(Weblog weblog) {
        this.weblog = weblog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public List<WeblogEntry> findEntries() {
        List<WeblogEntry> results = new ArrayList<>();
        try {
            WeblogManager weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
            if (this.weblog != null && this.user != null) {
                results = weblogManager.getWeblogEntries(this.weblog, this.user, this.keywords, this.orderBy, this.startIndex, this.resultsPerPage);
            } else if (this.weblog != null) {
                results = weblogManager.getWeblogEntries(this.weblog, this.keywords, this.orderBy, this.startIndex, this.resultsPerPage);
            } else if (this.user != null) {
                results = weblogManager.getUserWeblogEntries(this.user, this.keywords, this.orderBy, this.startIndex, this.resultsPerPage);
            }
        } catch (WebloggerException e) {
            LOGGER.log(Level.SEVERE, "Error searching for entries", e);
        }
        return results;
    }

    public String getLink() {
        return WebloggerRuntimeConfig.getBaseUrl() + DEFAULT_DIRECTORY + DEFAULT_EXTENSION;
    }
}