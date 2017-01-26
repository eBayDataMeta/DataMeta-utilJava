package org.ebay.datameta.test.util;

import java.util.Formatter;

/**
 * @author mbergens Michael Bergens
 */
public class LongTestUtils {

    /**
     * Would turn the long array into a readable hexadecimal image. For example a long value of
     * {@code 0xFEDCBA0987654321L} will be represented as {@code FEDC.BA09-8765.4321}
     */
    public static String getDumpImage(final long longs[], final int off, final int len) {
        if ((off < 0) || (off > longs.length) || (len < 0) ||
                   ((off + len) > longs.length) || ((off + len) < 0)) {
       	    throw new IndexOutOfBoundsException();
       	} else if (len == 0) {
       	    return "";
       	}
        StringBuilder sb = new StringBuilder(longs.length * (Long.SIZE * 2/8 + 4));
        Formatter fmt = new Formatter(sb);
        for(int ix = 0; ix < len; ix++) {
            long current = longs[off + ix];
            fmt.format("%04X.%04X-%04X.%04X ",
                (current >>> 48) & 0xFFFF,
                (current >>> 32) & 0xFFFF,
                (current >>> 16) & 0xFFFF,
                current  & 0xFFFF
            );
        }
        return sb.toString();
    }

    public static String getDumpImage(final long longs[]) {
        return getDumpImage(longs, 0, longs.length);
    }


}
