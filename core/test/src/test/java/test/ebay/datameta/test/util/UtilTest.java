package test.ebay.datameta.test.util;

import org.ebay.datameta.test.util.JsonTestUtil;
import org.ebay.datameta.test.util.LongTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ebay.datameta.test.util.JsonTestUtil.readJsonArray;
import static com.google.common.io.Resources.getResource;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.Resources.asByteSource;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UtilTest {
    private static Logger L = LoggerFactory.getLogger(UtilTest.class);

    @Before public void init() throws Exception {}

    @After public void destroy() throws Exception {}

    @Test public void testReadObject() throws Exception {
        final String[] expected = {
            "{\"firstName\": \"John\", \"lastName\": \"Doe\",\"age\": 20, \"salary\": 70000.0,\"currency\": \"GBP\"}",
            "{\"firstName\": \"Mary\", \"lastName\": \"Smith\", \"age\": 21, \"salary\": 85000.0, \"currency\": \"USD\"}",
            "{\"firstName\": \"Johann\", \"lastName\": \"Kurz\", \"age\": 55, \"salary\": 48000.0, \"currency\": \"EUR\"}",
            "{ \"firstName\": \"Zorg\", \"lastName\": \"Xqewooh\", \"age\": 516, \"salary\": 12000.0,\"currency\": \"GALAXY_BUCKAZOID\"}"
        };
        readJsonArray(asByteSource(getResource(UtilTest.class, "/data.json")).openStream(),
            new JsonTestUtil.Consumer() {
                int recordCounter;
                @Override public void consume(String objectSource) {
                    L.info("Next object: \n" + objectSource + '\n');
                    assertThat(objectSource, is(expected[recordCounter]));
                    recordCounter++;
                }
            });
    }

    @Test public void testLongUtils() {
        final long cafeBabe = 0x1FEDCBA098765432L;
        final long negaBabe = 0xFEDCBA0987654321L;
        final long[] arr = new long[]{cafeBabe, negaBabe};
        L.info(String.format("cb=%,d = %<016X, neg =%,d = %<016X, arr=%s", cafeBabe, negaBabe,
            LongTestUtils.getDumpImage(arr)));
    }
}
