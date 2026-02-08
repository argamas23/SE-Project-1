package org.apache.roller.weblogger.ui.rendering.util.mobile;

import org.apache.roller.weblogger.ui.rendering.util.MobileDevice;
import org.apache.roller.weblogger.ui.rendering.util.MobileDeviceType;
import org.apache.roller.weblogger.ui.rendering.util.MobileOperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileDeviceRepository {
    
    private static final int MAX_USER_AGENT_LENGTH = 100;
    private static final int MIN_USER_AGENT_LENGTH = 10;
    private static final int DEFAULT_SCREEN_WIDTH = 320;
    private static final int DEFAULT_SCREEN_HEIGHT = 480;
    private static final int DEFAULT_PIXEL_DENSITY = 160;
    
    private static final Logger logger = LoggerFactory.getLogger(MobileDeviceRepository.class);
    
    public MobileDevice getMobileDevice(String userAgent) {
        if (userAgent == null || userAgent.length() < MIN_USER_AGENT_LENGTH || userAgent.length() > MAX_USER_AGENT_LENGTH) {
            logger.warn("Invalid user agent: {}", userAgent);
            return new MobileDevice(MobileDeviceType.UNKNOWN, MobileOperatingSystem.UNKNOWN, DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT, DEFAULT_PIXEL_DENSITY);
        }
        
        String[] tokens = userAgent.split(" ");
        MobileDeviceType deviceType = getDeviceType(tokens);
        MobileOperatingSystem operatingSystem = getOperatingSystem(tokens);
        
        int screenWidth = getScreenWidth(deviceType);
        int screenHeight = getScreenHeight(deviceType);
        int pixelDensity = getPixelDensity(deviceType);
        
        logger.info("Detected device: type={}, operatingSystem={}, screenWidth={}, screenHeight={}, pixelDensity={}", deviceType, operatingSystem, screenWidth, screenHeight, pixelDensity);
        
        return new MobileDevice(deviceType, operatingSystem, screenWidth, screenHeight, pixelDensity);
    }
    
    private MobileDeviceType getDeviceType(String[] tokens) {
        for (String token : tokens) {
            if (token.contains("iPhone")) {
                return MobileDeviceType.IPHONE;
            } else if (token.contains("Android")) {
                return MobileDeviceType.ANDROID;
            }
        }
        return MobileDeviceType.UNKNOWN;
    }
    
    private MobileOperatingSystem getOperatingSystem(String[] tokens) {
        for (String token : tokens) {
            if (token.contains("iOS")) {
                return MobileOperatingSystem.IOS;
            } else if (token.contains("Android")) {
                return MobileOperatingSystem.ANDROID;
            }
        }
        return MobileOperatingSystem.UNKNOWN;
    }
    
    private int getScreenWidth(MobileDeviceType deviceType) {
        switch (deviceType) {
            case IPHONE:
                return 375;
            case ANDROID:
                return 360;
            default:
                return DEFAULT_SCREEN_WIDTH;
        }
    }
    
    private int getScreenHeight(MobileDeviceType deviceType) {
        switch (deviceType) {
            case IPHONE:
                return 667;
            case ANDROID:
                return 740;
            default:
                return DEFAULT_SCREEN_HEIGHT;
        }
    }
    
    private int getPixelDensity(MobileDeviceType deviceType) {
        switch (deviceType) {
            case IPHONE:
                return 326;
            case ANDROID:
                return 420;
            default:
                return DEFAULT_PIXEL_DENSITY;
        }
    }
}