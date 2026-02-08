package org.apache.roller.weblogger.ui.rendering.util.mobile;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Represents a device and provides functionality to determine its type.
 */
public class Device {

    private String userAgent;

    public Device(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Checks if the device is a mobile device.
     * 
     * @return true if the device is a mobile device, false otherwise
     */
    public boolean isMobile() {
        return isAndroid() || isIOS() || isWindowsPhone() || isBlackBerry() || isOperaMini();
    }

    /**
     * Checks if the device is an Android device.
     * 
     * @return true if the device is an Android device, false otherwise
     */
    private boolean isAndroid() {
        return userAgent.contains("Android");
    }

    /**
     * Checks if the device is an iOS device.
     * 
     * @return true if the device is an iOS device, false otherwise
     */
    private boolean isIOS() {
        return userAgent.contains("iPhone") || userAgent.contains("iPad") || userAgent.contains("iPod");
    }

    /**
     * Checks if the device is a Windows Phone device.
     * 
     * @return true if the device is a Windows Phone device, false otherwise
     */
    private boolean isWindowsPhone() {
        return userAgent.contains("Windows Phone");
    }

    /**
     * Checks if the device is a BlackBerry device.
     * 
     * @return true if the device is a BlackBerry device, false otherwise
     */
    private boolean isBlackBerry() {
        return userAgent.contains("BlackBerry");
    }

    /**
     * Checks if the device is using Opera Mini.
     * 
     * @return true if the device is using Opera Mini, false otherwise
     */
    private boolean isOperaMini() {
        return userAgent.contains("Opera Mini");
    }
}