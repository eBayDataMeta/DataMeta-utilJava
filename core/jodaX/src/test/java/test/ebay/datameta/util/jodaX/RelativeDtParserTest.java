package test.ebay.datameta.util.jodaX;

import org.ebay.datameta.util.jodaX.RelativeDttmParser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author michaelb Michael Bergens
 */
public class RelativeDtParserTest {
    
    private static final Logger L = LoggerFactory.getLogger(RelativeDtParserTest.class);
    private DateTimeFormatter formatter;
    private RelativeDttmParser parser;

    @Before public void init() throws Exception {
        L.info("**** Init start");
        formatter = ISODateTimeFormat.dateTime();
        parser = new RelativeDttmParser();
    }

    @After public void destroy() throws Exception {}

    private void testRelative(final DateTime source, final String spec, final DateTime expected) {
        final DateTime actual = parser.parse(source, spec);
        L.info("Adjusting " + formatter.print(source) + " by >>" + spec + "<<, result: " + formatter.print(actual));
        assertEquals(expected, actual);
    }
    
    @Test public void testRelative() {
        final DateTime source = new DateTime(2011, 3, 31, 15, 45, 54, 0);
        final DateTime sourceNoTime = new DateTime(2011, 3, 31, 0, 0, 0, 0);
        final DateTime lastDayOfLastMonthWithTime = new DateTime(2011, 2, 28, 15, 45, 54, 0);
        final DateTime lastDayOfLastMonthNoTime = new DateTime(2011, 2, 28, 0, 0, 0, 0);
        final DateTime lastDayOfYearFrom6MonthAgo = new DateTime(2010, 12, 31, 15, 45, 54, 0);
        final DateTime aYearAgo = new DateTime(2010, 3, 31, 15, 45, 54, 0);
        testRelative(source, "", source); // should be able just to specify current dt, useful for interpolation.
        testRelative(source, "Z", sourceNoTime);
        testRelative(source, "-1M=LD", lastDayOfLastMonthWithTime);
        testRelative(source, "-1M=LDZ", lastDayOfLastMonthNoTime);
        testRelative(source, "-1MZ=LD", lastDayOfLastMonthNoTime);
        testRelative(source, "Z-1M=LD", lastDayOfLastMonthNoTime);
        testRelative(source, "Z=LD-1M", lastDayOfLastMonthNoTime);
        testRelative(source, "-6M=LM=LD", lastDayOfYearFrom6MonthAgo);
        testRelative(source, "-365D", aYearAgo);
	}
	
}
