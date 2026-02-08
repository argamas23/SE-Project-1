package org.apache.roller.weblogger.ui.rendering.util;

import java.util.HashMap;
import java.util.Map;

public class WeblogFeedRequest {

    private static final String DEFAULT_FEED_TYPE = "rss";
    private static final String DEFAULT_ITEMS_PER_PAGE = "10";
    private static final String CATEGORY_QUERY_PARAMETER = "category";
    private static final String TAGS_QUERY_PARAMETER = "tags";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String PUBLISHED_START_DATE_QUERY_PARAMETER = "publishedStartDate";
    private static final String PUBLISHED_END_DATE_QUERY_PARAMETER = "publishedEndDate";
    private static final String WEBSITE_LANGUAGE_QUERY_PARAMETER = "language";

    private String weblogHandler;
    private String feedType;
    private String itemsPerPage;
    private String category;
    private String[] tags;
    private String publishedStartDate;
    private String publishedEndDate;
    private String websiteLanguage;

    public WeblogFeedRequest(String weblogHandler, Map<String, String> queryParameters) {
        this.weblogHandler = weblogHandler;
        this.feedType = getFeedType(queryParameters);
        this.itemsPerPage = getItemsPerPage(queryParameters);
        this.category = getCategory(queryParameters);
        this.tags = getTags(queryParameters);
        this.publishedStartDate = getPublishedStartDate(queryParameters);
        this.publishedEndDate = getPublishedEndDate(queryParameters);
        this.websiteLanguage = getWebsiteLanguage(queryParameters);
    }

    private String getFeedType(Map<String, String> queryParameters) {
        return queryParameters.getOrDefault("feedType", DEFAULT_FEED_TYPE);
    }

    private String getItemsPerPage(Map<String, String> queryParameters) {
        return queryParameters.getOrDefault("itemsPerPage", DEFAULT_ITEMS_PER_PAGE);
    }

    private String getCategory(Map<String, String> queryParameters) {
        return queryParameters.get(CATEGORY_QUERY_PARAMETER);
    }

    private String[] getTags(Map<String, String> queryParameters) {
        String tags = queryParameters.get(TAGS_QUERY_PARAMETER);
        return tags != null ? tags.split(",") : new String[0];
    }

    private String getPublishedStartDate(Map<String, String> queryParameters) {
        return queryParameters.get(PUBLISHED_START_DATE_QUERY_PARAMETER);
    }

    private String getPublishedEndDate(Map<String, String> queryParameters) {
        return queryParameters.get(PUBLISHED_END_DATE_QUERY_PARAMETER);
    }

    private String getWebsiteLanguage(Map<String, String> queryParameters) {
        return queryParameters.get(WEBSITE_LANGUAGE_QUERY_PARAMETER);
    }

    public String getWeblogHandler() {
        return weblogHandler;
    }

    public String getFeedType() {
        return feedType;
    }

    public String getItemsPerPage() {
        return itemsPerPage;
    }

    public String getCategory() {
        return category;
    }

    public String[] getTags() {
        return tags;
    }

    public String getPublishedStartDate() {
        return publishedStartDate;
    }

    public String getPublishedEndDate() {
        return publishedEndDate;
    }

    public String getWebsiteLanguage() {
        return websiteLanguage;
    }
}