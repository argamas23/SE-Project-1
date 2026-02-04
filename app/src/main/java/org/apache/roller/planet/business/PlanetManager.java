package org.apache.roller.planet.business;

import org.apache.roller.planet.Planet;
import org.apache.roller.planet.PlanetFactory;
import org.apache.roller.planet.PlanetEntry;
import org.apache.roller.planet.PlanetEntryFactory;
import org.apache.roller.planet.PlanetFeed;
import org.apache.roller.planet.PlanetFeedFactory;

import java.util.ArrayList;
import java.util.List;

public class PlanetManager {

    private PlanetFactory planetFactory;
    private PlanetEntryFactory planetEntryFactory;
    private PlanetFeedFactory planetFeedFactory;

    public PlanetManager(PlanetFactory planetFactory, PlanetEntryFactory planetEntryFactory, PlanetFeedFactory planetFeedFactory) {
        this.planetFactory = planetFactory;
        this.planetEntryFactory = planetEntryFactory;
        this.planetFeedFactory = planetFeedFactory;
    }

    public List<PlanetEntry> getPlanetEntries(Planet planet) {
        List<PlanetEntry> entries = new ArrayList<>();
        // code to get planet entries
        return entries;
    }

    public void savePlanetEntry(PlanetEntry entry, List<String> categories, List<String> tags, String content) {
        // code to save planet entry
    }

    public void deletePlanet(Planet planet) {
        // code to delete planet
    }

    public void savePlanet(Planet planet, List<PlanetFeed> feeds) {
        // code to save planet
    }

    public List<PlanetFeed> getPlanetFeeds(Planet planet) {
        List<PlanetFeed> feeds = new ArrayList<>();
        // code to get planet feeds
        return feeds;
    }

    public void savePlanetFeed(PlanetFeed feed, String name, String url, int interval) {
        // code to save planet feed
    }

    public void deletePlanetFeed(PlanetFeed feed) {
        // code to delete planet feed
    }

    private void createPlanetEntry(PlanetEntry entry, List<String> categories, List<String> tags, String content) {
        // code to create planet entry
    }

    private void updatePlanetEntry(PlanetEntry entry, List<String> categories, List<String> tags, String content) {
        // code to update planet entry
    }
}