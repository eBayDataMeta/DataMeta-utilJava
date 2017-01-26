package test.ebay.datameta.util.guavaX;

import org.ebay.datameta.util.guavaX.MbFiles;
import com.google.common.io.Resources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.Assert.assertTrue;

public class UtilTest {
    private static Logger L = LoggerFactory.getLogger(UtilTest.class);
    @Before public void init() throws Exception {}

    @After public void destroy() throws Exception {}

    @Test public void testFiles() throws Exception {
        // L.info(null); - not OK anymore
        L.info("tested");

        L.info("from file: " + MbFiles.getIdsAsSet(new URL("file:./pom.xml"), 150));
        final URL resourceUrl = Resources.getResource("log4j2-test.xml");
        L.info("Resource URL:" + resourceUrl);
        L.info("from classpath: " + MbFiles.getIdsAsSet(resourceUrl, 50));
        assertTrue("As expected", true);
    }

}
