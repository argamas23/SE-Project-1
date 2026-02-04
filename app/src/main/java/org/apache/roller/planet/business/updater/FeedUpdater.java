package org.apache.roller.planet.business.updater;

import org.apache.roller.planet.PlanetManager;
import org.apache.roller.planet.business.Planet;
import org.apache.roller.planet.business.PlanetFactory;
import org.apache.roller.planet.business.Entry;
import org.apache.roller.planet.business.Feed;
import org.apache.roller.planet.business.FeedFactory;
import org.apache.roller.planet.business.UpdateStatus;
import org.apache.roller.planet.exceptions.PlanetException;
import org.apache.roller.planet.utils.RSSUtils;
import org.apache.roller.planet.utils.FeedUtils;
import org.apache.roller.planet.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedUpdater {

    private static final Logger logger = LoggerFactory.getLogger(FeedUpdater.class);

    private final PlanetManager planetManager;
    private final PlanetFactory planetFactory;
    private final FeedFactory feedFactory;

    public FeedUpdater(PlanetManager planetManager, PlanetFactory planetFactory, FeedFactory feedFactory) {
        this.planetManager = planetManager;
        this.planetFactory = planetFactory;
        this.feedFactory = feedFactory;
    }

    public void updateFeed(Feed feed) throws PlanetException {
        updateFeed(feed, false);
    }

    public void updateFeed(Feed feed, boolean forceUpdate) throws PlanetException {
        try {
            checkFeedForUpdate(feed, forceUpdate);
        } catch (Exception e) {
            throw new PlanetException("Error updating feed", e);
        }
    }

    private void checkFeedForUpdate(Feed feed, boolean forceUpdate) {
        if (feed == null) {
            logger.warn("Feed is null, cannot update");
            return;
        }

        planetManager.startUpdate(feed);

        if (forceUpdate || shouldUpdateFeed(feed)) {
            Planet planet = planetFactory.getPlanet(feed.getPlanetId());
            processFeedUpdate(planet, feed);
        }

        planetManager.endUpdate(feed);
    }

    private boolean shouldUpdateFeed(Feed feed) {
        return DateUtils.timeSinceFeedUpdate(feed.getLastUpdated()) > feed.getUpdateInterval();
    }

    private void processFeedUpdate(Planet planet, Feed feed) {
        UpdateStatus updateStatus = readFeedAndUpdateEntries(planet, feed);

        if (updateStatus.getNewEntryCount() > 0) {
            planetManager.addNewEntriesToPlanet(planet, updateStatus.getNewEntries());
        }

        planetManager.updatePlanet(planet);
        planetManager.updateFeed(feed);
    }

    private UpdateStatus readFeedAndUpdateEntries(Planet planet, Feed feed) {
        UpdateStatus updateStatus = new UpdateStatus();

        try {
            FeedUtils.parseFeed(feed.getUrl(), feedProcessor -> {
                Entry entry = feedProcessor.getEntry();
                if (isEntryNew(planet, entry)) {
                    updateStatus.addNewEntry(entry);
                    createEntry(feed, entry);
                }
            });

        } catch (Exception e) {
            logger.error("Error reading feed: {}", e.getMessage());
        }

        return updateStatus;
    }

    private boolean isEntryNew(Planet planet, Entry entry) {
        return !planet.getEntries().contains(entry);
    }

    private void createEntry(Feed feed, Entry entry) {
        planetManager.addEntryToFeed(feed, entry);
    }
}