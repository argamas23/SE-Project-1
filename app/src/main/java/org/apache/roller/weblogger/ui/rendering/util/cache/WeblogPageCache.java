package org.apache.roller.weblogger.ui.rendering.util.cache;

import java.util.HashMap;
import java.util.Map;

public class WeblogPageCache {
    
    private static final String CACHE_KEY_PREFIX = "weblog-page-";
    private static final String DEFAULT_CACHE_NAME = "weblog-page-cache";
    private static final int MAX_CACHE_SIZE = 100;

    private WeblogPageCache() {}

    private static class WeblogPageCacheInstance {
        private static final WeblogPageCache instance = new WeblogPageCache();
    }

    public static WeblogPageCache getInstance() {
        return WeblogPageCacheInstance.instance;
    }

    private Map<String, CachedPage> cache = new HashMap<>();
    private int cacheSize = 0;

    public CachedPage getCachedPage(String weblogId, String pageId) {
        String cacheKey = CACHE_KEY_PREFIX + weblogId + "-" + pageId;
        return cache.get(cacheKey);
    }

    public void cachePage(String weblogId, String pageId, CachedPage page) {
        String cacheKey = CACHE_KEY_PREFIX + weblogId + "-" + pageId;
        if (cacheSize < MAX_CACHE_SIZE) {
            cache.put(cacheKey, page);
            cacheSize++;
        } else {
            // evict least recently used item from cache
            String lruKey = getLeastRecentlyUsedKey();
            cache.remove(lruKey);
            cache.put(cacheKey, page);
        }
    }

    private String getLeastRecentlyUsedKey() {
        // implement logic to find least recently used key
        return null; // todo: implement this method
    }

    private enum PageType {
        INDEX,
        CATEGORY,
        TAG,
        DATE,
        AUTHOR
    }

    private static class CachedPage {
        private String content;
        private PageType type;

        public CachedPage(String content, PageType type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public PageType getType() {
            return type;
        }
    }

    public void clearCache() {
        cache.clear();
        cacheSize = 0;
    }

    public int getCacheSize() {
        return cacheSize;
    }
}