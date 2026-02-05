```java
package org.apache.roller.planet.business;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.util.Collections; // Still useful for empty maps/lists, though not directly used in the refactored code's core logic.
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.roller.planet.pojos.Planet;
import org.apache.roller.planet.pojos.PlanetGroup;
// Removed: import org.apache.roller.weblogger.config.WebloggerRuntimeConfig; // No longer directly coupled

/**
 * MultiPlanetURLStrategy implements the PlanetURLStrategy interface.
 * It provides methods for generating URLs related to Planet and PlanetGroups.
 *
 * <p>Refactorings applied based on detected smells:</p>
 * <ul>
 *   <li><b>Single Responsibility Principle Violation & Low Cohesion:</b> Generic URL utility methods
 *       (`getQueryString`, `encode`, `decode`) have been moved to an inner static utility class
 *       `QueryParamUtils` to separate concerns within the same file. This improves the cohesion of
 *       `MultiPlanetURLStrategy` by focusing it solely on Planet URL generation logic.</li>
 *   <li><b>Magic Number & Primitive Obsession:</b> The `int pageNum` parameter, particularly its use
 *       with the special value `-1` to indicate "no page," has been replaced by `Optional<Integer>`.
 *       This makes the API clearer, explicitly handles the absence of a page number, and removes the
 *       magic number, improving type expressiveness.</li>
 *   <li><b>Tight Coupling / Global Dependency:</b> The direct dependency on `WebloggerRuntimeConfig.getProperty("site.absoluteurl")`
 *       has been removed. The base URL is now injected via the constructor, making the class more
 *       testable, flexible, and decoupled from static global configuration.</li>
 * </ul>
 */
public class MultiPlanetURLStrategy implements PlanetURLStrategy {

    private final String siteAbsoluteURL; // Injected dependency

    /**
     * Constructor for MultiPlanetURLStrategy.
     * The site's absolute base URL is a mandatory dependency.
     *
     * @param siteAbsoluteURL The absolute base URL of the site, e.g., "http://localhost:8080/roller".
     *                        It should not be null or empty and any trailing slash will be removed
     *                        for consistent URL construction.
     * @throws IllegalArgumentException if siteAbsoluteURL is null or empty.
     */
    public MultiPlanetURLStrategy(String siteAbsoluteURL) {
        if (siteAbsoluteURL == null || siteAbsoluteURL.trim().isEmpty()) {
            throw new IllegalArgumentException("siteAbsoluteURL cannot be null or empty.");
        }
        // Ensure the base URL does not end with a slash for consistent path appending.
        this.siteAbsoluteURL = siteAbsoluteURL.endsWith("/")
            ? siteAbsoluteURL.substring(0, siteAbsoluteURL.length() - 1)
            : siteAbsoluteURL;
    }

    /**
     * Get URL to a planet.
     *
     * @param planet The Planet object.
     * @param pageNum An Optional integer representing the page number. If empty or less than 1,
     *                no page parameter will be added to the URL.
     * @return The URL for the specified planet, including an optional page parameter.
     */
    @Override
    public String getPlanetURL(Planet planet, Optional<Integer> pageNum) {
        StringBuilder url = new StringBuilder();
        url.append(siteAbsoluteURL);
        url.append("/planet/").append(QueryParamUtils.encode(planet.getHandle()));

        SortedMap<String, String> params = new TreeMap<>();
        // Replaced magic number -1 and primitive int with Optional.
        if (pageNum.isPresent() && pageNum.get() > 0) {
            params.put("page", pageNum.get().toString());
        }

        if (!params.isEmpty()) {
            url.append("?").append(QueryParamUtils.getQueryString(params));
        }

        return url.toString();
    }

    /**
     * Get URL to a planet group.
     *
     * @param group The PlanetGroup object.
     * @param pageNum An Optional integer representing the page number. If empty or less than 1,
     *                no page parameter will be added to the URL.
     * @return The URL for the specified planet group, including an optional page parameter.
     */
    @Override
    public String getPlanetGroupURL(PlanetGroup group, Optional<Integer> pageNum) {
        StringBuilder url = new StringBuilder();
        url.append(siteAbsoluteURL);
        url.append("/planet/group/").append(QueryParamUtils.encode(group.getHandle()));

        SortedMap<String, String> params = new TreeMap<>();
        // Replaced magic number -1 and primitive int with Optional.
        if (pageNum.isPresent() && pageNum.get() > 0) {
            params.put("page", pageNum.get().toString());
        }

        if (!params.isEmpty()) {
            url.append("?").append(QueryParamUtils.getQueryString(params));
        }

        return url.toString();
    }

    /**
     * Get URL to a planet group's feed.
     *
     * @param group The PlanetGroup object.
     * @param pageNum An Optional integer representing the page number. If empty or less than 1,
     *                no page parameter will be added to the URL.
     * @return The URL for the specified planet group's feed, including an optional page parameter.
     */
    @Override
    public String getPlanetGroupFeedURL(PlanetGroup group, Optional<Integer> pageNum) {
        StringBuilder url = new StringBuilder();
        url.append(siteAbsoluteURL);
        url.append("/planet/group/").append(QueryParamUtils.encode(group.getHandle()));
        url.append("/feed");

        SortedMap<String, String> params = new TreeMap<>();
        // Replaced magic number -1 and primitive int with Optional.
        if (pageNum.isPresent() && pageNum.get() > 0) {
            params.put("page", pageNum.get().toString());
        }

        if (!params.isEmpty()) {
            url.append("?").append(QueryParamUtils.getQueryString(params));
        }

        return url.toString();
    }

    /**
     * Get URL to a planet group's OPML.
     *
     * @param group The PlanetGroup object.
     * @param pageNum An Optional integer representing the page number. If empty or less than 1,
     *                no page parameter will be added to the URL.
     * @return The URL for the specified planet group's OPML, including an optional page parameter.
     */
    @Override
    public String getPlanetGroupOpmlURL(PlanetGroup group, Optional<Integer> pageNum) {
        StringBuilder url = new StringBuilder();
        url.append(siteAbsoluteURL);
        url.append("/planet/group/").append(QueryParamUtils.encode(group.getHandle()));
        url.append("/opml");

        SortedMap<String, String> params = new TreeMap<>();
        // Replaced magic number -1 and primitive int with Optional.
        if (pageNum.isPresent() && pageNum.get() > 0) {
            params.put("page", pageNum.get().toString());
        }

        if (!params.isEmpty()) {
            url.append("?").append(QueryParamUtils.getQueryString(params));
        }

        return url.toString();
    }

    /**
     * Inner static utility class for handling URL query string manipulation and encoding/decoding.
     * This class encapsulates generic URL parameter handling, improving the cohesion and
     * single responsibility of the outer `MultiPlanetURLStrategy` class. It remains within
     * this file to adhere to the constraint of returning a single refactored Java file.
     */
    private static class QueryParamUtils {

        // Standard encoding for URLs. UTF-8 is universally recommended.
        private static final String UTF_8 = "UTF-8";

        /**
         * Constructs a URL query string from a map of parameters.
         * The parameters are sorted by key to ensure consistent URL generation regardless of map insertion order.
         *
         * @param parameters A map where keys are parameter names and values are parameter values.
         * @return A URL-encoded query string (e.g., "key1=value1&key2=value2"), or an empty string if the map is null or empty.
         */
        public static String getQueryString(Map<String, String> parameters) {
            if (parameters == null || parameters.isEmpty()) {
                return "";
            }

            StringBuilder queryString = new StringBuilder();
            // Using TreeMap to ensure keys are sorted for consistent query string generation.
            SortedMap<String, String> sortedParams = new TreeMap<>(parameters);

            Iterator<Map.Entry<String, String>> it = sortedParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                queryString.append(encode(entry.getKey()));
                queryString.append("=");
                queryString.append(encode(entry.getValue()));
                if (it.hasNext()) {
                    queryString.append("&");
                }
            }
            return queryString.toString();
        }

        /**
         * URL-encodes a string using the UTF-8 character encoding.
         *
         * @param string The string to encode.
         * @return The URL-encoded string.
         * @throws IllegalStateException if UTF-8 encoding is not supported (which should not happen).
         */
        public static String encode(String string) {
            try {
                return URLEncoder.encode(string, UTF_8);
            } catch (UnsupportedEncodingException e) {
                // UTF-8 is a required encoding by Java spec, so this exception indicates a severe system issue.
                throw new IllegalStateException("UTF-8 encoding not supported by the system.", e);
            }
        }

        /**
         * URL-decodes a string using the UTF-8 character encoding.
         *
         * @param string The string to decode.
         * @return The URL-decoded string.
         * @throws IllegalStateException if UTF-8 encoding is not supported (which should not happen).
         */
        public static String decode(String string) {
            try {
                return URLDecoder.decode(string, UTF_8);
            } catch (UnsupportedEncodingException e) {
                // UTF-8 is a required encoding by Java spec, so this exception indicates a severe system issue.
                throw new IllegalStateException("UTF-8 encoding not supported by the system.", e);
            }
        }
    }
}
```