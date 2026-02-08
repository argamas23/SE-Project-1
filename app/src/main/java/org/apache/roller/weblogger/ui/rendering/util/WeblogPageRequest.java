package org.apache.roller.weblogger.ui.rendering.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class WeblogPageRequest {

    private static final String INDEX = "index";
    private static final String DATE = "date";
    private static final String CATEGORY = "category";
    private static final String TAG = "tag";
    private static final String ENTRY = "entry";
    private static final String USER = "user";
    private static final String PAGE = "page";
    private static final String FEED = "feed";

    private final HttpServletRequest request;
    private final String[] pathElements;

    public WeblogPageRequest(HttpServletRequest request) {
        this.request = request;
        this.pathElements = getPathElements(request);
    }

    private String[] getPathElements(HttpServletRequest request) {
        String[] elements = request.getPathInfo().split("/");
        return Arrays.stream(elements)
                .filter(element -> !element.isEmpty())
                .toArray(String[]::new);
    }

    public String getType() {
        if (pathElements.length == 0) {
            return INDEX;
        }
        return pathElements[0];
    }

    public String getSecondLevelType() {
        if (pathElements.length < 2) {
            return null;
        }
        return pathElements[1];
    }

    public boolean isIndexPage() {
        return getType().equals(INDEX);
    }

    public boolean isDateArchivePage() {
        return getType().equals(DATE);
    }

    public boolean isCategoryPage() {
        return getType().equals(CATEGORY);
    }

    public boolean isTagPage() {
        return getType().equals(TAG);
    }

    public boolean isEntryPage() {
        return getType().equals(ENTRY);
    }

    public boolean isUserPage() {
        return getType().equals(USER);
    }

    public boolean isPagePage() {
        return getType().equals(PAGE);
    }

    public boolean isFeedPage() {
        return getType().equals(FEED);
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}