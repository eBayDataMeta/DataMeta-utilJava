package org.ebay.datameta.util.jodaX;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates simple parsing of relative datetime specifications. We found one FOSS project for this purpose
 * named <a href="https://github.com/samtingleff/jchronic" target="_blank">jchronic</a>, but it much fatter
 * than we need, uses totally wrong approach to parsing complex datetime queries (at the level of complexity
 * that they aim to support it becomes a task of creating a specific domain language of freeform datetime specifications
 * with formulae and use a parser framework such as
 * <a href="http://www.antlr.org">Antlr</a>); also notable poor documentation and design lacking coherence.
 * Hence the decision to create this dead-on simple parser simple enough to be userful for our simple purpose.
 * <br/><p><b>Datetime Unit specifications</b>
 * There are following units currently supported, case important:
 * <ul>
 * <li><tt>M</tt> - month</ll>
 * <li><tt>D</tt> - day</ll>
 * </ul></p>
 * <p>There are 3 kinds of <b>relative/concrete cardinal dttm adjustments</b> specifications:
 * <ul>
 * <li>Set cardinal, including special notation for "last": starts with "<tt>=</tt>" followed by a number or uppercase "<tt>L</tt>"
 * for "last" of a next bigger unit, followed by a unit spec described above&uarr;.
 * Next bigger unit for a day is month; next bigger unit for a month is year.
 * <br/>For example, "<tt>=1D</tt>" - first day of the current month, "<tt>=2M</tt>" - 2<sup>nd</sup> month of the current year
 * , "<tt>=LD</tt>" - last day of the month.
 * Other parts of the datetime do not get affected by first/last scrolling, they should take care of separately.
 * So if you need 1st day of the second month, specify "<tt>=2M1D</tt>".</li>
 * <li>Plus/Minus amount: starts with "+" or "-", then digits telling how much back/forth and the unit, case important:
 * - for example, "<tt>-12D</tt>" meaning "twelve days back".</li>
 * <li>Zap/Zero time part: literally upppercase letter "<tt>Z</tt>"</li>
 * </ul>
 * <p>
 * Can combine the specs, such as "<tt>=1DZ</tt>" meaning "first day of current month, drop the time part, i.e. set time
 * to midnight of the day. Or, "<tt>-1M=1D+1DZ</tt>" - 1<sup>st</sup> day of the last month, midnight.</p>
 * <p>Empty string results in current datetime.</p>
 * <p>The class is immutable, thread safe, can be kept in a singleton scope.</p>
 * <br/><p><b>Weeks are not supported</b> because of complicated semantics, math that requires a lot of scenarios to test
 * and no value for our current tasks.</p>
 * @author michaelb Michael Bergens
 */
public class RelativeDttmParser {

    private static final Logger L = LoggerFactory.getLogger(RelativeDttmParser.class);
    static final DateTimeFormatter DATE_TIME_LOGGING_FORMAT = ISODateTimeFormat.dateTime();
    static final char ZAP_TIME_TOKEN = 'Z';
    static final char SET_VAL_TOKEN = '=';
    static final char FORTH_TOKEN = '+';
    static final char BACK_TOKEN = '-';
    static final char LAST_ADJUSTMENT = 'L';
    static final char MONTH_UNIT = 'M';
    static final char DAY_UNIT = 'D';

    static final String ALL_SUPPORTED_UNIT_SPECS = "" + MONTH_UNIT + DAY_UNIT;
    static final Pattern RELATIVE_SPECS_PATTERN
        = Pattern.compile("Z|=[\\dL]+[" + ALL_SUPPORTED_UNIT_SPECS + "]|[+\\-]\\d+[" + ALL_SUPPORTED_UNIT_SPECS + "]");

    private static final Adjuster ZAP_TIME_ADJUSTER = new Adjuster() {
        @Override public void adjust(final MutableDateTime origin, final String spec) {
            if(spec.charAt(0) == ZAP_TIME_TOKEN) origin.setTime(0);
        }

        @Override public String toString() { return "ZapTime"; }
    };

    public static IllegalArgumentException getIllegalTimeUnitException(final char timeUnit) {
        return new IllegalArgumentException("Invalid Time Unit: " + timeUnit);
    }
    private static final Adjuster[] ADJUSTERS = new Adjuster[] {ZAP_TIME_ADJUSTER, new RelativeAdjuster(), new SetToValueAdjuster()};

    /**
     * Performs the parsing of the original datetime by the specs as described in the javadocs for this class.
     * @return adjusted value.
     * @see RelativeDttmParser
     */
    public DateTime parse(final DateTime origin, final String adjustmentSpec) {
        final Matcher matcher = RELATIVE_SPECS_PATTERN.matcher(adjustmentSpec);
        final MutableDateTime dt = origin.toMutableDateTime();
        if(L.isDebugEnabled()) L.debug("Origin: " + DATE_TIME_LOGGING_FORMAT.print(origin)
            + ", specs: >>" + adjustmentSpec + "<<"); // must check log level manually because of print method call
        while(matcher.find()) {
            final String stepSpec = matcher.group();
            for(Adjuster adjuster: ADJUSTERS) {
                adjuster.adjust(dt, stepSpec);
                if(L.isDebugEnabled()) L.debug("Step: >>" + stepSpec + "<<, adjuster: " + adjuster + ", result: "
                    + DATE_TIME_LOGGING_FORMAT.print(dt));
            }
        }
        return dt.toDateTime();
    }
}
