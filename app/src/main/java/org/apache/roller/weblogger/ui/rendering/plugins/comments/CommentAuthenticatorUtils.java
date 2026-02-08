package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.commons.lang3.StringUtils;

public class CommentAuthenticatorUtils {

    private static final String USER_ATTRIBUTE = "user";

    public static boolean isUserAuthenticated(WeblogManager weblogManager, HttpServletRequest request) {
        return StringUtils.isNotEmpty(getAuthenticatedUsername(weblogManager, request));
    }

    private static String getAuthenticatedUsername(WeblogManager weblogManager, HttpServletRequest request) {
        return (String) request.getSession().getAttribute(getUserAttribute());
    }

    private static String getUserAttribute() {
        return USER_ATTRIBUTE;
    }

    private static boolean isValidRequest(HttpServletRequest request) {
        return request != null;
    }

    public static String getUsername(WeblogManager weblogManager, HttpServletRequest request) {
        if (isValidRequest(request)) {
            return getAuthenticatedUsername(weblogManager, request);
        }
        return null;
    }
}