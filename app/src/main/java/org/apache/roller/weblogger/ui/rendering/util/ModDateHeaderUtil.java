package org.apache.roller.weblogger.ui.rendering.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.util.DateUtil;
import org.apache.roller.weblogger.util.RollerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModDateHeaderUtil {

    private static final Logger logger = LoggerFactory.getLogger(ModDateHeaderUtil.class);

    private static final int ONE_DAY_IN_SECONDS = 86400;
    private static final int ONE_WEEK_IN_SECONDS = 604800;
    private static final int ONE_MONTH_IN_SECONDS = 2629800;

    private enum TimePeriod {
        DAY(ONE_DAY_IN_SECONDS),
        WEEK(ONE_WEEK_IN_SECONDS),
        MONTH(ONE_MONTH_IN_SECONDS);

        private final int seconds;

        TimePeriod(int seconds) {
            this.seconds = seconds;
        }

        public int getSeconds() {
            return seconds;
        }
    }

    public static void addModDateHeader(Map<String, String> headers, Instant modDate) {
        if (modDate != null) {
            headers.put(RollerConstants.MOD_DATE_HEADER, DateTimeFormatter.RFC_1123_DATE_TIME.format(modDate.atZone(ZoneId.of(DateUtil.TZ_GMT))));
        }
    }

    public static void addMaxAgeHeader(Map<String, String> headers, TimePeriod timePeriod) {
        headers.put(RollerConstants.MAX_AGE_HEADER, String.valueOf(timePeriod.getSeconds()));
    }

    public static void addMaxAgeHeader(Map<String, String> headers, int maxAge) {
        headers.put(RollerConstants.MAX_AGE_HEADER, String.valueOf(maxAge));
    }

    public static Instant getModDate(Map<String, String> headers) {
        String modDateHeader = headers.get(RollerConstants.MOD_DATE_HEADER);
        if (modDateHeader != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                return Instant.from(formatter.parse(modDateHeader));
            } catch (Exception e) {
                logger.error("Error parsing mod date header", e);
            }
        }
        return null;
    }

    public static Map<String, Object> getCacheControlHeaders(String url) {
        Map<String, Object> cacheControlHeaders = new HashMap<>();

        // Cache settings for different types of URLs
        if (url.contains(WebloggerConfig.get("editor.url"))) {
            cacheControlHeaders.put(RollerConstants.CACHE_CONTROL_HEADER, RollerConstants.NO_CACHE);
        } else if (url.contains(WebloggerConfig.get("reader.url"))) {
            cacheControlHeaders.put(RollerConstants.CACHE_CONTROL_HEADER, RollerConstants.MAX_AGE_3600);
        } else {
            cacheControlHeaders.put(RollerConstants.CACHE_CONTROL_HEADER, RollerConstants.MAX_AGE_86400);
        }

        return cacheControlHeaders;
    }
}