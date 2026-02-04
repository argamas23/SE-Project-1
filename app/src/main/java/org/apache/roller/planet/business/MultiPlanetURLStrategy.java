package org.apache.roller.planet.business;

import org.apache.roller.planet.Planet;
import org.apache.roller.planet.PlanetManager;
import org.apache.roller.planet.PlanetURLStrategy;
import org.apache.roller.planet.PlanetFeed;
import org.apache.roller.planet.PlanetFeedManager;
import org.apache.roller.util.URLEncoder;
import org.apache.roller.util.UrlUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class MultiPlanetURLStrategy implements PlanetURLStrategy {

    private static final int PATH_LENGTH = 38;
    private static final int PATH_SEGMENT_LENGTH = 38;

    private final PlanetManager planetManager;
    private final PlanetFeedManager planetFeedManager;

    public MultiPlanetURLStrategy(PlanetManager planetManager, PlanetFeedManager planetFeedManager) {
        this.planetManager = planetManager;
        this.planetFeedManager = planetFeedManager;
    }

    @Override
    public String getPlanetFeedUrl(Planet planet) {
        return getBasePlanetUrl(planet) + "/feed";
    }

    @Override
    public String getPlanetEntryUrl(Planet planet, String entryId) {
        return getBasePlanetUrl(planet) + "/entry/" + entryId;
    }

    @Override
    public String getPlanetTagUrl(Planet planet, String tag) {
        return getBasePlanetUrl(planet) + "/tag/" + URLEncoder.encode(tag);
    }

    @Override
    public String getPlanetAuthorUrl(Planet planet, String author) {
        return getBasePlanetUrl(planet) + "/author/" + URLEncoder.encode(author);
    }

    @Override
    public String getPlanetSearchUrl(Planet planet, String query) {
        return getBasePlanetUrl(planet) + "/search/" + URLEncoder.encode(query);
    }

    private String getBasePlanetUrl(Planet planet) {
        return getPlanetUrlPath(planet);
    }

    private String getPlanetUrlPath(Planet planet) {
        String urlPath = planet.getUrlPattern();

        if (urlPath == null || urlPath.isEmpty()) {
            urlPath = planet.getId();
        }

        return formatUrlPath(urlPath);
    }

    private String formatUrlPath(String urlPath) {
        if (urlPath.length() > PATH_LENGTH) {
            return formatLongUrlPath(urlPath);
        } else {
            return formatShortUrlPath(urlPath);
        }
    }

    private String formatLongUrlPath(String urlPath) {
        return urlPath.substring(0, PATH_SEGMENT_LENGTH) + "/" + urlPath.substring(PATH_SEGMENT_LENGTH);
    }

    private String formatShortUrlPath(String urlPath) {
        return urlPath;
    }

    @Override
    public String getPlanetUrl(Planet planet) {
        try {
            URI uri = new URI(getBasePlanetUrl(planet));
            return UrlUtils.getAbsoluteUrl(uri.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPlanetFeedUrl(PlanetFeed planetFeed) {
        return getPlanetFeedUrl(planetFeed.getPlanet()) + "/" + planetFeed.getFeedType();
    }

    @Override
    public List<String> getPlanetFeeds(Planet planet) {
        List<String> feeds = planetManager.getPlanetFeeds(planet);

        if (feeds == null || feeds.isEmpty()) {
            return getPlanetDefaultFeeds(planet);
        }

        return feeds;
    }

    private List<String> getPlanetDefaultFeeds(Planet planet) {
        return List.of("atom", "rss");
    }
}