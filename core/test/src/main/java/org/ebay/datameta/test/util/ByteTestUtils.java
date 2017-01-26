package org.ebay.datameta.test.util;

import java.io.ByteArrayInputStream;
import java.util.Formatter;

/**
 * @author mbergens Michael Bergens
 */
public class ByteTestUtils {

    public static String getByteArrayImage(final byte bytes[], final int off, final int len) {
        if ((off < 0) || (off > bytes.length) || (len < 0) ||
                   ((off + len) > bytes.length) || ((off + len) < 0)) {
       	    throw new IndexOutOfBoundsException();
       	} else if (len == 0) {
       	    return "";
       	}
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        Formatter fmt = new Formatter(sb);
        for(int ix = 0; ix < len; ix++) {
            fmt.format("%02X ", bytes[off + ix] & 0xFF);
        }
        return sb.toString();
    }

    public static String getByteArrayImage(final byte bytes[]) {
        return getByteArrayImage(bytes, 0, bytes.length);
    }

    // todo promote to testing package
    public static String byteArrayInputImage(final ByteArrayInputStream in) {
        final int available = in.available();
        in.mark(Integer.MAX_VALUE);
        if(available < 1) return "";
        final byte[] bytes = new byte[available];
        //noinspection ResultOfMethodCallIgnored
        in.read(bytes, 0, available);
        in.reset();
        return getByteArrayImage(bytes);
    }


}
