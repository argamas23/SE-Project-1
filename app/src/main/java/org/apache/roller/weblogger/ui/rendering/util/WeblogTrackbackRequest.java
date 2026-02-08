package org.apache.roller.weblogger.ui.rendering.util;

import java.util.ArrayList;
import java.util.List;

public class WeblogTrackbackRequest {
    private static final String TRACKBACK_TITLE = "title";
    private static final String TRACKBACK_URL = "url";
    private static final String TRACKBACK_BLOG_NAME = "blog_name";
    private static final String TRACKBACK_EXCERPT = "excerpt";

    private String title;
    private String url;
    private String blogName;
    private String excerpt;

    public WeblogTrackbackRequest(String title, String url, String blogName, String excerpt) {
        this.title = title;
        this.url = url;
        this.blogName = blogName;
        this.excerpt = excerpt;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getBlogName() {
        return blogName;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public static class TrackbackRequestBuilder {
        private String title;
        private String url;
        private String blogName;
        private String excerpt;

        public TrackbackRequestBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public TrackbackRequestBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public TrackbackRequestBuilder withBlogName(String blogName) {
            this.blogName = blogName;
            return this;
        }

        public TrackbackRequestBuilder withExcerpt(String excerpt) {
            this.excerpt = excerpt;
            return this;
        }

        public WeblogTrackbackRequest build() {
            return new WeblogTrackbackRequest(title, url, blogName, excerpt);
        }
    }

    public static TrackbackRequestBuilder builder() {
        return new TrackbackRequestBuilder();
    }

    public static WeblogTrackbackRequest createTrackbackRequest(String title, String url, String blogName, String excerpt) {
        return builder()
                .withTitle(title)
                .withUrl(url)
                .withBlogName(blogName)
                .withExcerpt(excerpt)
                .build();
    }

    public static List<WeblogTrackbackRequest> createTrackbackRequests(List<String> titles, List<String> urls, List<String> blogNames, List<String> excerpts) {
        List<WeblogTrackbackRequest> trackbackRequests = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            trackbackRequests.add(createTrackbackRequest(titles.get(i), urls.get(i), blogNames.get(i), excerpts.get(i)));
        }
        return trackbackRequests;
    }
}