package org.apache.roller.weblogger.business.search.lucene;

import java.util.Arrays;

/**
 * Field constants for Lucene search
 */
public class FieldConstants {

    // Field names
    public static final String TITLE_FIELD = "title";
    public static final String CONTENT_FIELD = "content";
    public static final String CATEGORY_FIELD = "category";
    public static final String TAGS_FIELD = "tags";
    public static final String AUTHOR_FIELD = "author";
    public static final String PUBLISHED_FIELD = "published";

    // Field weights
    public enum FieldWeight {
        TITLE(10),
        CONTENT(5),
        CATEGORY(8),
        TAGS(6),
        AUTHOR(4),
        PUBLISHED(2);

        private final int weight;

        FieldWeight(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }
    }

    // Indexed fields
    public static final String[] INDEXED_FIELDS = {
        TITLE_FIELD,
        CONTENT_FIELD,
        CATEGORY_FIELD,
        TAGS_FIELD,
        AUTHOR_FIELD,
        PUBLISHED_FIELD
    };
}