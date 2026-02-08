package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.ui.rendering.model.RenderContext;
import org.apache.roller.weblogger.ui.rendering.model.WeblogURLStrategy;
import org.apache.roller.weblogger.util.URLUtilities;
import org.apache.roller.weblogger.util.WebloggerConfig;

public class PreviewURLModel {

    private static final String DEFAULT_PREVIEW_URL = "http://localhost:8080/roller";
    private static final String HTTPS_PROTOCOL = "https";
    private static final String HTTP_PROTOCOL = "http";

    private RenderContext renderContext;

    public PreviewURLModel(RenderContext renderContext) {
        this.renderContext = renderContext;
    }

    public String getPreviewURL(WeblogEntry entry) {
        return getPreviewURL(entry, false);
    }

    public String getPreviewURL(WeblogEntry entry, boolean https) {
        Weblog weblog = entry.getWeblog();

        if (weblog != null) {
            String urlStrategy = weblog.getUrlStrategy();
            return getPreviewURL(weblog, urlStrategy, https);
        } else {
            return getPreviewURL(renderContext, https);
        }
    }

    private String getPreviewURL(Weblog weblog, String urlStrategy, boolean https) {
        WeblogURLStrategy strategy = getURLStrategy(urlStrategy);
        return strategy.getPreviewURL(weblog, https);
    }

    private String getPreviewURL(RenderContext renderContext, boolean https) {
        WebloggerConfig config = WebloggerRuntimeConfig.getWebloggerConfig();
        String hostname = getHostname(config);
        int port = getPort(config, https);
        String protocol = getProtocol(https);

        return URLUtilities.createURL(protocol, hostname, port, renderContext.getWeblog().getId());
    }

    private String getHostname(WebloggerConfig config) {
        return config.getHostname();
    }

    private int getPort(WebloggerConfig config, boolean https) {
        return https ? config.getHttpsPort() : config.getHttpPort();
    }

    private String getProtocol(boolean https) {
        return https ? HTTPS_PROTOCOL : HTTP_PROTOCOL;
    }

    private WeblogURLStrategy getURLStrategy(String strategyName) {
        switch (strategyName) {
            case "dotnet":
                return new DotnetURLStrategy();
            case "jroller":
                return new JRollerURLStrategy();
            default:
                return new DefaultURLStrategy();
        }
    }

    private interface WeblogURLStrategy {
        String getPreviewURL(Weblog weblog, boolean https);
    }

    private class DefaultURLStrategy implements WeblogURLStrategy {
        @Override
        public String getPreviewURL(Weblog weblog, boolean https) {
            return getPreviewURL(weblog, "default", https);
        }
    }

    private class DotnetURLStrategy implements WeblogURLStrategy {
        @Override
        public String getPreviewURL(Weblog weblog, boolean https) {
            return getPreviewURL(weblog, "dotnet", https);
        }
    }

    private class JRollerURLStrategy implements WeblogURLStrategy {
        @Override
        public String getPreviewURL(Weblog weblog, boolean https) {
            return getPreviewURL(weblog, "jroller", https);
        }
    }
}