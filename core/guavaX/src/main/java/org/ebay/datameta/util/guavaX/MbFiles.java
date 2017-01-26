package org.ebay.datameta.util.guavaX;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
/**
 * @author michaelb Michael Bergens
 */
public class MbFiles {

    /**
     * Builds a Set of non-blank strings from a file or a resource where the IDs are located one at a line.
     *
     * @param linesSource the source - anything identifiable by a URL; it can be a file, in which case you pass
     * {@code new URL("file:...")} to this parameter, or if it is a classpath resource, use {@link Resources#getResource(String)}
     * to get a URL of a classpath resource. See the unit tests for examples.
     *
     * @param expectedSize a guestimate of how many IDs may be in that file or resource.
     *
     * @return immutable set containing all the IDs in the file.
     */
    public static Set<String> getIdsAsSet(final URL linesSource, final int expectedSize) {
        try {
            return Resources.readLines(linesSource, Charset.defaultCharset(), new LineProcessor<Set<String>>() {
                final Set<String> readIds = Sets.newHashSetWithExpectedSize(expectedSize);
                @Override public boolean processLine(final String line) throws IOException {
                    final String trimmed = line.trim();
                    if(!isBlank(trimmed)) readIds.add(trimmed);
                    return true;
                }

                @Override public Set<String> getResult() {
                    return ImmutableSet.copyOf(readIds);
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
