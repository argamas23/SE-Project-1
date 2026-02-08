package org.apache.roller.weblogger.ui.rendering.util.mobile;

/**
 * Enum to represent different types of devices.
 */
public enum DeviceType {

    DESKTOP,
    TABLET,
    MOBILE,
    @Deprecated
    COMPARISON_DEVICE_TYPE;

    private DeviceType() {}

    public boolean isMobile() {
        return this == MOBILE || this == TABLET;
    }

    public boolean isDesktop() {
        return this == DESKTOP;
    }
}