package org.apache.roller.weblogger.business.search.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.Weblog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadFromIndexOperation {

    private static final Logger LOG = LoggerFactory.getLogger(ReadFromIndexOperation.class);
    private static final String INDEX_DIR = "lucene/index";
    private static final String WEBLOG_ID_FIELD = "weblogId";
    private static final String ENTRY_ID_FIELD = "entryId";

    public List<WeblogEntry> readEntries(Weblog weblog) {
        List<WeblogEntry> entries = new ArrayList<>();
        try (Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
             IndexReader indexReader = DirectoryReader.open(directory)) {

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            QueryParser parser = new QueryParser(WEBLOG_ID_FIELD, new StandardAnalyzer());
            Query query = parser.parse(String.valueOf(weblog.getId()));

            TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                WeblogEntry entry = Weblogger.getInstance().getWeblogEntryManager().getWeblogEntry(
                        Long.parseLong(document.get(ENTRY_ID_FIELD)));
                if (entry != null) {
                    entries.add(entry);
                }
            }
        } catch (IOException | ParseException e) {
            LOG.error("Error reading from index", e);
            throw new WebloggerException("Error reading from index", e);
        }
        return entries;
    }

    public WeblogEntry readEntry(WeblogEntry entry) {
        try (Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
             IndexReader indexReader = DirectoryReader.open(directory)) {

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            TermQuery query = new TermQuery(new Term(ENTRY_ID_FIELD, String.valueOf(entry.getId())));

            TopDocs topDocs = indexSearcher.search(query, 1);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            if (scoreDocs.length > 0) {
                Document document = indexSearcher.doc(scoreDocs[0].doc);
                return Weblogger.getInstance().getWeblogEntryManager().getWeblogEntry(
                        Long.parseLong(document.get(ENTRY_ID_FIELD)));
            }
        } catch (IOException e) {
            LOG.error("Error reading from index", e);
            throw new WebloggerException("Error reading from index", e);
        }
        return null;
    }
}