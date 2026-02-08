package org.apache.roller.weblogger.ui.rendering.util.mobile;

public class Device {

    private DeviceType deviceType;
    private String userAgent;
    private String accept;

    public Device(String userAgent, String accept) {
        this.userAgent = userAgent;
        this.accept = accept;
        this.deviceType = determineDeviceType();
    }

    private DeviceType determineDeviceType() {
        if (isIphone()) {
            return DeviceType.IPHONE;
        } else if (isAndroid()) {
            return DeviceType.ANDROID;
        } else if (isWindowsPhone()) {
            return DeviceType.WINDOWS_PHONE;
        } else {
            return DeviceType.UNKNOWN;
        }
    }

    private boolean isIphone() {
        return userAgent.contains("iPhone");
    }

    private boolean isAndroid() {
        return userAgent.contains("Android");
    }

    private boolean isWindowsPhone() {
        return userAgent.contains("Windows Phone");
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public enum DeviceType {
        IPHONE,
        ANDROID,
        WINDOWS_PHONE,
        UNKNOWN
    }
}