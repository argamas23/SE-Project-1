package org.apache.roller.planet.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.roller.planet.PlanetConstants;
import org.apache.roller.planet.PlanetRuntimeException;
import org.apache.roller.planet.config.validator.PlanetConfigurationValidator;
import org.apache.roller.planet.model.Planet;
import org.apache.roller.planet.model.PlanetChannel;
import org.apache.roller.planet.model.PlanetWebsite;

import java.util.List;
import java.util.Map;

public class PlanetRuntimeConfig {

    private static final int DEFAULT_FEED_FETCH_ATTEMPTS = 3;
    private static final int DEFAULT_FEED_FETCH_DELAY = 1000;
    private static final int DEFAULT_FEED_MAX_ENTRIES = 10;

    private Planet planet;
    private PlanetConfigurationValidator validator;

    public PlanetRuntimeConfig(Planet planet, PlanetConfigurationValidator validator) {
        this.planet = planet;
        this.validator = validator;
    }

    public void validateConfiguration() {
        validator.validate(planet);
    }

    public void configurePlanet() {
        configureChannels();
        configureWebsites();
        configureFeed();
    }

    private void configureChannels() {
        List<PlanetChannel> channels = planet.getChannels();
        for (PlanetChannel channel : channels) {
            if (StringUtils.isEmpty(channel.getTitle())) {
                throw new PlanetRuntimeException("Channel title is required");
            }
        }
    }

    private void configureWebsites() {
        List<PlanetWebsite> websites = planet.getWebsites();
        for (PlanetWebsite website : websites) {
            if (StringUtils.isEmpty(website.getUrl())) {
                throw new PlanetRuntimeException("Website URL is required");
            }
        }
    }

    private void configureFeed() {
        int feedFetchAttempts = getFeedFetchAttempts();
        int feedFetchDelay = getFeedFetchDelay();
        int feedMaxEntries = getFeedMaxEntries();

        if (feedFetchAttempts < 1) {
            throw new PlanetRuntimeException("Feed fetch attempts must be greater than 0");
        }
        if (feedFetchDelay < 0) {
            throw new PlanetRuntimeException("Feed fetch delay must be greater than or equal to 0");
        }
        if (feedMaxEntries < 1) {
            throw new PlanetRuntimeException("Feed max entries must be greater than 0");
        }
    }

    private int getFeedFetchAttempts() {
        String attempts = planet.getFeedFetchAttempts();
        return attempts != null ? Integer.parseInt(attempts) : DEFAULT_FEED_FETCH_ATTEMPTS;
    }

    private int getFeedFetchDelay() {
        String delay = planet.getFeedFetchDelay();
        return delay != null ? Integer.parseInt(delay) : DEFAULT_FEED_FETCH_DELAY;
    }

    private int getFeedMaxEntries() {
        String maxEntries = planet.getFeedMaxEntries();
        return maxEntries != null ? Integer.parseInt(maxEntries) : DEFAULT_FEED_MAX_ENTRIES;
    }

    public void loadPlanetConfig() {
        // Load planet configuration from database or file
        // For demonstration purposes, assume it's loaded from a map
        Map<String, String> config = planet.getConfig();
        planet.setFeedFetchAttempts(config.get(PlanetConstants.FEED_FETCH_ATTEMPTS));
        planet.setFeedFetchDelay(config.get(PlanetConstants.FEED_FETCH_DELAY));
        planet.setFeedMaxEntries(config.get(PlanetConstants.FEED_MAX_ENTRIES));
    }
}