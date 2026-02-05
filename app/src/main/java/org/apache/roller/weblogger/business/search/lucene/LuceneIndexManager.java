// LuceneIndexManager.java

package org.apache.roller.weblogger.business.search.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneIndexManager {

    private static final Logger logger = LoggerFactory.getLogger(LuceneIndexManager.class);
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_AUTHOR = "author";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_CATEGORY = "category";
    private static final int MAX_RESULTS = 10;

    private Weblogger weblogger;
    private Directory directory;

    public LuceneIndexManager(Weblogger weblogger) throws WebloggerException {
        this.weblogger = weblogger;
        this.directory = createDirectory();
    }

    private Directory createDirectory() throws WebloggerException {
        try {
            return FSDirectory.open(Paths.get(WebloggerRuntimeConfig.getLuceneIndexDir()));
        } catch (IOException e) {
            throw new WebloggerException("Error creating directory", e);
        }
    }

    public void indexBlogEntry(User user, String title, String content) throws WebloggerException {
        try (IndexWriter writer = createIndexWriter()) {
            Document document = createDocument(user, title, content);
            writer.addDocument(document);
        } catch (IOException e) {
            throw new WebloggerException("Error indexing blog entry", e);
        }
    }

    private IndexWriter createIndexWriter() throws WebloggerException {
        try {
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            return new IndexWriter(directory, config);
        } catch (IOException e) {
            throw new WebloggerException("Error creating index writer", e);
        }
    }

    private Document createDocument(User user, String title, String content) {
        Document document = new Document();
        document.add(new TextField(FIELD_TITLE, title, Field.Store.YES));
        document.add(new TextField(FIELD_CONTENT, content, Field.Store.YES));
        document.add(new StringField(FIELD_AUTHOR, user.getFullName(), Field.Store.YES));
        return document;
    }

    public List<String> searchBlogEntries(String query, int limit) throws WebloggerException {
        try {
            QueryParser parser = new QueryParser(FIELD_CONTENT, new StandardAnalyzer());
            Query luceneQuery = parser.parse(query);
            return searchBlogEntries(luceneQuery, limit);
        } catch (Exception e) {
            throw new WebloggerException("Error searching blog entries", e);
        }
    }

    public List<String> searchBlogEntries(Query query, int limit) throws WebloggerException {
        try {
            // Create an IndexSearcher and search for the query
            // For simplicity, this example does not handle searching
            List<String> results = new ArrayList<>();
            // Add dummy data for testing
            for (int i = 0; i < limit; i++) {
                results.add("Result " + i);
            }
            return results;
        } catch (Exception e) {
            throw new WebloggerException("Error searching blog entries", e);
        }
    }

    public void close() {
        try {
            directory.close();
        } catch (IOException e) {
            logger.error("Error closing directory", e);
        }
    }

    public void rebuildIndex() throws WebloggerException {
        // Rebuild the index by deleting the existing index and re-indexing all blog entries
        try {
            deleteIndex();
            reIndexAllBlogEntries();
        } catch (Exception e) {
            throw new WebloggerException("Error rebuilding index", e);
        }
    }

    private void deleteIndex() throws WebloggerException {
        try {
            IndexWriter writer = createIndexWriter();
            writer.deleteAll();
            writer.close();
        } catch (IOException e) {
            throw new WebloggerException("Error deleting index", e);
        }
    }

    private void reIndexAllBlogEntries() throws WebloggerException {
        // For simplicity, this example does not handle re-indexing
        // In a real-world scenario, you would iterate over all blog entries and re-index them
    }
}