package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogComment;
import org.apache.roller.weblogger.util.URLUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AkismetCommentValidator {

    private static final Log log = LogFactory.getLog(AkismetCommentValidator.class);
    private static final String USER_AGENT = "Roller Akismet Comment Validator";
    private static final String AKISMET_API_URL = "https://rest.akismet.com/1.1/comment-check";
    private static final int MAX_COMMENT_CONTENT_LENGTH = 4000;
    private static final int MAX_USER_AGENT_LENGTH = 128;
    private static final Pattern USER_AGENT_PATTERN = Pattern.compile("^.*\\(.*\\).*");

    private WeblogEntryManager weblogEntryManager;
    private WebloggerRuntimeConfig runtimeConfig;

    public AkismetCommentValidator() {
        this.weblogEntryManager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        this.runtimeConfig = WebloggerFactory.getWeblogger().getRuntimeConfig();
    }

    public boolean validateComment(WeblogEntry entry, WeblogComment comment) throws WebloggerException {
        if (!shouldCheckComment(comment)) {
            return true;
        }

        String commentContent = truncateCommentContent(comment);
        String userAgent = truncateUserAgent(comment.getUserAgent());

        try {
            String response = sendCommentToAkismet(entry, comment, commentContent, userAgent);
            return response.equals("true");
        } catch (IOException e) {
            throw new WebloggerException("Error sending comment to Akismet", e);
        }
    }

    private boolean shouldCheckComment(WeblogComment comment) {
        if (comment.getAuthor() == null || comment.getAuthor().isEmpty()) {
            return false;
        }
        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            return false;
        }
        return true;
    }

    private String truncateCommentContent(WeblogComment comment) {
        if (comment.getContent().length() > MAX_COMMENT_CONTENT_LENGTH) {
            return comment.getContent().substring(0, MAX_COMMENT_CONTENT_LENGTH);
        }
        return comment.getContent();
    }

    private String truncateUserAgent(String userAgent) {
        if (userAgent.length() > MAX_USER_AGENT_LENGTH) {
            return userAgent.substring(0, MAX_USER_AGENT_LENGTH);
        }
        return userAgent;
    }

    private String sendCommentToAkismet(WeblogEntry entry, WeblogComment comment, String commentContent, String userAgent) throws IOException {
        URL url = new URL(AKISMET_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setDoOutput(true);

        String data = "blog=" + URLUtilities.escape(entry.getPermalink()) +
                "&user_ip=" + URLUtilities.escape(comment.getIp()) +
                "&user_agent=" + URLUtilities.escape(userAgent) +
                "&referrer=" + URLUtilities.escape(comment.getReferrer()) +
                "&permalink=" + URLUtilities.escape(entry.getPermalink()) +
                "&comment=" + URLUtilities.escape(commentContent) +
                "&comment_type=comment";

        connection.getOutputStream().write(data.getBytes());
        connection.getOutputStream().close();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Invalid response code: " + responseCode);
        }

        java.util.Scanner scanner = new java.util.Scanner(connection.getInputStream(), "UTF-8");
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();
        connection.disconnect();

        return response.trim();
    }
}