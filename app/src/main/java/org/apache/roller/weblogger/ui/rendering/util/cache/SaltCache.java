package org.apache.roller.weblogger.ui.rendering.util.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.util.RollerContext;

public class SaltCache {

    private static final Log log = LogFactory.getLog(SaltCache.class);
    private static final int MAX_SALT_CACHE_SIZE = WebloggerRuntimeConfig.getIntProperty("max.salt.cache.size", 1000);
    private static final int DEFAULT_SALT_EXPIRY = WebloggerRuntimeConfig.getIntProperty("default.salt.expiry", 30);

    private static class SaltCacheInstance {
        private final ConcurrentMap<String, SaltCacheEntry> cache;
        private final WeblogEntryManager weblogEntryManager;
        private final WeblogManager weblogManager;

        public SaltCacheInstance() {
            this.cache = new ConcurrentHashMap<>();
            this.weblogEntryManager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
            this.weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
        }

        public synchronized void put(String key, WeblogEntryComment comment) {
            if (cache.size() >= MAX_SALT_CACHE_SIZE) {
                // Remove the oldest entry from the cache
                String oldestKey = getOldestKey();
                if (oldestKey != null) {
                    cache.remove(oldestKey);
                }
            }
            cache.put(key, new SaltCacheEntry(comment));
        }

        private String getOldestKey() {
            long oldestTimestamp = Long.MAX_VALUE;
            String oldestKey = null;
            for (String key : cache.keySet()) {
                SaltCacheEntry entry = cache.get(key);
                if (entry.getTimestamp() < oldestTimestamp) {
                    oldestTimestamp = entry.getTimestamp();
                    oldestKey = key;
                }
            }
            return oldestKey;
        }

        public synchronized SaltCacheEntry get(String key) {
            return cache.get(key);
        }

        public synchronized int size() {
            return cache.size();
        }
    }

    private static class SaltCacheEntry {
        private final WeblogEntryComment comment;
        private final long timestamp;

        public SaltCacheEntry(WeblogEntryComment comment) {
            this.comment = comment;
            this.timestamp = System.currentTimeMillis();
        }

        public WeblogEntryComment getComment() {
            return comment;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    private static SaltCacheInstance instance;

    public static synchronized SaltCacheEntry getEntry(String key) {
        if (instance == null) {
            instance = new SaltCacheInstance();
        }
        return instance.get(key);
    }

    public static synchronized void putEntry(String key, WeblogEntryComment comment) {
        if (instance == null) {
            instance = new SaltCacheInstance();
        }
        instance.put(key, comment);
    }

    public static synchronized int getCacheSize() {
        if (instance == null) {
            instance = new SaltCacheInstance();
        }
        return instance.size();
    }

    public static void invalidateWeblogCommentSalt(WeblogEntryComment comment) {
        String key = getCacheKey(comment);
        putEntry(key, comment);
    }

    public static WeblogEntryComment getWeblogCommentFromSalt(String salt) {
        SaltCacheEntry entry = getEntry(salt);
        if (entry != null) {
            return entry.getComment();
        } else {
            return null;
        }
    }

    public static boolean validateWeblogCommentSalt(WeblogEntryComment comment, String salt) {
        String expectedSalt = getCacheKey(comment);
        if (expectedSalt.equals(salt)) {
            return true;
        } else {
            return false;
        }
    }

    private static String getCacheKey(WeblogEntryComment comment) {
        // generate a unique key for the comment
        return comment.getId() + "#" + comment.getWeblogId();
    }
}