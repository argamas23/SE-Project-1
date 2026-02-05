package org.apache.roller.weblogger.business.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult {
    private String blogHandle;
    private String url;
    private String title;
    private String text;
    private String permalink;
    private String category;
    private String username;
    private String date;

    public SearchResult(String blogHandle, String url, String title, String text, String permalink, String category, String username, String date) {
        this.blogHandle = blogHandle;
        this.url = url;
        this.title = title;
        this.text = text;
        this.permalink = permalink;
        this.category = category;
        this.username = username;
        this.date = date;
    }

    public String getBlogHandle() {
        return blogHandle;
    }

    public void setBlogHandle(String blogHandle) {
        this.blogHandle = blogHandle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

class SearchResultMap {
    private List<SearchResult> searchResults;

    public SearchResultMap(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }
}