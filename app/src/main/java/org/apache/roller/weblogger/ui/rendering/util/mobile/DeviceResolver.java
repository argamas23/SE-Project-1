package org.apache.roller.weblogger.ui.rendering.util.mobile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Utility class for resolving mobile device types from HTTP requests.
 */
public class DeviceResolver {

    // Regular expressions for matching mobile device user agents
    private static final Pattern MOBILE_DEVICE_PATTERN = Pattern.compile("mobile|android|iphone|ipad|ipod|blackberry|windows phone", Pattern.CASE_INSENSITIVE);
    private static final Pattern TABLET_DEVICE_PATTERN = Pattern.compile("tablet|ipad", Pattern.CASE_INSENSITIVE);

    /**
     * Resolves the device type from the given HTTP request.
     *
     * @param request the HTTP request
     * @return the device type (MOBILE, TABLET, or DESKTOP)
     */
    public static DeviceType resolveDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || userAgent.isEmpty()) {
            return DeviceType.Desktop;
        }

        Matcher mobileMatcher = MOBILE_DEVICE_PATTERN.matcher(userAgent);
        Matcher tabletMatcher = TABLET_DEVICE_PATTERN.matcher(userAgent);

        if (tabletMatcher.find()) {
            return DeviceType.Tablet;
        } else if (mobileMatcher.find()) {
            return DeviceType.Mobile;
        } else {
            return DeviceType.Desktop;
        }
    }

    /**
     * Enum representing different device types.
     */
    public enum DeviceType {
        Mobile,
        Tablet,
        Desktop
    }
}