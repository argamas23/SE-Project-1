package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.ui.core.URLStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WeblogPreviewResourceRequest {
    
    private static final Log log = LogFactory.getLog(WeblogPreviewResourceRequest.class);
    private static final int INITIAL_CAPACITY = 10;
    private final Weblog weblog;
    private final User user;
    private final WeblogEntry entry;

    public WeblogPreviewResourceRequest(Weblog weblog, User user, WeblogEntry entry) {
        this.weblog = weblog;
        this.user = user;
        this.entry = entry;
    }

    public void service(OutputStream out, InputStream in) throws Exception {
        previewResources(out, in);
    }

    private void previewResources(OutputStream out, InputStream in) throws Exception {
        if (entry.isDraft()) {
            serveResource(out, in, getDraftResourcePath());
        } else {
            serveResource(out, in, getPublishedResourcePath());
        }
    }

    private String getDraftResourcePath() {
        return "/draft" + getEntryResourcePath();
    }

    private String getPublishedResourcePath() {
        return getEntryResourcePath();
    }

    private String getEntryResourcePath() {
        return "/" + weblog.getHandle() + "/" + entry.getId() + ".html";
    }

    private void serveResource(OutputStream out, InputStream in, String path) throws IOException, WebloggerException {
        URLStrategy urlStrategy = new URLStrategy();
        urlStrategy.serveResource(out, in, path);
    }

    public void loadEntryComments() {
        List<WeblogEntryComment> comments = new ArrayList<>(INITIAL_CAPACITY);
        // Initialize comments list with actual data
        comments = loadCommentsFromDatabase();
        entry.setComments(comments);
    }

    private List<WeblogEntryComment> loadCommentsFromDatabase() {
        // Load comments from database
        // For demonstration purposes, return an empty list
        return Collections.emptyList();
    }
}