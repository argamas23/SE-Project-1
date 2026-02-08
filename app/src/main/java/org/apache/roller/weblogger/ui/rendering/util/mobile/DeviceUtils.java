package org.apache.roller.weblogger.ui.rendering.util.mobile;

public class DeviceUtils {

    private static final String USER_AGENT_IPHONE = "iPhone";
    private static final String USER_AGENT_IPAD = "iPad";
    private static final String USER_AGENT_ANDROID = "Android";
    private static final int MAX_MOBILE_SCREEN_WIDTH = 800;

    public enum DeviceType {
        MOBILE,
        TABLET,
        DESKTOP
    }

    public static boolean isMobileDevice(String userAgent) {
        return userAgent.contains(USER_AGENT_IPHONE) || userAgent.contains(USER_AGENT_ANDROID);
    }

    public static boolean isTabletDevice(String userAgent) {
        return userAgent.contains(USER_AGENT_IPAD);
    }

    public static DeviceType determineDeviceType(String userAgent, int screenWidth) {
        if (isMobileDevice(userAgent) || screenWidth < MAX_MOBILE_SCREEN_WIDTH) {
            return DeviceType.MOBILE;
        } else if (isTabletDevice(userAgent)) {
            return DeviceType.TABLET;
        } else {
            return DeviceType.DESKTOP;
        }
    }

    public static boolean isMobileOrTabletDevice(String userAgent) {
        return isMobileDevice(userAgent) || isTabletDevice(userAgent);
    }
}