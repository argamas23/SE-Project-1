package org.apache.roller.weblogger.business.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResultList {

    private final String query;
    private final List<SearchResult> results;
    private final int totalResults;
    private final int startIndex;
    private final int endIndex;

    public SearchResultList(String query, int totalResults, int startIndex, int endIndex) {
        this.query = query;
        this.results = new ArrayList<>();
        this.totalResults = totalResults;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void addResult(SearchResult result) {
        this.results.add(result);
    }

    public String getQuery() {
        return query;
    }

    public List<SearchResult> getResults() {
        return new ArrayList<>(results);
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public static class SearchResult {
        private final String title;
        private final String summary;
        private final String url;
        private final String blogName;
        private final String categoryName;

        public SearchResult(String title, String summary, String url, String blogName, String categoryName) {
            this.title = title;
            this.summary = summary;
            this.url = url;
            this.blogName = blogName;
            this.categoryName = categoryName;
        }

        public String getTitle() {
            return title;
        }

        public String getSummary() {
            return summary;
        }

        public String getUrl() {
            return url;
        }

        public String getBlogName() {
            return blogName;
        }

        public String getCategoryName() {
            return categoryName;
        }
    }
}