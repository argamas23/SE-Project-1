package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.business.UserManager;
import org.apache.roller.weblogger.business.FileManager;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.File;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.util.Date;

public class PageModel implements Serializable {

    private Weblog weblog;
    private WeblogEntry entry;
    private Map<String, Object> requestMap;
    private String pageUrl;
    private Date pageDate;

    public PageModel(Weblog weblog, WeblogEntry entry, Map<String, Object> requestMap) {
        this.weblog = weblog;
        this.entry = entry;
        this.requestMap = requestMap;
        this.pageUrl = getUrl();
        this.pageDate = getPageDate();
    }

    private String getUrl() {
        WebloggerFactory webloggerFactory = WebloggerFactory.getWebloggerFactory();
        WeblogManager weblogManager = webloggerFactory.getWeblogManager();
        return weblogManager.getWeblogURL(weblog);
    }

    private Date getPageDate() {
        if (entry != null) {
            return entry.getPublished();
        } else {
            return new Date();
        }
    }

    public Weblog getWeblog() {
        return weblog;
    }

    public WeblogEntry getEntry() {
        return entry;
    }

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public Date getPageDate() {
        return pageDate;
    }

    public String getFeedId() {
        return "feed_" + weblog.getHandle();
    }

    public String getWeblogTagUrl() {
        return "/tag/" + weblog.getHandle() + "/";
    }

    public String getDayUrl() {
        return "/day/" + weblog.getHandle() + "/";
    }

    public String getMonthUrl() {
        return "/month/" + weblog.getHandle() + "/";
    }

    public String getYearUrl() {
        return "/year/" + weblog.getHandle() + "/";
    }

    public boolean isOwner(WebloggerFactory webloggerFactory) {
        UserManager userManager = webloggerFactory.getUserManager();
        User user = userManager.getUser(requestMap);
        return user != null && user.getId().equals(weblog.getOwnerId());
    }
}