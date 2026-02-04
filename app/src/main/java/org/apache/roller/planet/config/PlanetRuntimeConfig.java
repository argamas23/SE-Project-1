package org.apache.roller.planet.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.roller.planet.utils.PlanetConstants;
import org.apache.roller.planet.utils.PlanetUtils;

import java.util.HashMap;
import java.util.Map;

public class PlanetRuntimeConfig {
    
    private Map<String, String> configMap;

    public PlanetRuntimeConfig() {
        this.configMap = new HashMap<>();
        initConfig();
    }

    private void initConfig() {
        configMap.put(PlanetConstants.CONFIG_KEY_1, PlanetConstants.CONFIG_VALUE_1);
        configMap.put(PlanetConstants.CONFIG_KEY_2, PlanetConstants.CONFIG_VALUE_2);
        // Add more config key-value pairs as needed
    }

    public String getConfigValue(String key) {
        return configMap.get(key);
    }

    public String getPlanetName() {
        return PlanetUtils.getPlanetName();
    }

    public String getPlanetFeedUrl() {
        return PlanetUtils.getPlanetFeedUrl();
    }

    public String getPlanetDescription() {
        return PlanetUtils.getPlanetDescription();
    }

    public String getPlanetAddress() {
        return PlanetUtils.getPlanetAddress();
    }

    public String getPlanetOwner() {
        return PlanetUtils.getPlanetOwner();
    }

    public boolean isPlanetEnabled() {
        String enabled = getConfigValue(PlanetConstants.CONFIG_KEY_ENABLED);
        return StringUtils.isNotEmpty(enabled) && Boolean.parseBoolean(enabled);
    }

    public int getRefreshInterval() {
        String interval = getConfigValue(PlanetConstants.CONFIG_KEY_REFRESH_INTERVAL);
        return StringUtils.isNotEmpty(interval) ? Integer.parseInt(interval) : PlanetConstants.DEFAULT_REFRESH_INTERVAL;
    }
}