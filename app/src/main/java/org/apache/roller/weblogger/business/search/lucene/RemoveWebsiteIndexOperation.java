package org.apache.roller.weblogger.business.search.lucene;

import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.search.IndexQueueManager;
import org.apache.roller.weblogger.config.WebloggerContext;
import org.apache.roller.weblogger.util.URLUtil;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WeblogEntry;
import org.apache.roller.weblogger.business.Weblog;
import org.apache.roller.weblogger.business.Comment;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RemoveWebsiteIndexOperation {
    
    private static final int BATCH_SIZE = 1000;
    private Weblogger context;
    private Weblog weblog;

    public RemoveWebsiteIndexOperation(Weblogger context, Weblog weblog) {
        this.context = context;
        this.weblog = weblog;
    }

    public void execute() throws Exception {
        FSDirectory directory = getDirectory();
        IndexWriter writer = getIndexWriter(directory);
        removeWebsiteIndex(writer, weblog);
        writer.close();
        directory.close();
    }

    private FSDirectory getDirectory() throws IOException {
        return FSDirectory.open(new File(getIndexPath()));
    }

    private IndexWriter getIndexWriter(FSDirectory directory) throws IOException {
        return new IndexWriter(directory, context.getIndexAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
    }

    private String getIndexPath() {
        return context.getIndexPath();
    }

    private void removeWebsiteIndex(IndexWriter writer, Weblog weblog) throws Exception {
        removeWeblogIndex(writer, weblog);
        removeWeblogEntryIndex(writer, weblog);
        removeCommentIndex(writer, weblog);
    }

    private void removeWeblogIndex(IndexWriter writer, Weblog weblog) throws IOException {
        writer.deleteDocuments(new Term("weblogHandle", weblog.getHandle()));
    }

    private void removeWeblogEntryIndex(IndexWriter writer, Weblog weblog) throws IOException {
        for (WeblogEntry entry : getWeblogEntries(weblog)) {
            writer.deleteDocuments(new Term("entryId", entry.getId()));
        }
    }

    private void removeCommentIndex(IndexWriter writer, Weblog weblog) throws IOException {
        for (Comment comment : getComments(weblog)) {
            writer.deleteDocuments(new Term("commentId", comment.getId()));
        }
    }

    private WeblogEntry[] getWeblogEntries(Weblog weblog) {
        return context.getWeblogEntryManager().getWeblogEntries(weblog, null, BATCH_SIZE, null, false);
    }

    private Comment[] getComments(Weblog weblog) {
        return context.getCommentManager().getCommentsForWeblog(weblog, null, BATCH_SIZE);
    }
}