package org.apache.roller.weblogger.ui.rendering.util.cache;

import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.util.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetCache.class);

    private static final int MAX_CACHE_TIME = 3600; // 1 hour in seconds
    private static final int MIN_CACHE_TIME = 300; // 5 minutes in seconds

    private boolean cacheEnabled;
    private CacheManager cacheManager;

    public PlanetCache() {
        this.cacheEnabled = WebloggerRuntimeConfig.getBooleanProperty("planet.cache.enabled");
        this.cacheManager = CacheManager.getInstance();
    }

    public String getCacheKey(String planetId, String locale, String url) {
        return "planet_" + planetId + "_" + locale + "_" + url;
    }

    public void invalidateCache(String planetId) {
        if (cacheEnabled) {
            cacheManager.invalidateCache("planet_" + planetId);
        }
    }

    public void putInCache(String key, String value) {
        if (cacheEnabled) {
            cacheManager.putInCache(key, value, getCacheTime());
        }
    }

    public String getFromCache(String key) {
        if (cacheEnabled) {
            return cacheManager.getFromCache(key);
        } else {
            return null;
        }
    }

    private int getCacheTime() {
        // Determine cache time based on configuration
        int cacheTime = WebloggerRuntimeConfig.getIntProperty("planet.cache.time");
        if (cacheTime < MIN_CACHE_TIME) {
            cacheTime = MIN_CACHE_TIME;
        } else if (cacheTime > MAX_CACHE_TIME) {
            cacheTime = MAX_CACHE_TIME;
        }
        return cacheTime;
    }
}