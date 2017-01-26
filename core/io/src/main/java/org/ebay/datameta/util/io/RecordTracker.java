package org.ebay.datameta.util.io;

import org.ebay.datameta.util.jdk.Api;
import org.apache.commons.io.input.CountingInputStream;

/**
 * Augments the {@link CountingInputStream} by adding record counting, diversifying good, bad, errored and total.
 * To define these:
 * <dl>
 * <dt>Good</dt><dd>- successfully parsed, read or otherwise obtained; use the {@link #incGoodRec()} method to register one.</dd>
 * <dt>Bad</dt><dd>- failed by data validation by formatting and/or business rules, not by an exception; use the {@link #incBadRec()} method to register one</dd>
 * <dt>Error</dt><dd>- failed by an exception thrown by the code, use the {@link #incErrorRec()} method to register this case.</dd>
 * </dl>
 * <p>The totals are tallied automatically by summing these 3 up, see the {@link #getTotalRecCount()} method.</>
 * <p>It's up to the client to define the semantics of "Bad" vs "Error" on case by case basis.</p>
 * <p><b>Make sure you do not double-count any!</b></p>
 * <p><b>Not thread-safe!</b></p>
 *
 * @author michaelb Michael Bergens
 */
@Api public class RecordTracker {
    
    private long goodCount;
    private long badCount;
    private long errorCount;

    @Api public void incGoodRec() { goodCount++; }
    
    @Api public void incBadRec() { badCount++; }
    
    @Api public void incErrorRec() { errorCount++; }

    @Api public long getGoodRecCount() { return goodCount; }

    @Api public long getBadRecCount() { return badCount; }

    @Api public long getErrorRecCount() { return errorCount; }
    
    @Api public long getTotalRecCount() { return goodCount + badCount + errorCount;}
    
    
}
