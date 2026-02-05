package org.apache.roller.weblogger.business.search.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.search.SearchConstants;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.pojos.WeblogEntrySearchResult;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AddEntryOperation {

    private static final String ENTRY_TITLE_FIELD = "entryTitle";
    private static final String ENTRY_CONTENT_FIELD = "entryContent";
    private static final String ENTRY_AUTHOR_FIELD = "entryAuthor";
    private static final String ENTRY_PUBLISH_DATE_FIELD = "entryPublishDate";
    private static final String ENTRY_WEBLOG_FIELD = "entryWeblog";

    private WeblogEntry entry;
    private Weblogger weblogger;
    private WebloggerFactory webloggerFactory;

    public AddEntryOperation(WeblogEntry entry) {
        this.entry = entry;
        this.weblogger = WebloggerFactory.getWeblogger();
        this.webloggerFactory = WebloggerFactory.getFactory();
    }

    public void execute() throws WebloggerException {
        addEntryToIndex();
    }

    private void addEntryToIndex() throws WebloggerException {
        addEntryDetailsToIndex();
        addEntryCommentsToIndex();
    }

    private void addEntryDetailsToIndex() throws WebloggerException {
        weblogger.getIndexManager().addDocument(createEntryDocument());
    }

    private void addEntryCommentsToIndex() throws WebloggerException {
        List<WeblogEntryComment> comments = weblogger.getWeblogEntryManager().getCommentsByEntry(entry);
        for (WeblogEntryComment comment : comments) {
            weblogger.getIndexManager().addDocument(createCommentDocument(comment));
        }
    }

    private org.apache.lucene.document.Document createEntryDocument() {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

        doc.add(new org.apache.lucene.index.Term(ENTRY_TITLE_FIELD, entry.getTitle()));
        doc.add(new org.apache.lucene.index.Term(ENTRY_CONTENT_FIELD, entry.getText()));
        doc.add(new org.apache.lucene.index.Term(ENTRY_AUTHOR_FIELD, entry.getCreator().getUserName()));
        doc.add(new org.apache.lucene.index.Term(ENTRY_PUBLISH_DATE_FIELD, Util.dateToString(entry.getPubTime(), Util.LUCENE_DATE_FORMAT)));
        doc.add(new org.apache.lucene.index.Term(ENTRY_WEBLOG_FIELD, entry.getWeblog().getHandle()));

        return doc;
    }

    private org.apache.lucene.document.Document createCommentDocument(WeblogEntryComment comment) {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

        doc.add(new org.apache.lucene.index.Term(ENTRY_TITLE_FIELD, comment.getContent()));
        doc.add(new org.apache.lucene.index.Term(ENTRY_CONTENT_FIELD, comment.getContent()));
        doc.add(new org.apache.lucene.index.Term(ENTRY_AUTHOR_FIELD, comment.getCreator().getUserName()));
        doc.add(new org.apache.lucene.index.Term(ENTRY_PUBLISH_DATE_FIELD, Util.dateToString(comment.getPubTime(), Util.LUCENE_DATE_FORMAT)));
        doc.add(new org.apache.lucene.index.Term(ENTRY_WEBLOG_FIELD, comment.getWeblogEntry().getWeblog().getHandle()));

        return doc;
    }

    private boolean isValidEntry(WeblogEntry entry) {
        return entry != null && entry.getWeblog() != null && entry.getCreator() != null;
    }

    private boolean isValidComment(WeblogEntryComment comment) {
        return comment != null && comment.getWeblogEntry() != null && comment.getCreator() != null;
    }
}