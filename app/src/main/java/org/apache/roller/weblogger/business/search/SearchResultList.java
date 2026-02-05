<think>
Okay, let's see. The user provided a Java class called SearchResultList and mentioned some design smells. I need to refactor it based on the detected issues.

First, the Long Parameter List. The constructor has four parameters. Maybe using a builder pattern would help here. That way, the parameters can be set incrementally, making the code more readable and easier to maintain.

Next, Exposing Internals. The getters return the actual collections (List and Set) which are mutable. If someone modifies these collections outside the class, it could change the internal state. To fix this, the getters should return unmodifiable copies. So, for the categories and results, I'll create defensive copies in the constructor and return unmodifiable versions in the getters.

Data Class is another issue. The class is just a data holder with no behavior. To address this, maybe add some methods that operate on the data, like calculating total results or checking if there are more pages. But since the user didn't specify any additional behavior, perhaps just making the class more robust by encapsulating the data better is sufficient.

Lack of Encapsulation is related to the fields being package-private. They should be private to prevent external modification. So, I'll change the fields to private and make sure the constructor initializes them properly.

Also, the fields like limit, offset, categories, and results should be final if they don't change after construction. That enforces immutability. The categories and results in the constructor should be copied to prevent external changes from affecting the internal state.

Wait, the original code's constructor doesn't make copies of the collections. So when the user passes a List or Set, if they modify it later, it could affect the SearchResultList. To prevent that, the constructor should create a defensive copy. So in the constructor, I'll assign new ArrayList and HashSet to the fields, copying the input collections. Then, the getters will return unmodifiable views of these copies.

Putting it all together: make the fields private and final, create defensive copies in the constructor, return unmodifiable collections in getters, and use a builder to handle the long parameter list. That should address the design smells mentioned.
</think>

```java
package org.apache.roller.weblogger.business.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SearchResultList {
    private final int limit;
    private final int offset;
    private final Set<String> categories;
    private final List<SearchResult> results;

    public static class Builder {
        private int limit;
        private int offset;
        private Set<String> categories = new HashSet<>();
        private List<SearchResult> results = new ArrayList<>();

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder setCategories(Set<String> categories) {
            this.categories = new HashSet<>(categories);
            return this;
        }

        public Builder setResults(List<SearchResult> results) {
            this.results = new ArrayList<>(results);
            return this;
        }

        public SearchResultList build() {
            return new SearchResultList(this);
        }
    }

    private SearchResultList(Builder builder) {
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.categories = Collections.unmodifiableSet(builder.categories);
        this.results = Collections.unmodifiableList(builder.results);
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public List<SearchResult> getResults() {
        return results;
    }
}