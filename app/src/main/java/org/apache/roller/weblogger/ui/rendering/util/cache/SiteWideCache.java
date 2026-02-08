package org.apache.roller.weblogger.ui.rendering.util.cache;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.ui.rendering.util.cache.providers.CacheProvider;
import org.apache.roller.weblogger.ui.rendering.util.cache.providers.EhCacheProvider;
import org.apache.roller.weblogger.ui.rendering.util.cache.providers.MemoryCacheProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SiteWideCache {

    private static final Logger logger = LoggerFactory.getLogger(SiteWideCache.class);

    private static final String CACHE_TYPE_EH_CACHE = "ehcache";
    private static final String CACHE_TYPE_MEMORY = "memory";

    private final CacheProvider cacheProvider;
    private final Map<String, String> cacheConfig;

    private SiteWideCache() {
        WebloggerRuntimeConfig runtimeConfig = WebloggerFactory.getWeblogger().getRuntimeConfig();
        String cacheType = runtimeConfig.getProperty("cache.type");

        cacheConfig = new ConcurrentHashMap<>();
        cacheConfig.put("maxElementsInMemory", runtimeConfig.getProperty("cache.maxElementsInMemory"));
        cacheConfig.put("timeToIdleSeconds", runtimeConfig.getProperty("cache.timeToIdleSeconds"));
        cacheConfig.put("timeToLiveSeconds", runtimeConfig.getProperty("cache.timeToLiveSeconds"));

        if (CACHE_TYPE_EH_CACHE.equals(cacheType)) {
            cacheProvider = new EhCacheProvider(cacheConfig);
        } else if (CACHE_TYPE_MEMORY.equals(cacheType)) {
            cacheProvider = new MemoryCacheProvider(cacheConfig);
        } else {
            throw new WebloggerException("Invalid cache type: " + cacheType);
        }
    }

    private static class CacheHelper {
        private static final SiteWideCache instance = new SiteWideCache();

        public static SiteWideCache getInstance() {
            return instance;
        }
    }

    public static SiteWideCache getInstance() {
        return CacheHelper.getInstance();
    }

    public void put(String key, Object value) {
        cacheProvider.put(key, value);
    }

    public Object get(String key) {
        return cacheProvider.get(key);
    }

    public void remove(String key) {
        cacheProvider.remove(key);
    }

    public void clear() {
        cacheProvider.clear();
    }
}

class EhCacheProvider implements CacheProvider {
    private final Map<String, String> cacheConfig;

    public EhCacheProvider(Map<String, String> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void put(String key, Object value) {
        // EhCache implementation
    }

    @Override
    public Object get(String key) {
        // EhCache implementation
        return null;
    }

    @Override
    public void remove(String key) {
        // EhCache implementation
    }

    @Override
    public void clear() {
        // EhCache implementation
    }
}

class MemoryCacheProvider implements CacheProvider {
    private final Map<String, String> cacheConfig;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public MemoryCacheProvider(Map<String, String> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}

interface CacheProvider {
    void put(String key, Object value);
    Object get(String key);
    void remove(String key);
    void clear();
}