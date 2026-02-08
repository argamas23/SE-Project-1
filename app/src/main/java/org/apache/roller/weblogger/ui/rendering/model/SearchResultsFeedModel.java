package org.apache.roller.weblogger.ui.rendering.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFeedModel {

    private String query;
    private List<SearchResult> results;
    private int startIndex;
    private int totalResults;
    private int maxResults;

    public SearchResultsFeedModel(String query, List<SearchResult> results, int startIndex, int totalResults, int maxResults) {
        this.query = query;
        this.results = results;
        this.startIndex = startIndex;
        this.totalResults = totalResults;
        this.maxResults = maxResults;
    }

    public String getQuery() {
        return query;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void processResults(List<SearchResult> results) {
        ResultProcessor resultProcessor = new ResultProcessor();
        resultProcessor.processResults(results);
    }

    private class ResultProcessor {
        private static final int DEFAULT_MAX_RESULTS = 10;

        public void processResults(List<SearchResult> results) {
            if (results.isEmpty()) {
                return;
            }

            int numberOfPages = calculateNumberOfPages(results.size());
            int startIndex = calculateStartIndex(results.size(), numberOfPages);

            processEachResult(results, numberOfPages, startIndex);
        }

        private int calculateNumberOfPages(int totalResults) {
            return (int) Math.ceil((double) totalResults / getMaxResults());
        }

        private int calculateStartIndex(int totalResults, int numberOfPages) {
            return (numberOfPages - 1) * getMaxResults();
        }

        private void processEachResult(List<SearchResult> results, int numberOfPages, int startIndex) {
            for (SearchResult result : results) {
                processResult(result, numberOfPages, startIndex);
            }
        }

        private void processResult(SearchResult result, int numberOfPages, int startIndex) {
            if (result != null) {
                handleResult(result);
            }
        }

        private void handleResult(SearchResult result) {
            if (result.getType() == SearchResultType.POST) {
                handlePostResult(result);
            } else if (result.getType() == SearchResultType.COMMENT) {
                handleCommentResult(result);
            } else {
                handleOtherResult(result);
            }
        }

        private void handlePostResult(SearchResult result) {
            PostResultHandler postResultHandler = new PostResultHandler();
            postResultHandler.handleResult(result);
        }

        private void handleCommentResult(SearchResult result) {
            CommentResultHandler commentResultHandler = new CommentResultHandler();
            commentResultHandler.handleResult(result);
        }

        private void handleOtherResult(SearchResult result) {
            OtherResultHandler otherResultHandler = new OtherResultHandler();
            otherResultHandler.handleResult(result);
        }
    }

    private enum SearchResultType {
        POST,
        COMMENT,
        OTHER
    }

    private interface ResultHandler {
        void handleResult(SearchResult result);
    }

    private class PostResultHandler implements ResultHandler {
        @Override
        public void handleResult(SearchResult result) {
            // handle post result
        }
    }

    private class CommentResultHandler implements ResultHandler {
        @Override
        public void handleResult(SearchResult result) {
            // handle comment result
        }
    }

    private class OtherResultHandler implements ResultHandler {
        @Override
        public void handleResult(SearchResult result) {
            // handle other result
        }
    }

    public static class SearchResult {
        private String id;
        private SearchResultType type;

        public SearchResult(String id, SearchResultType type) {
            this.id = id;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public SearchResultType getType() {
            return type;
        }
    }
}