package org.apache.roller.weblogger.business.search.lucene;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.roller.weblogger.business.search.IndexManager;
import org.apache.roller.weblogger.business.search.IndexSession;
import org.apache.roller.weblogger.business.search.IndexUtil;
import org.apache.roller.weblogger.business.search.SearchException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TermQuery;

public class RemoveEntryOperation {

    private static final Logger LOGGER = Logger.getLogger(RemoveEntryOperation.class.getName());
    private static final String FIELD_ENTRY_ID = "entryId";
    private static final QueryParser QUERYPARSER = new QueryParser(FIELD_ENTRY_ID);

    private final IndexManager indexManager;

    public RemoveEntryOperation(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    public void removeEntry(String entryId) {
        try (IndexSession indexSession = indexManager.openIndexSession()) {
            removeEntryFromIndex(indexSession, entryId);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error removing entry from index", e);
            throw new SearchException("Error removing entry from index", e);
        }
    }

    private void removeEntryFromIndex(IndexSession indexSession, String entryId) throws IOException {
        Term term = new Term(FIELD_ENTRY_ID, entryId);
        TermQuery query = new TermQuery(term);

        IndexUtil.deleteDocuments(indexSession, query);
    }

    private Term createTerm(String entryId) {
        return new Term(FIELD_ENTRY_ID, entryId);
    }
}