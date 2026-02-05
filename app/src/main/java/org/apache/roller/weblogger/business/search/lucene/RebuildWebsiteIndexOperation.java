package org.apache.roller.weblogger.business.search.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.roller.weblogger.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RebuildWebsiteIndexOperation {

    private static final Logger logger = LoggerFactory.getLogger(RebuildWebsiteIndexOperation.class);

    private static final String INDEX_DIR = "index";
    private static final String FIELD_WEBLOG_HANDLE = "weblogHandle";
    private static final String FIELD.Weblog_NAME = "weblogName";
    private static final String FIELD_ENTRY_ID = "entryId";
    private static final String FIELD_ENTRY_TITLE = "entryTitle";
    private static final String FIELD_ENTRY_CONTENT = "entryContent";
    private static final String FIELD_ENTRY_COMMENT = "entryComment";
    private static final int MAX_RESULTS = 1000;

    private WeblogManager weblogManager;
    private WeblogEntryManager weblogEntryManager;
    private IndexWriterConfig indexWriterConfig;
    private Directory directory;

    public RebuildWebsiteIndexOperation() {
        this.weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
        this.weblogEntryManager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        this.indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        this.directory = FSDirectory.open(WebloggerRuntimeConfig.getLuceneIndexDir());
    }

    public void execute() {
        try {
            rebuildIndex();
        } catch (IOException e) {
            logger.error("Error rebuilding index", e);
        }
    }

    private void rebuildIndex() throws IOException {
        IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
        writer.deleteAllDocuments();
        writer.commit();
        writer.close();

        List<Weblog> weblogs = getWeblogs();
        for (Weblog weblog : weblogs) {
            List<WeblogEntry> entries = getWeblogEntries(weblog);
            for (WeblogEntry entry : entries) {
                Document document = createDocument(entry);
                writer.addDocument(document);
            }
        }
        writer.close();
    }

    private List<Weblog> getWeblogs() {
        return weblogManager.getWeblogs();
    }

    private List<WeblogEntry> getWeblogEntries(Weblog weblog) {
        return weblogEntryManager.getWeblogEntries(weblog);
    }

    private Document createDocument(WeblogEntry entry) {
        Document document = new Document();
        document.add(new StringField(FIELD_WEBLOG_HANDLE, entry.getWeblog().getHandle(), Field.Store.YES));
        document.add(new StringField(FIELD.Weblog_NAME, entry.getWeblog().getName(), Field.Store.YES));
        document.add(new StringField(FIELD_ENTRY_ID, String.valueOf(entry.getId()), Field.Store.YES));
        document.add(new TextField(FIELD_ENTRY_TITLE, entry.getTitle(), Field.Store.YES));
        document.add(new TextField(FIELD_ENTRY_CONTENT, entry.getContent(), Field.Store.YES));
        List<WeblogEntryComment> comments = weblogEntryManager.getComments(entry);
        for (WeblogEntryComment comment : comments) {
            document.add(new TextField(FIELD_ENTRY_COMMENT, comment.getContent(), Field.Store.YES));
        }
        return document;
    }

    public static void main(String[] args) {
        RebuildWebsiteIndexOperation operation = new RebuildWebsiteIndexOperation();
        operation.execute();
    }
}