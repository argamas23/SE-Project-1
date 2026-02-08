package org.apache.roller.weblogger.ui.rendering.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryCategory;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.business.UserManager;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.roller.weblogger.util.StringUtils;

public class ParsedRequest {

    private String weblogHandle;
    private String category;
    private String entryId;
    private String page;
    private String date;
    private String tag;
    private String username;
    private boolean isAdminRequest;
    private RollerContext context;

    public ParsedRequest(RollerContext context) {
        this.context = context;
        parseRequest();
    }

    private void parseRequest() {
        String requestURI = context.getRequest().getRequestURI();
        Pattern pattern = Pattern.compile("/(weblog)/(\\w+)(?:/(\\w+))?(?:/(\\w+))?(?:/(\\w+))?.*");
        Matcher matcher = pattern.matcher(requestURI);

        if (matcher.matches()) {
            weblogHandle = matcher.group(2);
            category = matcher.group(3);
            entryId = matcher.group(4);
            page = matcher.group(5);
        } else {
            throw new WebloggerException("Invalid request URI");
        }
    }

    private void extractDateFromRequest() {
        if (StringUtils.isEmpty(date)) {
            String[] parts = context.getRequest().getRequestURI().split("/");
            if (parts.length > 4 && parts[4].matches("\\d{4}-\\d{2}-\\d{2}")) {
                date = parts[4];
            }
        }
    }

    private void extractTagFromRequest() {
        if (StringUtils.isEmpty(tag)) {
            String[] parts = context.getRequest().getRequestURI().split("/");
            if (parts.length > 4 && parts[4].startsWith("tag")) {
                tag = parts[4].substring(4);
            }
        }
    }

    private void extractUsernameFromRequest() {
        if (StringUtils.isEmpty(username)) {
            String[] parts = context.getRequest().getRequestURI().split("/");
            if (parts.length > 4 && parts[4].startsWith("user")) {
                username = parts[4].substring(5);
            }
        }
    }

    public String getWeblogHandle() {
        return weblogHandle;
    }

    public String getCategory() {
        return category;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getPage() {
        return page;
    }

    public String getDate() {
        if (StringUtils.isEmpty(date)) {
            extractDateFromRequest();
        }
        return date;
    }

    public String getTag() {
        if (StringUtils.isEmpty(tag)) {
            extractTagFromRequest();
        }
        return tag;
    }

    public String getUsername() {
        if (StringUtils.isEmpty(username)) {
            extractUsernameFromRequest();
        }
        return username;
    }

    public boolean isAdminRequest() {
        return isAdminRequest;
    }

    private void setAdminRequest(User user) {
        WeblogManager weblogManager = WebloggerFactory.getWeblogManager();
        isAdminRequest = weblogManager.isSystemAdmin(user) || weblogManager.isUserWeblogAdmin(user, weblogHandle);
    }

    public List<WeblogEntryCategory> getCategories() {
        WeblogManager weblogManager = WebloggerFactory.getWeblogManager();
        return weblogManager.getCategories(weblogHandle);
    }

    public List<WeblogEntryComment> getComments() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogEntryManager();
        return entryManager.getComments(entryId);
    }

    public WeblogEntry getEntry() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogEntryManager();
        return entryManager.getWeblogEntry(entryId);
    }

    public List<WeblogEntry> getRecentEntries() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogEntryManager();
        return entryManager.getRecentWeblogEntries(weblogHandle);
    }

    public List<WeblogEntry> getEntriesByCategory() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogEntryManager();
        return entryManager.getWeblogEntriesByCategory(weblogHandle, category);
    }

    public List<WeblogEntry> getEntriesByDate() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogEntryManager();
        return entryManager.getWeblogEntriesByDate(weblogHandle, getDate());
    }

    public List<WeblogEntry> getEntriesByTag() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogEntryManager();
        return entryManager.getWeblogEntriesByTag(weblogHandle, getTag());
    }

    public User getUser() {
        UserManager userManager = WebloggerFactory.getUserManager();
        return userManager.getUser(getUsername());
    }

    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("weblog", getWeblog());
        model.put("entries", getRecentEntries());
        model.put("categories", getCategories());
        model.put("entry", getEntry());
        model.put("comments", getComments());
        model.put("user", getUser());
        return model;
    }

    private Object getWeblog() {
        WeblogManager weblogManager = WebloggerFactory.getWeblogManager();
        return weblogManager.getWeblog(weblogHandle);
    }

    public void initializeUser() {
        User user = context.getRequest().getRemoteUser();
        if (user != null && !isAdminRequest) {
            setAdminRequest(user);
        }
    }
}