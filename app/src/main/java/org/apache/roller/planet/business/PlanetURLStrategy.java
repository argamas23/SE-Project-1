```java
package org.apache.roller.planet.business;

// Removed imports for java.io.UnsupportedEncodingException and java.util.Map
// as they were only used by the generic utility methods which have been moved.

/**
 * Defines a strategy for generating Planet-related URLs.
 * <p>
 * Implementors of this interface provide specific logic for constructing
 * various URLs pertinent to the Planet module. This interface is now
 * solely focused on defining the Planet URL strategy, ensuring a high degree
 * of cohesion. Generic URL utility functions, such as query string construction
 * and URL encoding/decoding, have been moved to a separate utility class
 * (e.g., {@code PlanetURLUtils}) to adhere to the Interface Segregation Principle
 * and prevent this interface from becoming "fat".
 * </p>
 */
public interface PlanetURLStrategy {

    /**
     * Returns the base URL for the Planet instance.
     * @return The Planet base URL.
     */
    String getPlanetURL();

    /**
     * Returns a URL for a specific Planet feed subscription.
     * @param feedURL The URL of the feed to subscribe to.
     * @return The subscription URL.
     */
    String getSubscriptionURL(String feedURL);

    /**
     * Returns a URL for a specific entry or resource within the Planet system.
     * This is a placeholder for a typical strategy-specific method.
     * @param resourceId The identifier of the resource.
     * @return The URL for the specified resource.
     */
    String getResourceURL(String resourceId);

    // The following methods (getQueryString, encode, decode) were previously
    // part of this interface but have been identified as generic utility methods
    // not directly related to the core Planet URL strategy.
    // They have been moved to a separate, dedicated utility class
    // (e.g., 'org.apache.roller.planet.business.PlanetURLUtils')
    // to improve the cohesion of this interface and eliminate the Fat Interface smell.
    // Callers should now use the methods provided by the utility class directly.

    // Removed: String getQueryString(java.util.Map<String, String[]> params);
    // Removed: String encode(String url) throws java.io.UnsupportedEncodingException;
    // Removed: String decode(String url) throws java.io.UnsupportedEncodingException;
}
```