package org.apache.roller.planet.business.fetcher;

import org.apache.roller.planet.PlanetUtils;
import org.apache.roller.planet.business.Feed;
import org.apache.roller.planet.business.FetchResult;
import org.apache.roller.planet.business.PlanetManager;
import org.apache.roller.planet.business.UrlFeedFetcher;
import org.apache.roller.planet.business.XmlParser;
import org.apache.roller.planet.business.exception.FeedFetcherException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class FeedFetcher {

    private static final String APPLICATION_ATOM = "application/atom+xml";
    private static final String APPLICATION_RSS = "application/rss+xml";
    private static final String TEXT_XML = "text/xml";
    private static final String APPLICATION_XML = "application/xml";

    private Logger logger = Logger.getLogger(FeedFetcher.class.getName());
    private PlanetManager planetManager;

    public FeedFetcher(PlanetManager planetManager) {
        this.planetManager = planetManager;
    }

    public FetchResult fetch(Feed feed) throws FeedFetcherException {
        if (feed == null) {
            return new FetchResult(false, "Feed is null");
        }
        try {
            URL url = new URL(feed.getUrl());
            return fetch(url, feed);
        } catch (MalformedURLException e) {
            throw new FeedFetcherException("Error fetching feed", e);
        }
    }

    private FetchResult fetch(URL url, Feed feed) throws FeedFetcherException {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return parseFeed(inputStream, feed);
        } catch (IOException e) {
            throw new FeedFetcherException("Error fetching feed", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.warning("Error closing input stream");
                }
            }
        }
    }

    private FetchResult parseFeed(InputStream inputStream, Feed feed) throws FeedFetcherException {
        XmlParser parser = new XmlParser();
        parser.parse(inputStream);
        String contentType = parser.getContentType();
        if (isSupportedContentType(contentType)) {
            if (contentType.equals(APPLICATION_ATOM)) {
                return parseAtomFeed(parser, feed);
            } else if (contentType.equals(APPLICATION_RSS) || contentType.equals(TEXT_XML) || contentType.equals(APPLICATION_XML)) {
                return parseRssFeed(parser, feed);
            }
        }
        return new FetchResult(false, "Unsupported content type: " + contentType);
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals(APPLICATION_ATOM) ||
                contentType.equals(APPLICATION_RSS) ||
                contentType.equals(TEXT_XML) ||
                contentType.equals(APPLICATION_XML);
    }

    private FetchResult parseAtomFeed(XmlParser parser, Feed feed) throws FeedFetcherException {
        List<org.apache.roller.planet.business.Entry> entries = parser.parseAtomEntries();
        feed.setUpdated(new Date());
        planetManager.updateFeed(feed);
        return new FetchResult(true, "", entries);
    }

    private FetchResult parseRssFeed(XmlParser parser, Feed feed) throws FeedFetcherException {
        List<org.apache.roller.planet.business.Entry> entries = parser.parseRssEntries();
        feed.setUpdated(new Date());
        planetManager.updateFeed(feed);
        return new FetchResult(true, "", entries);
    }
}