package org.apache.roller.weblogger.business.search;

import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.logging.Logger;

public class IndexManager {

    private static final Logger log = Logger.getLogger(IndexManager.class.getName());
    private static final String INDEX_DIR = "index";

    private Directory indexDir;
    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    public IndexManager() throws IOException {
        initIndex();
    }

    private void initIndex() throws IOException {
        File indexFile = new File(RollerContext.getRealPath(INDEX_DIR));
        if (!indexFile.exists()) {
            indexFile.mkdir();
        }
        indexDir = FSDirectory.open(indexFile);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_30, new StandardAnalyzer(Version.LUCENE_30));
        indexWriter = new IndexWriter(indexDir, config);
        indexReader = IndexReader.open(indexDir, true);
        indexSearcher = new IndexSearcher(indexReader);
    }

    public void createIndex() throws IOException {
        List<Weblog> weblogs = WebloggerFactory.getWeblogger().getWeblogs();
        for (Weblog weblog : weblogs) {
            createWeblogIndex(weblog);
        }
        indexWriter.close();
    }

    private void createWeblogIndex(Weblog weblog) throws IOException {
        deleteWeblogIndex(weblog);
        List<WeblogEntry> entries = weblog.getEntries(0, 1000);
        for (WeblogEntry entry : entries) {
            Document document = createDocument(entry);
            indexWriter.addDocument(document);
        }
    }

    private Document createDocument(WeblogEntry entry) {
        Document document = new Document();
        document.add(new Field("id", String.valueOf(entry.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("title", entry.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("content", entry.getContent(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("permalink", entry.getPermalink(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        return document;
    }

    public void deleteWeblogIndex(Weblog weblog) throws IOException {
        Term term = new Term("id", weblog.getId());
        indexWriter.deleteDocuments(term);
        indexWriter.flush();
    }

    public List<WeblogEntry> search(String queryStr) throws IOException {
        QueryParser parser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
        Query query;
        try {
            query = parser.parse(queryStr);
        } catch (org.apache.lucene.queryParser.ParseException e) {
            throw new IOException(e.getMessage(), e);
        }
        TopDocs results = indexSearcher.search(query, 100);
        List<WeblogEntry> entries = new ArrayList<>();
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            WeblogEntry entry = WebloggerFactory.getWeblogger().getWeblogEntry(Integer.parseInt(document.get("id")));
            entries.add(entry);
        }
        return entries;
    }

    public void close() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
        if (indexReader != null) {
            indexReader.close();
        }
        if (indexDir != null) {
            indexDir.close();
        }
    }
}