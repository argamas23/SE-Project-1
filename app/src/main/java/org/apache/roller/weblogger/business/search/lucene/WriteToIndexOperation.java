package org.apache.roller.weblogger.business.search.lucene;

import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.roller.weblogger.pojos.WeblogEntry;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

public class WriteToIndexOperation {

    private static final int BATCH_SIZE = 100;
    private static final int MAX_RETRIES = 3;

    private final WebloggerFactory webloggerFactory;
    private final StandardAnalyzer analyzer;

    public WriteToIndexOperation(WebloggerFactory webloggerFactory) {
        this.webloggerFactory = webloggerFactory;
        this.analyzer = new StandardAnalyzer();
    }

    public void execute() throws IOException {
        try (IndexWriter indexWriter = createIndexWriter()) {
            int retryCount = 0;
            while (retryCount <= MAX_RETRIES) {
                try {
                    writeEntriesToIndex(indexWriter);
                    break;
                } catch (Exception e) {
                    if (retryCount >= MAX_RETRIES) {
                        throw e;
                    }
                    retryCount++;
                }
            }
        }
    }

    private IndexWriter createIndexWriter() throws IOException {
        Directory directory = FSDirectory.open(Paths.get("./index"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(directory, config);
    }

    private void writeEntriesToIndex(IndexWriter indexWriter) throws IOException {
        WeblogEntry[] entries = webloggerFactory.getWeblogEntryManager().getRecentWeblogEntries(BATCH_SIZE);
        for (WeblogEntry entry : entries) {
            Document document = createDocument(entry);
            indexWriter.addDocument(document);
        }
    }

    private Document createDocument(WeblogEntry entry) {
        Document document = new Document();
        document.add(new StringField("id", String.valueOf(entry.getId()), Field.Store.YES));
        document.add(new TextField("title", entry.getTitle(), Field.Store.YES));
        document.add(new TextField("content", entry.getContent(), Field.Store.YES));
        document.add(new StringField("permalink", entry.getPermalink(), Field.Store.YES));
        document.add(new StringField("username", entry.getUsername(), Field.Store.YES));
        document.add(new StringField("timezone", entry.getTimezone(), Field.Store.YES));
        document.add(new StringField("locale", entry.getLocale(), Field.Store.YES));
        document.add(new StringField("dateCreated", String.valueOf(new Date(entry.getDateCreated()).getTime()), Field.Store.YES));
        return document;
    }
}