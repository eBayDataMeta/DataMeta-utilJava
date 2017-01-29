package org.ebay.datameta.util.jodaX;

import org.joda.time.MutableDateTime;

import static org.ebay.datameta.util.jodaX.RelativeDttmParser.DAY_UNIT;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.LAST_ADJUSTMENT;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.MONTH_UNIT;
import static org.ebay.datameta.util.jodaX.RelativeDttmParser.getIllegalTimeUnitException;

/**
 * Adjuster cardinal value setting like <tt>=2D</tt> (2<sup>nd</sup> day of the current month).
 * See {@link RelativeDttmParser} for details.
 *
 * @author michaelb Michael Bergens
 * @see RelativeDttmParser
 */
class SetToValueAdjuster extends Adjuster {

    private void adjustToLast(final MutableDateTime origin, final char unitSpec) {
        switch (unitSpec) {
            case MONTH_UNIT:
                origin.setMonthOfYear(origin.monthOfYear().getMaximumValue());
                break;

            case DAY_UNIT:
                origin.setDayOfMonth(origin.dayOfMonth().getMaximumValue());
                break;

            default:
                throw getIllegalTimeUnitException(unitSpec);
        }
    }

    private void adjustToCardinal(final MutableDateTime origin, final int cardinal, final char unitSpec) {
        switch (unitSpec) {
            case MONTH_UNIT:
                origin.setMonthOfYear(cardinal);
                break;

            case DAY_UNIT:
                origin.setDayOfMonth(cardinal);
                break;

            default:
                throw getIllegalTimeUnitException(unitSpec);
        }
    }

    @Override public void adjust(final MutableDateTime origin, final String spec) {
        final char firstChar = spec.charAt(0);
        if (firstChar != RelativeDttmParser.SET_VAL_TOKEN) return;
        final char secondChar = spec.charAt(1);
        final int len = spec.length();
        final char unitSpec = spec.charAt(len - 1);
        if (secondChar == LAST_ADJUSTMENT) {
            adjustToLast(origin, unitSpec);
        }
        else {
            adjustToCardinal(origin, Integer.parseInt(spec.substring(1, len - 1)), unitSpec);
        }
    }
}
