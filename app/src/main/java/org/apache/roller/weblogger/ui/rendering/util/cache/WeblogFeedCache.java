package org.apache.roller.weblogger.ui.rendering.util.cache;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;

import java.util.List;
import java.util.Map;

public class WeblogFeedCache {

    private static final String CACHE_ENTRY_KEY = "weblogEntry";
    private static final String CACHE_DATE_KEY = "cacheDate";
    private static final String CACHE_TYPE_KEY = "cacheType";
    private static final String FEED_TYPE_RSS = "rss";
    private static final String FEED_TYPE_ATOM = "atom";

    private WeblogManager weblogManager;

    public WeblogFeedCache(WeblogManager weblogManager) {
        this.weblogManager = weblogManager;
    }

    public String getWeblogFeed(Weblog weblog, String feedType) {
        try {
            String feedContent = getFeedFromCache(weblog, feedType);
            if (feedContent == null) {
                feedContent = generateFeed(weblog, feedType);
                cacheFeed(weblog, feedType, feedContent);
            }
            return feedContent;
        } catch (Exception e) {
            throw new WebloggerException("Error generating feed", e);
        }
    }

    private String getFeedFromCache(Weblog weblog, String feedType) {
        Map<String, String> cache = weblogManager.getWeblogCache(weblog);
        if (cache.containsKey(CACHE_TYPE_KEY) && cache.get(CACHE_TYPE_KEY).equals(feedType)) {
            return cache.get(CACHE_ENTRY_KEY);
        }
        return null;
    }

    private String generateFeed(Weblog weblog, String feedType) {
        FeedGenerator feedGenerator;
        if (FEED_TYPE_RSS.equals(feedType)) {
            feedGenerator = new RssFeedGenerator();
        } else if (FEED_TYPE_ATOM.equals(feedType)) {
            feedGenerator = new AtomFeedGenerator();
        } else {
            throw new WebloggerException("Unsupported feed type");
        }
        return feedGenerator.generateFeed(weblog);
    }

    private void cacheFeed(Weblog weblog, String feedType, String feedContent) {
        Map<String, String> cache = weblogManager.getWeblogCache(weblog);
        cache.put(CACHE_TYPE_KEY, feedType);
        cache.put(CACHE_ENTRY_KEY, feedContent);
        cache.put(CACHE_DATE_KEY, String.valueOf(System.currentTimeMillis()));
    }

    private interface FeedGenerator {
        String generateFeed(Weblog weblog);
    }

    private class RssFeedGenerator implements FeedGenerator {

        @Override
        public String generateFeed(Weblog weblog) {
            List<WeblogEntry> entries = weblogManager.getRecentWeblogEntries(weblog, 10);
            StringBuilder feed = new StringBuilder();
            feed.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            feed.append("<rss version=\"2.0\">\n");
            feed.append("<channel>\n");
            feed.append("<title>").append(weblog.getName()).append("</title>\n");
            feed.append("<link>").append(weblog.getHandle()).append("</link>\n");
            for (WeblogEntry entry : entries) {
                feed.append("<item>\n");
                feed.append("<title>").append(entry.getTitle()).append("</title>\n");
                feed.append("<link>").append(entry.getPermalink()).append("</link>\n");
                feed.append("<description>").append(entry.getContent()).append("</description>\n");
                feed.append("<pubDate>").append(entry.getPosted()).append("</pubDate>\n");
                feed.append("</item>\n");
            }
            feed.append("</channel>\n");
            feed.append("</rss>\n");
            return feed.toString();
        }
    }

    private class AtomFeedGenerator implements FeedGenerator {

        @Override
        public String generateFeed(Weblog weblog) {
            List<WeblogEntry> entries = weblogManager.getRecentWeblogEntries(weblog, 10);
            StringBuilder feed = new StringBuilder();
            feed.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            feed.append("<feed xmlns=\"http://www.w3.org/2005/Atom\">\n");
            feed.append("<title>").append(weblog.getName()).append("</title>\n");
            feed.append("<link href=\"").append(weblog.getHandle()).append("\"/>\n");
            for (WeblogEntry entry : entries) {
                feed.append("<entry>\n");
                feed.append("<title>").append(entry.getTitle()).append("</title>\n");
                feed.append("<link href=\"").append(entry.getPermalink()).append("\"/>\n");
                feed.append("<content>").append(entry.getContent()).append("</content>\n");
                feed.append("<updated>").append(entry.getPosted()).append("</updated>\n");
                feed.append("</entry>\n");
            }
            feed.append("</feed>\n");
            return feed.toString();
        }
    }
}