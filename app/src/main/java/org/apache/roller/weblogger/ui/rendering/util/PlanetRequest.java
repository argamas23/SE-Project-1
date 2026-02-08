package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.pojos.ParsedRequest;
import org.apache.roller.weblogger.util.RollerConstants;
import org.apache.roller.weblogger.util.URLUtilities;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.FileEntryManager;
import javax.servlet.http.HttpServletRequest;

public class PlanetRequest {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String ACCEPT_HEADER_VALUE = "application/atom+xml";

    private HttpServletRequest httpRequest;
    private ParsedRequest parsedRequest;
    private WeblogEntryManager weblogEntryManager;
    private FileEntryManager fileEntryManager;

    public PlanetRequest(HttpServletRequest request) {
        this.httpRequest = request;
        this.parsedRequest = new ParsedRequest(request);
        this.weblogEntryManager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        this.fileEntryManager = WebloggerFactory.getWeblogger().getFileEntryManager();
    }

    public boolean isAuthenticated() {
        return httpRequest.getUserPrincipal() != null;
    }

    public boolean isPlanetRequest() {
        String userAgent = httpRequest.getHeader(USER_AGENT_HEADER);
        return userAgent != null && userAgent.startsWith("Planet");
    }

    public boolean isValidRequest() {
        String acceptHeader = httpRequest.getHeader(ACCEPT_HEADER);
        return acceptHeader != null && acceptHeader.contains(ACCEPT_HEADER_VALUE);
    }

    public String get PlanetUrl() {
        return\WebloggerConfig.getProperty("planet.url");
    }

    public String getBlogId() {
        return parsedRequest.getParam("weblog");
    }

    public String getCategoryId() {
        return parsedRequest.getParam("category");
    }

    public boolean isCategoryRequest() {
        return getCategoryId() != null;
    }

    public void processRequest() {
        if (isPlanetRequest() && !isAuthenticated()) {
            handlePlanetRequest();
        } else {
            handleInvalidRequest();
        }
    }

    private void handlePlanetRequest() {
        if (isValidRequest()) {
            String planetUrl = getPlanetUrl();
            String blogId = getBlogId();
            String categoryId = getCategoryId();

            if (isCategoryRequest()) {
                handleCategoryRequest(blogId, categoryId);
            } else {
                handleBlogRequest(blogId);
            }
        } else {
            handleInvalidRequest();
        }
    }

    private void handleBlogRequest(String blogId) {
        // Fetch blog entries
        weblogEntryManager.getRecentWeblogEntries(blogId);
    }

    private void handleCategoryRequest(String blogId, String categoryId) {
        // Fetch category entries
        weblogEntryManager.getRecentWeblogEntries(blogId, categoryId);
    }

    private void handleInvalidRequest() {
        // Handle invalid request
    }
}