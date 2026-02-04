package org.apache.roller.planet.business;

public class MultiPlanetURLStrategy {

    private static final int DEFAULT_MAX_RESULTS = 10;
    private static final int MIN_RESULTS = 1;
    private static final int MAX_RESULTS = 100;

    public enum Planets {
        PLANET1,
        PLANET2,
        PLANET3
    }

    public String getUrlForPlanets(Planets[] planets, int maxResults) {
        validateMaxResults(maxResults);
        return buildUrlForPlanets(planets, maxResults);
    }

    private void validateMaxResults(int maxResults) {
        if (maxResults < MIN_RESULTS || maxResults > MAX_RESULTS) {
            throw new IllegalArgumentException("Invalid max results");
        }
    }

    private String buildUrlForPlanets(Planets[] planets, int maxResults) {
        StringBuilder url = new StringBuilder();
        for (Planets planet : planets) {
            url.append(planet.name()).append(",");
        }
        url.deleteCharAt(url.length() - 1);
        url.append("?maxResults=").append(maxResults);
        return url.toString();
    }

    public String getDefaultUrlForPlanets(Planets[] planets) {
        return getUrlForPlanets(planets, DEFAULT_MAX_RESULTS);
    }
}