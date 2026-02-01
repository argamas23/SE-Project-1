## `IndexManager`

The primary interface defining the contract for Roller's full-text search subsystem. It hides the underlying implementation details (Lucene) from the rest of the application.

Methods:
* `initialize()`
* `shutdown()`
* `release()`
* `isInconsistentAtStartup()`
* `addEntryIndexOperation(WeblogEntry)`
* `addEntryReIndexOperation(WeblogEntry)`
* `rebuildWeblogIndex(Weblog)`
* `removeWeblogIndex(Weblog)`
* `removeEntryIndexOperation(WeblogEntry)`
* `search(...)`

---

## `LuceneIndexManager`

The implementation of `IndexManager` using Apache Lucene. It acts as the central controller, managing the index directory, concurrency locks, and operation scheduling. It creates and schedules `IndexOperation` instances; manages the shared `IndexReader`.

Attributes:
* `reader`: Holds the shared Lucene `IndexReader` instance.
* `roller`: Reference to the main `Weblogger` business application.
* `searchEnabled`: Flag indicating if search functionality is active.
* `indexDir`: Filesystem path to the Lucene index directory.
* `indexConsistencyMarker`: File object used to detect unclean shutdowns.
* `rwl`: A `ReadWriteLock` to manage concurrent access to the index.


Methods:
* `initialize()`: Sets up the index directory and checks for consistency markers.
* `rebuildWeblogIndex(...)`: Schedules the `RebuildWebsiteIndexOperation`.
* `removeWeblogIndex(...)`: Schedules the `RemoveWebsiteIndexOperation`.
* `addEntryIndexOperation(...)`: Schedules the `AddEntryOperation`.
* `addEntryReIndexOperation(...)`: Schedules the `ReIndexEntryOperation`.
* `removeEntryIndexOperation(...)`: Executes `RemoveEntryOperation` immediately.
* `search(...)`: Configures and executes a `SearchOperation`, then converts hits to a result list.
* `getReadWriteLock()`: Returns the lock object for operations to synchronize on.
* `isInconsistentAtStartup`: Returns a boolean flag indicating if the index was found in a potentially corrupt state (or missing) when the application started
* `getAnalyzer()`: Factory method to instantiate the configured Lucene Analyzer.
* `instantiateAnalyzer()`: Attempts to instantiate the specific Lucene Analyzer class defined in the `lucene.analyzer.class` configuration property, falling back to the default if loading fails.
* `instantiateDefaultAnalyzer`: Helper method that returns a standard Lucene `StandardAnalyzer` instance, used as a fallback when the custom analyzer configuration fails.
* `scheduleIndexOperation(...)`: Submits an operation to the background thread pool.
* `executeIndexOperationNow(...)`: Runs an operation immediately in the current thread.
* `resetSharedReader`: Sets the internal shared `IndexReader` to null, forcing the system to re-open the index (and thus see the latest updates) the next time a read is requested.
* `getSharedIndexReader()`: returns (or opens) the shared `IndexReader`.
* `getIndexDirectory()`: Returns the Lucene FSDirectory object corresponding to the file.
* `indexExists()`: Checks the file system to determine if a valid Lucene index already exists at the configured location.
* `deleteIndex()`: Deletes the file index, if it exists.
* `createIndex(...)`: Initializes a new, empty Lucene index in the provided directory.
* `shutdown()`: Performs clean termination tasks.
* `convertHitsToEntryList(...)`: Converts raw Lucene `ScoreDoc` hits into Roller `WeblogEntryWrapper` objects.


---


## `IndexOperation`

Base class for all index tasks. It encapsulates the logic for mapping Roller objects to Lucene documents and managing the `IndexWriter`. Extended by specific operation classes.

Attributes:
* `manager`: Reference to the central `LuceneIndexManager`.
* `writer`: The Lucene `IndexWriter` used for modifying the index.


Methods:
* `getDocument(WeblogEntry)`: Converts a `WeblogEntry` POJO into a Lucene `Document`.
* `beginWriting()`: Initializes and returns the `IndexWriter`.
* `endWriting()`: Closes the `IndexWriter` safely.
* `run()`
* `doRun()`: Method where subclasses implement specific business logic.



## `ReadFromIndexOperation`

Decorator for read-only operations. Acquires the read lock from the manager before execution.

Methods:
* `run()`: Acquires the read lock, calls `doRun()`, and releases the lock.



## `WriteToIndexOperation`

