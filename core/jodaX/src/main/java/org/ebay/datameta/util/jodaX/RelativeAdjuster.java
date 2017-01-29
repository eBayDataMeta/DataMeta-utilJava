package org.ebay.datameta.util.jodaX;

import org.joda.time.MutableDateTime;

import static org.ebay.datameta.util.jodaX.RelativeDttmParser.BACK_TOKEN;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.DAY_UNIT;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.FORTH_TOKEN;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.MONTH_UNIT;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.getIllegalTimeUnitException;

/** Adjuster for relative specification like <tt>-2M</tt> (two months ago). See {@link RelativeDttmParser} for details.
 * @see RelativeDttmParser
 * @author michaelb Michael Bergens
 */
class RelativeAdjuster extends Adjuster {

    @Override public void adjust(final MutableDateTime origin, final String spec) {
        final char firstChar = spec.charAt(0);
        int multiplier;
        switch(firstChar) {
            case FORTH_TOKEN:
                multiplier = 1;
                break;
            case BACK_TOKEN:
                multiplier = -1;
                break;
            default: // not my spec, must be a different adjuster if any
                return;
        }
        final int len = spec.length();
        int amount = multiplier * Integer.parseInt(spec.substring(1, len - 1));
        final char timeUnitSpec = spec.charAt(len - 1);
        switch(timeUnitSpec) {
            case MONTH_UNIT:
                origin.addMonths(amount);
                break;
            case DAY_UNIT:
                origin.addDays(amount);
                break;
            default:
                throw getIllegalTimeUnitException(timeUnitSpec);
        }
    }
}
