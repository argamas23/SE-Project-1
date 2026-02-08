package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.ui.rendering.model.ThemeModel;

import java.util.Locale;
import java.util.Map;

public class PreviewPageModel {

    private static final String DEFAULT_LOCALE = "en_US";
    private static final String DEFAULT_THEME = "default";

    private Weblog weblog;
    private WeblogEntry entry;
    private User user;

    public PreviewPageModel(Weblog weblog, WeblogEntry entry, User user) {
        this.weblog = weblog;
        this.entry = entry;
        this.user = user;
    }

    public String getThemeName() {
        return getThemeName(weblog);
    }

    private String getThemeName(Weblog weblog) {
        String themeName = weblog.getTheme();
        return themeName != null ? themeName : DEFAULT_THEME;
    }

    public ThemeModel getThemeModel() {
        return new ThemeModel(getThemeName(), Locale.forLanguageTag(DEFAULT_LOCALE));
    }

    public WeblogEntry getWeblogEntry() {
        return entry;
    }

    public Weblog getWeblog() {
        return weblog;
    }

    public User getUser() {
        return user;
    }

    public void setWeblogEntry(WeblogEntry entry) {
        this.entry = entry;
    }

    public void setWeblog(Weblog weblog) {
        this.weblog = weblog;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String generatePreviewContent() throws WebloggerException {
        WeblogEntryRenderer renderer = getRenderer(entry);
        return renderer.render(entry);
    }

    private WeblogEntryRenderer getRenderer(WeblogEntry entry) {
        if (entry instanceof WeblogEntry) {
            return new DefaultWeblogEntryRenderer();
        } else {
            throw new UnsupportedOperationException("Unsupported entry type");
        }
    }

    private interface WeblogEntryRenderer {
        String render(WeblogEntry entry) throws WebloggerException;
    }

    private class DefaultWeblogEntryRenderer implements WeblogEntryRenderer {

        @Override
        public String render(WeblogEntry entry) throws WebloggerException {
            // entry rendering logic
            return "rendered entry content";
        }
    }

    public Map<String, Object> getVelocityContext() {
        Map<String, Object> velocityContext = createVelocityContext();
        velocityContext.put("weblog", weblog);
        velocityContext.put("entry", entry);
        velocityContext.put("user", user);
        return velocityContext;
    }

    private Map<String, Object> createVelocityContext() {
        Map<String, Object> velocityContext = new java.util.HashMap<>();
        velocityContext.put("default_locale", DEFAULT_LOCALE);
        return velocityContext;
    }
}