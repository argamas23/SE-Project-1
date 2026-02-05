package org.apache.roller.weblogger.business.search.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WeblogEntry;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.util.UIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IndexUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexUtil.class);
    private static final String INDEX_DIR = "indexDir";
    private static final String ID_FIELD = "id";
    private static final String CONTENT_FIELD = "content";

    public void createIndex(WeblogEntry entry) {
        Directory directory = getDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        try (IndexWriter writer = new IndexWriter(directory, config)) {
            Document document = new Document();
            document.add(new StringField(ID_FIELD, entry.getHandle(), Field.Store.YES));
            document.add(new TextField(CONTENT_FIELD, entry.getTitle() + " " + entry.getSummary(), Field.Store.YES));
            writer.addDocument(document);
        } catch (IOException e) {
            LOGGER.error("Error creating index", e);
        }
    }

    public List<WeblogEntry> searchEntries(String query) {
        Directory directory = getDirectory();
        IndexReader reader = null;
        IndexSearcher searcher = null;
        try {
            reader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser(CONTENT_FIELD, new StandardAnalyzer());
            Query luceneQuery = parser.parse(query);
            TopDocs results = searcher.search(luceneQuery, 10);
            ScoreDoc[] hits = results.scoreDocs;
            List<WeblogEntry> entries = new ArrayList<>();
            for (ScoreDoc hit : hits) {
                Document hitDoc = searcher.doc(hit.doc);
                String id = hitDoc.get(ID_FIELD);
                WeblogEntry entry = Weblogger.getWeblogEntry(id);
                if (entry != null) {
                    entries.add(entry);
                }
            }
            return entries;
        } catch (IOException | ParseException e) {
            LOGGER.error("Error searching entries", e);
            return new ArrayList<>();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("Error closing IndexReader", e);
                }
            }
            if (searcher != null) {
                try {
                    searcher.getIndexReader().close();
                } catch (IOException e) {
                    LOGGER.error("Error closing IndexSearcher", e);
                }
            }
        }
    }

    private Directory getDirectory() {
        try {
            return FSDirectory.open(Paths.get(WebloggerRuntimeConfig.getProperty(INDEX_DIR)));
        } catch (IOException e) {
            LOGGER.error("Error getting directory", e);
            return null;
        }
    }
}