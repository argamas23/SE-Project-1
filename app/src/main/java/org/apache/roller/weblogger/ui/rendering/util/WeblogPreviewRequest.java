package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.ui.rendering.util.PreviewUtils;
import org.apache.roller.weblogger.util.RollerConstants;
import org.apache.roller.weblogger.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeblogPreviewRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeblogPreviewRequest.class);
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private Weblog weblog;
    private WeblogEntry entry;
    private String format;
    private String theme;
    private String dateHeader;
    private String action;
    private List<String> messages;
    private Map<String, String> templateModel;

    public WeblogPreviewRequest() {
        this.messages = new ArrayList<>();
        this.templateModel = Map.of();
    }

    public void setWeblog(Weblog weblog) {
        this.weblog = weblog;
    }

    public void setEntry(WeblogEntry entry) {
        this.entry = entry;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setDateHeader(String dateHeader) {
        this.dateHeader = dateHeader;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Map<String, String> getTemplateModel() {
        return templateModel;
    }

    public void setupTemplateModel() {
        this.templateModel = Map.of(
                "weblog", weblog.getId(),
                "entry", entry.getId(),
                "action", action,
                "dateHeader", dateHeader
        );
    }

    public void prepareForRendering() {
        try {
            if ("view".equals(action)) {
                renderView();
            } else if ("edit".equals(action)) {
                renderEdit();
            } else {
                messages.add("Invalid action: " + action);
            }
        } catch (WebloggerException e) {
            LOGGER.error(e.getMessage(), e);
            messages.add("Error occurred while preparing for rendering: " + e.getMessage());
        }
    }

    private void renderView() throws WebloggerException {
        if (weblog == null || entry == null) {
            messages.add("Invalid weblog or entry");
            return;
        }
        String date = DateUtil.formatDate(new Date(), DEFAULT_DATE_FORMAT);
        if (dateHeader != null) {
            date = dateHeader;
        }
        templateModel.put("date", date);
        templateModel.put("weblog", weblog.getName());
        templateModel.put("entry", entry.getTitle());
        if (theme != null) {
            templateModel.put("theme", theme);
        }
    }

    private void renderEdit() throws WebloggerException {
        if (weblog == null || entry == null) {
            messages.add("Invalid weblog or entry");
            return;
        }
        templateModel.put("weblog", weblog.getName());
        templateModel.put("entry", entry.getTitle());
        if (theme != null) {
            templateModel.put("theme", theme);
        }
    }

    public void loadWeblog() {
        try {
            WeblogManager weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
            weblog = weblogManager.getWeblog(weblog.getId());
        } catch (WebloggerException e) {
            LOGGER.error(e.getMessage(), e);
            messages.add("Error occurred while loading weblog: " + e.getMessage());
        }
    }

    public void loadEntry() {
        try {
            WeblogManager weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
            entry = weblogManager.getWeblogEntry(entry.getId());
        } catch (WebloggerException e) {
            LOGGER.error(e.getMessage(), e);
            messages.add("Error occurred while loading entry: " + e.getMessage());
        }
    }

    public void loadTheme() {
        try {
            WebloggerRuntimeConfig runtimeConfig = WebloggerFactory.getWeblogger().getRuntimeConfig();
            theme = runtimeConfig.getProperty(weblog.getId(), "theme");
        } catch (WebloggerException e) {
            LOGGER.error(e.getMessage(), e);
            messages.add("Error occurred while loading theme: " + e.getMessage());
        }
    }
}