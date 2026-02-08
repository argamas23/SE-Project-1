package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.config.WebloggerContext;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.User;

public class URLModel {
    
    private Weblog weblog;
    private WeblogEntry entry;
    private String categoryId;

    public URLModel(Weblog weblog, WeblogEntry entry, String categoryId) {
        this.weblog = weblog;
        this.entry = entry;
        this.categoryId = categoryId;
    }

    public String getWeblogEntryPermanentURL() {
        return getWeblogEntryPermanentURLStrategy().getPermanentURL(weblog, entry);
    }

    public String getWeblogCategoryEntriesURL() {
        return getWeblogCategoryEntriesURLStrategy().getCategoryEntriesURL(weblog, categoryId);
    }

    public String getWeblogRecentEntriesURL() {
        return getWeblogRecentEntriesURLStrategy().getRecentEntriesURL(weblog);
    }

    public String getWeblogArchiveURL() {
        return getWeblogArchiveURLStrategy().getArchiveURL(weblog);
    }

    private WeblogEntryPermanentURLStrategy getWeblogEntryPermanentURLStrategy() {
        return new WeblogEntryPermanentURLStrategy();
    }

    private WeblogCategoryEntriesURLStrategy getWeblogCategoryEntriesURLStrategy() {
        return new WeblogCategoryEntriesURLStrategy();
    }

    private WeblogRecentEntriesURLStrategy getWeblogRecentEntriesURLStrategy() {
        return new WeblogRecentEntriesURLStrategy();
    }

    private WeblogArchiveURLStrategy getWeblogArchiveURLStrategy() {
        return new WeblogArchiveURLStrategy();
    }

    public static class WeblogEntryPermanentURLStrategy {
        private static final String ENTRY_PERMALINK_PATTERN = "/entry/{entryId}";

        public String getPermanentURL(Weblog weblog, WeblogEntry entry) {
            String url = WebloggerRuntimeConfig.getBaseUrl() + "/weblog/" + weblog.getHandle() + ENTRY_PERMALINK_PATTERN;
            return url.replace("{entryId}", String.valueOf(entry.getId()));
        }
    }

    public static class WeblogCategoryEntriesURLStrategy {
        private static final String CATEGORY_ENTRIES_PATTERN = "/category/{categoryId}";

        public String getCategoryEntriesURL(Weblog weblog, String categoryId) {
            String url = WebloggerRuntimeConfig.getBaseUrl() + "/weblog/" + weblog.getHandle() + CATEGORY_ENTRIES_PATTERN;
            return url.replace("{categoryId}", categoryId);
        }
    }

    public static class WeblogRecentEntriesURLStrategy {
        private static final String RECENT_ENTRIES_PATTERN = "/recent";

        public String getRecentEntriesURL(Weblog weblog) {
            return WebloggerRuntimeConfig.getBaseUrl() + "/weblog/" + weblog.getHandle() + RECENT_ENTRIES_PATTERN;
        }
    }

    public static class WeblogArchiveURLStrategy {
        private static final String ARCHIVE_PATTERN = "/archive";

        public String getArchiveURL(Weblog weblog) {
            return WebloggerRuntimeConfig.getBaseUrl() + "/weblog/" + weblog.getHandle() + ARCHIVE_PATTERN;
        }
    }
}