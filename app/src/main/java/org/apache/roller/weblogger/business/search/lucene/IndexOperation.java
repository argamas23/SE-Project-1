package org.apache.roller.weblogger.business.search.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IndexableField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.search.IndexException;
import org.apache.roller.weblogger.business.search.IndexHelper;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.util.UIDGenerator;

public class IndexOperation {
    private static final String INDEX_DIR = WebloggerRuntimeConfig.getWebloggerConfig().getLuceneIndexDir();
    private static final int MAX_RESULTS = 1000;
    private IndexWriter writer;
    private IndexHelper indexHelper;

    public IndexOperation() {
        indexHelper = new IndexHelper();
    }

    public void createIndex() throws IndexException {
        IndexWriterConfig config = new IndexWriterConfig(indexHelper.getAnalyzer());
        try {
            writer = new IndexWriter(INDEX_DIR, config);
        } catch (Exception e) {
            throw new IndexException("Failed to create index writer", e);
        }
    }

    public void closeIndex() throws IndexException {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                throw new IndexException("Failed to close index writer", e);
            }
        }
    }

    public void addDocument(WeblogEntry entry) throws IndexException {
        Document document = getDocument(entry);
        try {
            writer.addDocument(document);
        } catch (Exception e) {
            throw new IndexException("Failed to add document to index", e);
        }
    }

    public void deleteDocument(String id) throws IndexException {
        Term term = new Term("id", id);
        try {
            writer.deleteDocuments(term);
        } catch (Exception e) {
            throw new IndexException("Failed to delete document from index", e);
        }
    }

    public void updateDocument(WeblogEntry entry) throws IndexException {
        deleteDocument(entry.getId());
        addDocument(entry);
    }

    private Document getDocument(WeblogEntry entry) {
        Document document = new Document();
        document.add(new Field("id", entry.getId(), Store.YES, indexHelper.getAnalyzer()));
        document.add(new Field("title", entry.getTitle(), Store.YES, indexHelper.getAnalyzer()));
        document.add(new Field("content", entry.getContent(), Store.YES, indexHelper.getAnalyzer()));
        document.add(new Field("author", entry.getAuthor().getUserName(), Store.YES, indexHelper.getAnalyzer()));
        return document;
    }

    private FieldType getFieldType() {
        FieldType fieldType = new FieldType();
        fieldType.setTokenized(true);
        fieldType.setIndexed(true);
        fieldType.setStored(true);
        return fieldType;
    }

    public void addComment(WeblogEntryComment comment) throws IndexException {
        WeblogEntry entry = comment.getEntry();
        Document document = getDocument(comment);
        try {
            writer.addDocument(document);
        } catch (Exception e) {
            throw new IndexException("Failed to add comment to index", e);
        }
    }

    private Document getDocument(WeblogEntryComment comment) {
        Document document = new Document();
        document.add(new Field("id", UIDGenerator.getUUID(), Store.YES, indexHelper.getAnalyzer()));
        document.add(new Field("entryId", comment.getEntry().getId(), Store.YES, indexHelper.getAnalyzer()));
        document.add(new Field("content", comment.getContent(), Store.YES, indexHelper.getAnalyzer()));
        document.add(new Field("author", comment.getAuthor().getUserName(), Store.YES, indexHelper.getAnalyzer()));
        return document;
    }
}