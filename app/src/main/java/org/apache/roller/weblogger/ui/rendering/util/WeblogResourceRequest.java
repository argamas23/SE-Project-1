package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryCategory;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.roller.weblogger.util.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class WeblogResourceRequest {

    private static final Log log = LogFactory.getLog(WeblogResourceRequest.class);
    private static final String CATEGORY_NAME = "category";
    private static final String WEBLOG_HANDLE = "weblogHandle";
    private static final String CATEGORY_HANDLE = "categoryHandle";
    private static final String LOCALE = "locale";
    private static final String DATE_FORMAT = "dateFormat";
    private static final String DATE = "date";

    private HttpServletRequest request;
    private Weblog weblog;
    private WeblogEntryCategory category;
    private Date date;
    private String dateFormat;
    private String locale;

    public WeblogResourceRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void initialize() {
        weblog = getWeblog();
        category = getCategory();
        date = getDate();
        dateFormat = getDateFormat();
        locale = getLocale();
    }

    private Weblog getWeblog() {
        String weblogHandle = request.getParameter(WEBLOG_HANDLE);
        try {
            return WebloggerFactory.getWeblogger().getWeblogByHandle(weblogHandle);
        } catch (WebloggerException e) {
            log.error("Error getting weblog by handle", e);
            return null;
        }
    }

    private WeblogEntryCategory getCategory() {
        String categoryHandle = request.getParameter(CATEGORY_HANDLE);
        if (categoryHandle == null || categoryHandle.isEmpty()) {
            return null;
        }
        try {
            return WebloggerFactory.getWeblogger().getWeblogEntryCategoryByHandle(categoryHandle);
        } catch (WebloggerException e) {
            log.error("Error getting category by handle", e);
            return null;
        }
    }

    private Date getDate() {
        String dateString = request.getParameter(DATE);
        if (dateString == null || dateString.isEmpty()) {
            return new Date();
        }
        try {
            return WebloggerFactory.getWeblogger().parseDate(dateString, getDateFormat());
        } catch (WebloggerException e) {
            log.error("Error parsing date", e);
            return new Date();
        }
    }

    private String getDateFormat() {
        String dateFormat = request.getParameter(DATE_FORMAT);
        if (dateFormat == null || dateFormat.isEmpty()) {
            return "yyyy-MM-dd";
        }
        return dateFormat;
    }

    private String getLocale() {
        String locale = request.getParameter(LOCALE);
        if (locale == null || locale.isEmpty()) {
            return "en_US";
        }
        return locale;
    }

    public Weblog getWeblog() {
        return weblog;
    }

    public WeblogEntryCategory getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getLocale() {
        return locale;
    }
}