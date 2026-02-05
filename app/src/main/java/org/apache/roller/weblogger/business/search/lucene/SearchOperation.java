package org.apache.roller.weblogger.business.search.lucene;

import org.apache.roller.weblogger.business.search.Query;
import org.apache.roller.weblogger.business.search.SearchException;
import org.apache.roller.weblogger.business.search.SearchResult;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchOperation {

    private static final int MAX_RESULTS = 100;

    private final IndexSearcher indexSearcher;
    private final StandardAnalyzer analyzer;

    public SearchOperation(IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
        this.analyzer = new StandardAnalyzer();
    }

    public List<SearchResult> search(String queryStr) throws SearchException {
        try {
            QueryParser parser = new QueryParser("content", analyzer);
            org.apache.lucene.search.Query luceneQuery = parser.parse(queryStr);
            return search(luceneQuery);
        } catch (ParseException e) {
            throw new SearchException("Error parsing query", e);
        }
    }

    public List<SearchResult> search(org.apache.lucene.search.Query luceneQuery) throws SearchException {
        try {
            TopDocs topDocs = indexSearcher.search(luceneQuery, MAX_RESULTS);
            return createSearchResults(topDocs.scoreDocs);
        } catch (IOException e) {
            throw new SearchException("Error searching index", e);
        }
    }

    private List<SearchResult> createSearchResults(ScoreDoc[] scoreDocs) throws IOException {
        List<SearchResult> searchResults = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            searchResults.add(createSearchResult(scoreDoc));
        }
        return searchResults;
    }

    private SearchResult createSearchResult(ScoreDoc scoreDoc) throws IOException {
        return new SearchResult(indexSearcher.doc(scoreDoc.doc));
    }

    public enum SearchType {
        BLOG(1),
        CATEGORY(2),
        ENTRY(3),
        USER(4);

        private final int value;

        SearchType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public SearchType determineSearchType(int value) {
        for (SearchType type : SearchType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}