package org.apache.roller.weblogger.ui.rendering;

import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.util.URLUtilities;
import org.apache.roller.weblogger.util.RollerContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RequestMapper {

    private static final List<String> RESERVATIONS = Arrays.asList("admin", "login", "logout", "preview");

    private RequestMapper() {}

    public static String mapRequest(HttpServletRequest request) {
        try {
            RollerContext context = RollerContext.getRollerContext(request);
            String path = context.getRequestedPath();
            if (RESERVATIONS.contains(path)) {
                return path;
            }

            if (WebloggerConfig.getBoolProperty("url.extensions.disabled")) {
                String[] paths = path.split("/");
                if (paths.length == 2 && paths[0].isEmpty() && paths[1].contains(".")) {
                    String extension = paths[1].substring(paths[1].lastIndexOf('.') + 1);
                    if (!Arrays.asList("rss", "atom").contains(extension)) {
                        path = paths[1].substring(0, paths[1].lastIndexOf('.'));

                        String langPath = getLangPath(request);
                        if (langPath != null) {
                            path = langPath + "/" + path;
                        }

                        request.setAttribute("requestedPath", path);
                        return path;
                    }
                } else {
                    String langPath = getLangPath(request);
                    if (langPath != null) {
                        path = langPath + "/" + path;
                    }
                }
            }

            request.setAttribute("requestedPath", path);
            return path;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getLangPath(HttpServletRequest request) {
        Locale locale = request.getLocale();
        Weblog weblog = (Weblog) request.getSession().getAttribute("weblog");
        if (weblog != null && !weblog.getLocale().equals(locale.getLanguage())) {
            return locale.getLanguage();
        }
        return null;
    }

    // utility method
    public static String getPathWithoutLocale(String path) {
        if (path.contains("/")) {
            return path.substring(path.indexOf('/') + 1);
        }
        return path;
    }
}