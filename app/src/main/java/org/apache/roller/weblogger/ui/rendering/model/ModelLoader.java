package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WeblogEntry;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogCategory;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.WeblogLocale;
import org.apache.roller.weblogger.pojos.WeblogTag;
import org.apache.roller.weblogger.util.RollerConstants;
import org.apache.roller.weblogger.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelLoader {

    private static final Logger LOGGER = Logger.getLogger(ModelLoader.class.getName());

    private static final String DEFAULT_WEBLOG_ID = "defaultWeblogId";
    private static final String WEBLOG_TAG = "weblogTag";
    private static final String CATEGORY = "category";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String LOCALE = "locale";

    private WeblogEntryManager weblogEntryManager;

    public ModelLoader(WeblogEntryManager weblogEntryManager) {
        this.weblogEntryManager = weblogEntryManager;
    }

    public ModelData getModelData(String weblogId, String weblogTag, String category, Integer month, Integer year, Locale locale) throws WebloggerException {
        ModelData modelData = new ModelData();
        modelData.setWeblog(getWeblog(weblogId, locale));
        modelData.setWeblogTag(getWeblogTag(weblogEntryManager, weblogTag));
        modelData.setCategory(getCategory(weblogEntryManager, category));
        modelData.setMonth(getMonth(month));
        modelData.setYear(getYear(year));
        modelData.setLocale(locale);
        return modelData;
    }

    private Weblog getWeblog(String weblogId, Locale locale) {
        try {
            if (weblogId == null || weblogId.isEmpty()) {
                return getWeblogForLocale(locale);
            } else {
                return weblogEntryManager.getWeblog(weblogId);
            }
        } catch (WebloggerException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving weblog", e);
            return null;
        }
    }

    private Weblog getWeblogForLocale(Locale locale) {
        WeblogLocale weblogLocale = Weblogger.getWeblogger().getWeblogLocale(locale);
        return weblogLocale != null ? weblogLocale.getWeblog() : null;
    }

    private WeblogTag getWeblogTag(WeblogEntryManager weblogEntryManager, String weblogTag) {
        if (weblogTag != null && !weblogTag.isEmpty()) {
            return weblogEntryManager.getWeblogTag(weblogTag);
        } else {
            return null;
        }
    }

    private WeblogCategory getCategory(WeblogEntryManager weblogEntryManager, String category) {
        if (category != null && !category.isEmpty()) {
            return weblogEntryManager.getWeblogCategory(category);
        } else {
            return null;
        }
    }

    private Integer getMonth(Integer month) {
        if (month == null || month < 1 || month > 12) {
            return Calendar.getInstance().get(Calendar.MONTH) + 1;
        } else {
            return month;
        }
    }

    private Integer getYear(Integer year) {
        if (year == null || year < 1) {
            return Calendar.getInstance().get(Calendar.YEAR);
        } else {
            return year;
        }
    }

    private static class ModelData {
        private Weblog weblog;
        private WeblogTag weblogTag;
        private WeblogCategory category;
        private Integer month;
        private Integer year;
        private Locale locale;

        public Weblog getWeblog() {
            return weblog;
        }

        public void setWeblog(Weblog weblog) {
            this.weblog = weblog;
        }

        public WeblogTag getWeblogTag() {
            return weblogTag;
        }

        public void setWeblogTag(WeblogTag weblogTag) {
            this.weblogTag = weblogTag;
        }

        public WeblogCategory getCategory() {
            return category;
        }

        public void setCategory(WeblogCategory category) {
            this.category = category;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }
    }
}