Decorator for modification operations. Acquires the write lock from the manager before execution.

Methods:
* `run()`: Acquires the write lock, calls `doRun()`, releases the lock, and resets the shared reader.



---


## `SearchOperation`

Performs the actual search query against the index. Uses `MultiFieldQueryParser` to parse terms and `IndexSearcher` to find results.

Attributes:
* `logger`: Static Log instance used to record debug information and error stack traces.
* `SEARCH_FIELDS`: Static constant array defining the specific index fields that will be queried.
* `SORTER`: Static constant defining the sort order of results (descending sort by the PUBLISHED date).
* `searcher`: The Lucene `IndexSearcher` used to execute the query.
* `searchresults`: Stores the `TopFieldDocs` returned by the search.
* `term`, `weblogHandle`, `category`, `locale`: Search criteria fields.


Methods:
* `setTerm(...)`, `setWeblogHandle(...)`, etc.: Setters for search criteria.
* `doRun()`: Constructs the BooleanQuery and executes the search.
* `getResults()`: Returns the raw search results.
* `getResultsCount()`: Returns the total number of hits.
* `getSearcher()`: Returns the current IndexSearcher instance being used by this operation.
* `setSearcher(...)`: Manually sets the IndexSearcher instance.
* `getParseError()`: Returns the error message string if an exception occurred during query parsing.
* `setWeblogHandle(...)`: Sets the weblog handle to filter results by a specific blog.
* `setCategory(...)`: Sets the category name to filter results by a specific topic.
* `setLocale(...)`: Sets the locale code to filter results by language/region.


## `AddEntryOperation`

Adds a new blog entry to the index. Re-fetches the entry from the DB to ensure data freshness before indexing.

Attributes:
* `data`: The `WeblogEntry` to be indexed.
* `roller`: Reference to Weblogger to fetch fresh data.


Methods:
* `doRun()`: Re-fetches the entry and adds its document to the index.



## `ReIndexEntryOperation`

Updates an existing entry in the index. Deletes the old document and adds the new one.

Attributes:
* `data`: The `WeblogEntry` to be updated.
* `roller`: Reference to Weblogger.


Methods:
* `doRun()`: Deletes the document by ID and adds the updated document.



## `RemoveEntryOperation`

Removes a specific entry from the index. Deletes documents matching the entry ID.

Attributes:
* `data`: The `WeblogEntry` to be removed.
* `roller`: Reference to Weblogger.


Methods:
* `doRun()`: Executes deletion of the document with the matching ID term.



## `RemoveWebsiteIndexOperation`

Removes all entries associated with a specific website/blog. Deletes documents matching the website handle.

Attributes:
* `website`: The `Weblog` whose index entries should be removed.
* `roller`: Reference to Weblogger.


Methods:
* `doRun()`: Deletes all documents containing the website handle term.



## `RebuildWebsiteIndexOperation`

Completely rebuilds the index for a specific website or the entire site. Deletes existing entries and iterates through all published entries in the database to re-add them.

Attributes:
* `website`: The `Weblog` to rebuild (null for all sites).
* `roller`: Reference to Weblogger.


Methods:
* `doRun()`: Clears relevant index data, queries DB for all published entries, and adds them to the index.



---

## `SearchResultList`

A container for search results returned to the client.

Attributes:
* `limit`: The maximum number of results requested.
* `offset`: The starting index of the results.
* `categories`: A set of categories found within the search results.
* `results`: The list of actual `WeblogEntryWrapper` objects.


Methods:
* `getLimit()`: Returns the search limit.
* `getOffset()`: Returns the offset.
* `getResults()`: Returns the list of entries.
* `getCategories()`: Returns the set of categories.



## `SearchResultMap`

A container that organizes search results by date (e.g., for calendar views).

Attributes:
* `limit`: The maximum number of results requested.
* `offset`: The starting index of the results.
* `categories`: A set of categories found within the search results.
* `results`: A map where keys are dates and values are sets of entries.


Methods:
* `getLimit()`: Returns the search limit.
* `getOffset()`: Returns the offset.
* `getResults()`: Returns the map of date-grouped entries.
* `getCategories()`: Returns the set of categories.



## `FieldConstants`

Defines the schema (field names) for the Lucene index to ensure consistent field naming.


## `IndexUtil`

Utility class for helper functions.

Methods:
* `getTerm(field, input)`: Converts a raw string input into a Lucene `Term` using the system's Analyzer.