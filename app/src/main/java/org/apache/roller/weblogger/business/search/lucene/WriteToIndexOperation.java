<think>
Okay, let's see. The user provided a Java file and mentioned design smells. The main issues identified are Error Handling by Logging Only and Missing Exception Handling.

First, the original code has a catch block that logs an exception but doesn't re-throw it. That's a problem because it's hiding the error. I need to modify that to re-throw the exception after logging.

Next, the line manager.resetSharedReader(); is outside any try-catch block. If this method throws an exception, it won't be handled. So I should wrap that in a try-catch block to log and re-throw any exceptions there as well.

Also, the original code uses LogFactory.getFactory().getInstance(), which is outdated. It's better to use the static final logger approach for better performance and clarity.

I'll refactor the logger initialization to use the static final logger. Then, in the run method, after the try-catch-finally block, I'll add a try-catch around the resetSharedReader call. Both catch blocks should log the error and re-throw the exception to maintain the original behavior but with proper error handling.

Make sure that the lock is released in the finally block regardless of any exceptions. Also, check that the resetSharedReader is called even if an exception occurs during the doRun method, but handle any exceptions from reset properly.

Testing the flow: if doRun throws an exception, it's caught, logged, and re-thrown. The finally block releases the lock. Then, resetSharedReader is called in a try-catch, so any exceptions there are logged and re-thrown. This way, all exceptions are handled appropriately without being swallowed.
</think>

```java
package org.apache.roller.weblogger.business.search.lucene;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class WriteToIndexOperation extends IndexOperation {

    private static final Log logger = LogFactory.getLog(WriteToIndexOperation.class);

    @Override
    public void run() {
        try {
            manager.getReadWriteLock().writeLock().lock();
            logger.debug("Acquired write lock for operation: " + this.getClass().getSimpleName());
            doRun();
            logger.debug("Completed write operation: " + this.getClass().getSimpleName());
        } catch (Exception e) {
            logger.error("Error during write operation: " + this.getClass().getSimpleName(), e);
            throw new RuntimeException("Write operation failed", e);
        } finally {
            try {
                manager.getReadWriteLock().writeLock().unlock();
            } catch (Exception e) {
                logger.error("Error releasing write lock", e);
            }
        }

        try {
            manager.resetSharedReader();
        } catch (Exception e) {
            logger.error("Error resetting shared reader after write operation", e);
            throw new RuntimeException("Failed to reset shared reader", e);
        }
    }

    protected abstract void doRun();
}