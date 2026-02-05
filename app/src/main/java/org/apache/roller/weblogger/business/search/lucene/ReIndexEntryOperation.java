package org.apache.roller.weblogger.business.search.lucene;

import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.searchTermVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReIndexEntryOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReIndexEntryOperation.class);
    private static final int MAX_RESULT_SET_SIZE = 1000;
    private static final int PAGE_SIZE = 50;

    private WeblogEntryManager weblogEntryManager;
    private WeblogManager weblogManager;

    public ReIndexEntryOperation() {
        this.weblogEntryManager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        this.weblogManager = WebloggerFactory.getWeblogger().getWeblogManager();
    }

    public void reIndexEntry(WeblogEntry entry) {
        try {
            Weblog weblog = weblogManager.getWeblog(entry.getWeblog());
            List<WeblogEntry> entries = new ArrayList<>();
            entries.add(entry);
            reIndexEntries(weblog, entries);
        } catch (Exception e) {
            LOGGER.error("Error re-indexing entry", e);
        }
    }

    public void reIndexEntries(Weblog weblog, List<WeblogEntry> entries) {
        Directory directory = getDirectory(weblog);
        IndexWriterConfig config = new IndexWriterConfig();
        try (IndexWriter writer = new IndexWriter(directory, config)) {
            for (WeblogEntry entry : entries) {
                reIndexEntry(writer, entry);
            }
        } catch (IOException e) {
            LOGGER.error("Error re-indexing entries", e);
        }
    }

    private void reIndexEntry(IndexWriter writer, WeblogEntry entry) throws IOException {
        Query query = getQuery(entry);
        writer.deleteDocuments(query);
        writer.addDocument(getDocument(entry));
    }

    private Query getQuery(WeblogEntry entry) {
        BooleanQuery query = new BooleanQuery.Builder()
                .add(new Term(entry.getId().toString(), "id"))
                .build();
        return query;
    }

    private Document getDocument(WeblogEntry entry) {
        Document document = new Document();
        document.add(new TextField("id", entry.getId().toString(), Field.Store.YES));
        document.add(new TextField("title", entry.getTitle(), Field.Store.YES));
        document.add(new TextField("content", entry.getContent(), Field.Store.YES));
        return document;
    }

    private Directory getDirectory(Weblog weblog) {
        File file = new File(weblog.getName() + ".index");
        return FSDirectory.open(file);
    }

    public void reIndexAllEntries(Weblog weblog) {
        int startIndex = 0;
        while (true) {
            List<WeblogEntry> entries = weblogEntryManager.getRecentWeblogEntries(weblog, startIndex, PAGE_SIZE);
            if (entries.isEmpty()) {
                break;
            }
            reIndexEntries(weblog, entries);
            startIndex += PAGE_SIZE;
        }
    }

    public void reIndexEntries(WeblogEntry entry, int startIndex) {
        try {
            Weblog weblog = weblogManager.getWeblog(entry.getWeblog());
            List<WeblogEntry> entries = new ArrayList<>();
            entries.add(entry);
            reIndexEntries(weblog, entries);
        } catch (Exception e) {
            LOGGER.error("Error re-indexing entries", e);
        }
    }
}