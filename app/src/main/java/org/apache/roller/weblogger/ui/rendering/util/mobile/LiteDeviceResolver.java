package org.apache.roller.weblogger.ui.rendering.util.mobile;

import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.util.URLHelper;
import org.apache.commons.lang3.StringUtils;

public class LiteDeviceResolver {

    private static final String USER_AGENT_PREFIX_IPHONE = "iPhone";
    private static final String USER_AGENT_PREFIX_BLACKBERRY = "BlackBerry";
    private static final String USER_AGENT_PREFIX_ANDROID = "Android";
    private static final String USER_AGENT_PREFIX_IPAD = "iPad";
    private static final String USER_AGENT_PREFIX_WINDOWS_PHONE = "Windows Phone";

    public static class DeviceType {
        public static final String IPHONE = "iPhone";
        public static final String BLACKBERRY = "BlackBerry";
        public static final String ANDROID = "Android";
        public static final String IPAD = "iPad";
        public static final String WINDOWS_PHONE = "Windows Phone";
        public static final String UNKNOWN = "Unknown";
    }

    public static class LiteDevice {
        private String userAgent;
        private String deviceType;

        public LiteDevice(String userAgent) {
            this.userAgent = userAgent;
            this.deviceType = determineDeviceType(userAgent);
        }

        public String getDeviceType() {
            return deviceType;
        }

        private String determineDeviceType(String userAgent) {
            DeviceTypeDetermineStrategy strategy = DeviceTypeDetermineStrategyFactory.createStrategy(userAgent);
            return strategy.determineDeviceType();
        }
    }

    public static abstract class DeviceTypeDetermineStrategy {
        protected String userAgent;

        public DeviceTypeDetermineStrategy(String userAgent) {
            this.userAgent = userAgent;
        }

        public abstract String determineDeviceType();
    }

    public static class IPhoneDeviceTypeDetermineStrategy extends DeviceTypeDetermineStrategy {
        public IPhoneDeviceTypeDetermineStrategy(String userAgent) {
            super(userAgent);
        }

        @Override
        public String determineDeviceType() {
            return DeviceType.IPHONE;
        }
    }

    public static class BlackBerryDeviceTypeDetermineStrategy extends DeviceTypeDetermineStrategy {
        public BlackBerryDeviceTypeDetermineStrategy(String userAgent) {
            super(userAgent);
        }

        @Override
        public String determineDeviceType() {
            return DeviceType.BLACKBERRY;
        }
    }

    public static class AndroidDeviceTypeDetermineStrategy extends DeviceTypeDetermineStrategy {
        public AndroidDeviceTypeDetermineStrategy(String userAgent) {
            super(userAgent);
        }

        @Override
        public String determineDeviceType() {
            return DeviceType.ANDROID;
        }
    }

    public static class iPadDeviceTypeDetermineStrategy extends DeviceTypeDetermineStrategy {
        public iPadDeviceTypeDetermineStrategy(String userAgent) {
            super(userAgent);
        }

        @Override
        public String determineDeviceType() {
            return DeviceType.IPAD;
        }
    }

    public static class WindowsPhoneDeviceTypeDetermineStrategy extends DeviceTypeDetermineStrategy {
        public WindowsPhoneDeviceTypeDetermineStrategy(String userAgent) {
            super(userAgent);
        }

        @Override
        public String determineDeviceType() {
            return DeviceType.WINDOWS_PHONE;
        }
    }

    public static class UnknownDeviceTypeDetermineStrategy extends DeviceTypeDetermineStrategy {
        public UnknownDeviceTypeDetermineStrategy(String userAgent) {
            super(userAgent);
        }

        @Override
        public String determineDeviceType() {
            return DeviceType.UNKNOWN;
        }
    }

    public static class DeviceTypeDetermineStrategyFactory {
        public static DeviceTypeDetermineStrategy createStrategy(String userAgent) {
            if (userAgent.contains(USER_AGENT_PREFIX_IPHONE)) {
                return new IPhoneDeviceTypeDetermineStrategy(userAgent);
            } else if (userAgent.contains(USER_AGENT_PREFIX_BLACKBERRY)) {
                return new BlackBerryDeviceTypeDetermineStrategy(userAgent);
            } else if (userAgent.contains(USER_AGENT_PREFIX_ANDROID)) {
                return new AndroidDeviceTypeDetermineStrategy(userAgent);
            } else if (userAgent.contains(USER_AGENT_PREFIX_IPAD)) {
                return new iPadDeviceTypeDetermineStrategy(userAgent);
            } else if (userAgent.contains(USER_AGENT_PREFIX_WINDOWS_PHONE)) {
                return new WindowsPhoneDeviceTypeDetermineStrategy(userAgent);
            } else {
                return new UnknownDeviceTypeDetermineStrategy(userAgent);
            }
        }
    }

    public static boolean isLiteDevice(String userAgent) {
        LiteDevice device = new LiteDevice(userAgent);
        return !device.getDeviceType().equals(DeviceType.UNKNOWN);
    }

    public static void main(String[] args) {
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1";
        System.out.println(isLiteDevice(userAgent));
    }
}