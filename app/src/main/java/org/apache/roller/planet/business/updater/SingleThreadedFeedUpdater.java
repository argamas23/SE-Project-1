package org.apache.roller.planet.business.updater;

import org.apache.roller.planet.PlanetContext;
import org.apache.roller.planet.business.SubscriptionManager;
import org.apache.roller.planet.model.Subscription;
import org.apache.roller.planet.model.SubscriptionState;
import org.apache.roller.planet.model.Feed;
import org.apache.roller.planet.model.FeedEntry;
import org.apache.roller.planet.model.FeedParser;
import org.apache.roller.planet.util.URLUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

public class SingleThreadedFeedUpdater {

    private static final Logger LOGGER = Logger.getLogger(SingleThreadedFeedUpdater.class.getName());
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private static final int MAX_FEED_ENTRIES = 100;
    private static final int UPDATE_INTERVAL_HOURS = 2;
    private static final int MAX_FAILED_UPDATES = 5;

    private final PlanetContext context;
    private final SubscriptionManager subscriptionManager;
    private final FeedParser feedParser;
    private final URLUtil urlUtil;
    private final Clock clock;

    public SingleThreadedFeedUpdater(PlanetContext context) {
        this.context = context;
        this.subscriptionManager = context.getSubscriptionManager();
        this.feedParser = new FeedParser();
        this.urlUtil = new URLUtil();
        this.clock = Clock.systemUTC();
    }

    public void updateSubscription(Subscription subscription) throws IOException {
        if (shouldUpdateSubscription(subscription)) {
            try {
                updateSubscriptionInternal(subscription);
            } catch (Exception e) {
                handleUpdateException(subscription, e);
            }
        }
    }

    private boolean shouldUpdateSubscription(Subscription subscription) {
        Instant now = clock.instant();
        Instant lastUpdate = subscription.getLastUpdate();
        return lastUpdate == null || now.minus(UPDATE_INTERVAL_HOURS, ChronoUnit.HOURS).isAfter(lastUpdate);
    }

    private void updateSubscriptionInternal(Subscription subscription) throws IOException {
        Feed feed = fetchFeed(subscription.getFeedUrl());
        updateSubscriptionFeed(feed, subscription);
    }

    private Feed fetchFeed(String feedUrl) throws IOException {
        URL url = urlUtil.toURL(feedUrl);
        return feedParser.parseFeed(url, CONNECTION_TIMEOUT, READ_TIMEOUT);
    }

    private void updateSubscriptionFeed(Feed feed, Subscription subscription) {
        List<FeedEntry> feedEntries = feed.getEntries();
        if (feedEntries.isEmpty()) {
            subscription.setState(SubscriptionState.EMPTY);
        } else {
            subscription.setState(SubscriptionState.VALID);
            updateFeedEntries(feedEntries, subscription);
        }
    }

    private void updateFeedEntries(List<FeedEntry> feedEntries, Subscription subscription) {
        feedEntries.stream()
                .limit(MAX_FEED_ENTRIES)
                .forEach(entry -> subscription.addEntry(entry));
    }

    private void handleUpdateException(Subscription subscription, Exception e) {
        int failedUpdates = subscription.getFailedUpdates();
        failedUpdates++;
        subscription.setFailedUpdates(failedUpdates);
        if (failedUpdates >= MAX_FAILED_UPDATES) {
            subscription.setState(SubscriptionState.FAILED);
        } else {
            subscription.setState(SubscriptionState.ERROR);
        }
        LOGGER.severe("Error updating subscription: " + subscription.getFeedUrl());
    }
}