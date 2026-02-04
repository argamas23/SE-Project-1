package org.apache.roller.planet.business.fetcher;

import org.apache.roller.planet.business.PlanetManager;
import org.apache.roller.planet.business.PostManager;
import org.apache.roller.planet.business.RomeFeedFetcherConfig;
import org.apache.roller.planet.business.RomeFeedFetcherResult;
import org.apache.roller.planet.model.Planet;
import org.apache.roller.planet.model.Post;
import org.apache.roller.planet.model.RomeFeed;
import org.apache.roller.planet.model.RomeFeedEntry;
import org.apache.roller.planet.pojos.FeedConfiguration;
import org.apache.roller.planet.pojos.FeedStatistics;
import org.apache.roller.planet.pojos.PlanetStatistics;
import org.apache.roller.planet.util.RomeFeedUtils;
import org.apache.roller.planet.util.StringUtils;
import org.apache.roller.weblogger.business.WebloggerManager;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.roller.weblogger.util.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RomeFeedFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RomeFeedFetcher.class);

    private static final int MAX_FEED_ENTRIES = 20;
    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 4000;

    private final PlanetManager planetManager;
    private final PostManager postManager;

    public RomeFeedFetcher(PlanetManager planetManager, PostManager postManager) {
        this.planetManager = planetManager;
        this.postManager = postManager;
    }

    public RomeFeedFetcherResult fetchFeed(RomeFeedFetcherConfig config) {
        RomeFeedFetcherResult result = new RomeFeedFetcherResult();
        try {
            RomeFeed feed = fetchRomeFeed(config.getFeedUrl());
            if (feed != null) {
                feed.setFeedUrl(config.getFeedUrl());
                result.setFeed(feed);
                List<RomeFeedEntry> entries = feed.getEntries();
                if (entries != null && !entries.isEmpty()) {
                    result.setEntries(entries);
                    Planet planet = planetManager.getPlanet(config.getPlanetId());
                    if (planet != null) {
                        result.setPlanet(planet);
                        populatePostStatistics(planet, entries);
                        populateFeedStatistics(planet, feed);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching feed", e);
            result.setErrorMessage("Error fetching feed: " + e.getMessage());
        }
        return result;
    }

    private RomeFeed fetchRomeFeed(String feedUrl) throws IOException {
        try {
            URL url = new URL(feedUrl);
            RomeFeed feed = RomeFeedUtils.parseRomeFeed(url);
            return feed;
        } catch (MalformedURLException e) {
            LOGGER.error("Invalid feed URL: {}", feedUrl, e);
            return null;
        }
    }

    private void populatePostStatistics(Planet planet, List<RomeFeedEntry> entries) {
        PostStatisticsCalculator calculator = new PostStatisticsCalculator(planet, postManager);
        calculator.calculate(entries);
    }

    private void populateFeedStatistics(Planet planet, RomeFeed feed) {
        FeedStatisticsCalculator calculator = new FeedStatisticsCalculator(planet, planetManager);
        calculator.calculate(feed);
    }

    private static class PostStatisticsCalculator {
        private final Planet planet;
        private final PostManager postManager;

        public PostStatisticsCalculator(Planet planet, PostManager postManager) {
            this.planet = planet;
            this.postManager = postManager;
        }

        public void calculate(List<RomeFeedEntry> entries) {
            for (RomeFeedEntry entry : entries) {
                Post post = postManager.getPostByPermalink(planet.getId(), entry.getPermalink());
                if (post != null) {
                    post.setComments(entry.getComments());
                    post.setTrackbacks(entry.getTrackbacks());
                    postManager.savePost(post);
                }
            }
        }
    }

    private static class FeedStatisticsCalculator {
        private final Planet planet;
        private final PlanetManager planetManager;

        public FeedStatisticsCalculator(Planet planet, PlanetManager planetManager) {
            this.planet = planet;
            this.planetManager = planetManager;
        }

        public void calculate(RomeFeed feed) {
            PlanetStatistics statistics = planetManager.getPlanetStatistics(planet.getId());
            if (statistics != null) {
                statistics.setFeedLastUpdated(feed.getUpdatedAt());
                statistics.setFeedEntries(feed.getEntriesCount());
                planetManager.savePlanetStatistics(statistics);
            }
        }
    }
}