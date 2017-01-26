package org.ebay.datameta.util.jdk;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author Michael Bergens
 */
public class JdkUtil {

    /**
     * Null-tolerant, non-disruptive close all call for the <tt><b>finally</b></tt> clause.
     *
     * @param errHandler error handler if any, see {@link CatchHandler}
     * @param closeables   to safely close (which by the way closes all related result sets) one by one, nulls ignored.
     * @deprecated Use Java 7'th try-with-resources instead.
     */
    @Api public void closeAll(@Nullable final CatchHandler<IOException> errHandler, final Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (IOException e) {
                if(errHandler != null) errHandler.handle(e);
            }
        }
    }
}
