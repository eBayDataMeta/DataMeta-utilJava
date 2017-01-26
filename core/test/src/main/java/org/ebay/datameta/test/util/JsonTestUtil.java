package org.ebay.datameta.test.util;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for using JSON with testing.
 */
public class JsonTestUtil {

    /**
     * Interface for the consumer to consumer the individual JSON objects from the stream;
     * this method will be called once with the source of whole text of the next JSON object in the input.
     * @see #consume
     */
    public static interface Consumer {
        void consume(final String objectSource);
    }
    private static enum State {
        /**
         * Not in an object - before the opening square brake.
         */
        INIT,
        /**
         * Reading objects.
         */
        OBJECT
    }

    /**
     * Some JSON framework are unable to read JSON array file and render each object to the consumer.
     * Others can not read proper format, they read an object stream instead. This class
     * allows reading this special format:
     * <ul>
     * <li>Opening square bracket that should be a single symbol on the line</li>
     * <li>One JSON object definition</li>
     * <li>comma, single symbol on the line</li>
     * <li>Next JSON object</li>
     * <li>...repeat as needed...</li>
     * <li>Closing square bracket, a single symbol on the line</li>
     * </ul>
     * Example:
     * <pre>
     *     [
     *     {"firstName": "John"
     *          , "lastName": "Doe", "age": 20, "salary": 70000.0}
     *     ,
     *     {"firstName": "Mary",
     *       "lastName": "Smith",
     *       "age": 21, "salary": 85000.0
     *       }
     *     ]
     * </pre>
     * This is quick and dirty implementation to avoid parsing the structure, described above.
     * @param consumer {@link Consumer}
     */
    public static void readJsonArray(@Nonnull final InputStream inputStream, @Nonnull final Consumer consumer)
        throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); 
        String line;
        final StringBuilder jsonObject = new StringBuilder(2048); /* normally would make a setting but this code is for testing
                                                                     therefore hardcoded should be fine */
        State state = State.INIT;
        while((line = reader.readLine()) != null) {
            final String trimmedLine = line.trim();
            switch(state) {
                case INIT:
                    if("[".equals(trimmedLine)) {
                        state = State.OBJECT;
                    }
                    break;
                case OBJECT:
                    if("]".equals(trimmedLine)) {
                        if(jsonObject.length() > 0) consumer.consume(jsonObject.toString());
                        return;
                    }
                    else if(",".equals(trimmedLine)) {
                        if(jsonObject.length() > 0) {
                            consumer.consume(jsonObject.toString());
                            jsonObject.setLength(0);
                        }
                    }
                    else jsonObject.append(trimmedLine);
                    break;
            }
        }
        
    }
}
