package org.apache.roller.planet.business;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PlanetURLStrategy {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String HTTP_PORT = "80";
    private static final String HTTPS_PORT = "443";

    private static final Map<String, String> PORTS = new HashMap<>();

    static {
        PORTS.put(HTTP, HTTP_PORT);
        PORTS.put(HTTPS, HTTPS_PORT);
    }

    private String scheme;
    private String host;
    private String path;

    public PlanetURLStrategy(String url) throws URISyntaxException {
        URI uri = new URI(url);
        this.scheme = uri.getScheme();
        this.host = uri.getHost();
        this.path = uri.getPath();
    }

    public boolean isValid() {
        return scheme != null && host != null;
    }

    public boolean isSecure() {
        return HTTPS.equals(scheme);
    }

    public boolean isDefaultPort() {
        int port = getPort();
        return isSecure() && port == 443 || !isSecure() && port == 80;
    }

    public int getPort() {
        try {
            return Integer.parseInt(new URI(getURL()).getPort());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getURL() {
        return new StringBuilder()
                .append(scheme)
                .append("://")
                .append(host)
                .toString();
    }

    public boolean isHTTP() {
        return HTTP.equals(scheme);
    }

    public boolean isHTTPS() {
        return HTTPS.equals(scheme);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String buildURL(String path) {
        return new StringBuilder()
                .append(getURL())
                .append(path)
                .toString();
    }

    public String buildURL(String path, boolean isSecure) {
        String newScheme = isSecure ? HTTPS : HTTP;
        return new StringBuilder()
                .append(newScheme)
                .append("://")
                .append(host)
                .append(path)
                .toString();
    }

    @Override
    public String toString() {
        return "PlanetURLStrategy{" +
                "scheme='" + scheme + '\'' +
                ", host='" + host + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}