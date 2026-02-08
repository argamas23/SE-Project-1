package org.apache.roller.weblogger.ui.rendering.util;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.MediaFile;
import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.util.Tools;
import org.apache.roller.weblogger.util.RollerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeblogMediaResourceRequest {

    private static final Logger logger = LoggerFactory.getLogger(WeblogMediaResourceRequest.class);
    private static final String MEDIA_FILE_PARAM = "mediaFileId";
    private static final String ENTRY_ID_PARAM = "entryId";
    private static final String WEBLOG_ID_PARAM = "weblogId";

    private Weblog weblog;
    private MediaFile mediaFile;
    private boolean isMediaFileRequest;
    private boolean isEntryAttachmentRequest;

    public WeblogMediaResourceRequest(RollerContext context) {
        weblog = getWeblog(context);
        mediaFile = getMediaFile(context);
        isMediaFileRequest = isMediaFileRequest(context);
        isEntryAttachmentRequest = isEntryAttachmentRequest(context);
    }

    private Weblog getWeblog(RollerContext context) {
        String weblogId = context.getRequest().getParameter(WEBLOG_ID_PARAM);
        if (weblogId == null) {
            return null;
        }
        try {
            return WebloggerFactory.getWeblogger().getWeblog(weblogId);
        } catch (WebloggerException e) {
            logger.error("Error getting weblog", e);
            return null;
        }
    }

    private MediaFile getMediaFile(RollerContext context) {
        String mediaFileId = context.getRequest().getParameter(MEDIA_FILE_PARAM);
        if (mediaFileId == null) {
            return null;
        }
        try {
            return WebloggerFactory.getWeblogger().getMediaFile(mediaFileId);
        } catch (WebloggerException e) {
            logger.error("Error getting media file", e);
            return null;
        }
    }

    private boolean isMediaFileRequest(RollerContext context) {
        return context.getRequest().getParameter(MEDIA_FILE_PARAM) != null;
    }

    private boolean isEntryAttachmentRequest(RollerContext context) {
        String entryId = context.getRequest().getParameter(ENTRY_ID_PARAM);
        if (entryId == null) {
            return false;
        }
        try {
            return WebloggerFactory.getWeblogger().getEntry(entryId).getAttachments().size() > 0;
        } catch (WebloggerException e) {
            logger.error("Error checking for entry attachments", e);
            return false;
        }
    }

    public Weblog getWeblog() {
        return weblog;
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public boolean isMediaFileRequest() {
        return isMediaFileRequest;
    }

    public boolean isEntryAttachmentRequest() {
        return isEntryAttachmentRequest;
    }
}