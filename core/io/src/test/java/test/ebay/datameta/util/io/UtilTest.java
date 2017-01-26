package test.ebay.datameta.util.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilTest {
    private static Logger L = LoggerFactory.getLogger(UtilTest.class);

    @Before public void init() throws Exception {}

    @After public void destroy() throws Exception {}

    @Test public void testIt() throws Exception {
        L.info("Tested");
    }
}
