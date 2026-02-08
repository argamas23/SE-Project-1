package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.pojos.Comment;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.util.URLUtil;
import org.apache.roller.weblogger.ui.rendering.util.WeblogURLStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class TrackbackLinkbackCommentValidator {

    private static final int MAX_COMMENT_LENGTH = 1000;
    private static final int MAX_TRACKBACK_LENGTH = 2000;
    private static final int TRACKBACK_SUCCESS_CODE = 200;
    private static final int TRACKBACK_ERROR_CODE = 500;

    private static final Logger log = Logger.getLogger(TrackbackLinkbackCommentValidator.class.getName());

    public void validate(Comment comment) throws WebloggerException {
        if (comment != null && comment.getContent() != null) {
            Weblog weblog = comment.getWeblog();
            WeblogURLStrategy urlStrategy = WeblogURLStrategy.getInstance();
            String trackbackUrl = urlStrategy.getTrackbackUrl(weblog);

            if (trackbackUrl != null) {
                if (comment.getContent().length() > MAX_COMMENT_LENGTH) {
                    throw new WebloggerException("Comment content exceeds maximum allowed length of " + MAX_COMMENT_LENGTH + " characters");
                }

                if (!isValidTrackbackUrl(trackbackUrl)) {
                    throw new WebloggerException("Invalid trackback URL: " + trackbackUrl);
                }

                try {
                    sendTrackbackRequest(trackbackUrl, comment);
                } catch (IOException e) {
                    log.warning("Error sending trackback request: " + e.getMessage());
                    throw new WebloggerException("Error sending trackback request: " + e.getMessage());
                }
            }
        }
    }

    private boolean isValidTrackbackUrl(String trackbackUrl) {
        try {
            URL url = new URL(trackbackUrl);
            if (url.getProtocol().equals("http") || url.getProtocol().equals("https")) {
                return true;
            }
        } catch (MalformedURLException e) {
            log.warning("Invalid trackback URL: " + trackbackUrl);
        }
        return false;
    }

    private void sendTrackbackRequest(String trackbackUrl, Comment comment) throws IOException {
        URL url = new URL(trackbackUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String trackbackData = "title=" + comment.getTitle() + "&url=" + comment.getUrl() + "&excerpt=" + comment.getExcerpt();
        if (trackbackData.length() > MAX_TRACKBACK_LENGTH) {
            throw new WebloggerException("Trackback data exceeds maximum allowed length of " + MAX_TRACKBACK_LENGTH + " characters");
        }

        httpConnection.setDoOutput(true);
        httpConnection.getOutputStream().write(trackbackData.getBytes());

        int responseCode = httpConnection.getResponseCode();
        if (responseCode != TRACKBACK_SUCCESS_CODE) {
            throw new WebloggerException("Trackback request failed with response code " + responseCode);
        }

        try (InputStream inputStream = httpConnection.getInputStream()) {
            // Close the input stream to free up resources
        }
    }
}