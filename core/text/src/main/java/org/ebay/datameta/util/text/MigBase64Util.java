package org.ebay.datameta.util.text;

import org.ebay.datameta.util.jdk.Api;

import java.util.Arrays;

/**
 * A very fast and memory efficient class to encode and decode to and from BASE64 in full accordance
 * with RFC 2045.<br><br>
 * On Windows XP sp1 with 1.4.2_04 and later ;), this encoder and decoder is about 10 times faster
 * on small arrays (10 - 1000 bytes) and 2-3 times as fast on larger arrays (10000 - 1000000 bytes)
 * compared to <tt>sun.misc.Encoder()/Decoder()</tt>.
 * <p/>
 * On byte arrays the encoder is about 20% faster than Jakarta Commons Base64 Codec for encode and
 * about 50% faster for decoding large arrays. This implementation is about twice as fast on very small
 * arrays (&lt 30 bytes). If source/destination is a <tt>String</tt> this
 * version is about three times as fast due to the fact that the Commons Codec result has to be recoded
 * to a <tt>String</tt> from <tt>byte[]</tt>, which is very expensive.
 * <p/>
 * This encode/decode algorithm doesn't create any temporary arrays as many other codecs do, it only
 * allocates the resulting array. This produces less garbage and it is possible to handle arrays twice
 * as large as algorithms that create a temporary array. (E.g. Jakarta Commons Codec). It is unknown
 * whether Sun's <tt>sun.misc.Encoder()/Decoder()</tt> produce temporary arrays but since performance
 * is quite low it probably does.
 * <p/>
 * The encoder produces the same output as the Sun one except that the Sun's encoder appends
 * a trailing line separator if the last character isn't a pad. Unclear why but it only adds to the
 * length and is probably a side effect. Both are in conformance with RFC 2045 though.<br>
 * Commons codec seem to always att a trailing line separator.
 * <p/>
 * <b>Note!</b>
 * The encode/decode method pairs (types) come in three versions with the <b>exact</b> same algorithm and
 * thus a lot of code redundancy. This is to not create any temporary arrays for transcoding to/from different
 * format types. The methods not used can simply be commented out.
 * <p/>
 * There is also a "fast" version of all decode methods that works the same way as the normal ones, but
 * har a few demands on the decoded input. Normally though, these fast verions should be used if the source if
 * the input is known and it hasn't bee tampered with.
 * <p/>
 * If you find the code useful or you find a bug, please send me a note at base64 @ miginfocom . com.
 * <p>
 * <u>Licence (BSD):</u>
 * </p><p>
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (base64 @ miginfocom . com)
 * All rights reserved.</p>
 * <p/>
 * <p>Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.</p>
 * <p/>
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 * </p>
 * <p>
 * <dl>
 * <dt>Date:</dt><dd>2004-Aug-02</dd>
 * <dt>Time:</dt><dd>11:31:11</dd>
 * </dl>
 * </p>
 *
 * <p>[michaelb] got it from <a href="http://migbase64.sourceforge.net" target="_blank">http://migbase64.sourceforge.net/</a>
 * </p>
 * @author Mikael Grev
 * @author michaelb
 * @version 2.2
 */
@Api public class MigBase64Util {
    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final int[] INTS = new int[256];

    static {
        Arrays.fill(INTS, -1);
        for (int i = 0, iS = CHARS.length; i < iS; i++)
            INTS[CHARS[i]] = i;
        INTS['='] = 0;
    }

    /**
     * Encodes a raw byte array into a BASE64 <tt>char[]</tt> representation i accordance with RFC 2045.
     *
     * @param source    The bytes to convert. If <tt>null</tt> or length 0 an empty array will be returned.
     * @param mustProvideLineSep set to true to provide "\r\n" after 76 characters, unless end of file.<br>
     *                No line separator will be in breach of RFC 2045 which specifies max 76 per line but will be a
     *                little faster.
     * @return A BASE64 encoded array. Never <tt>null</tt>.
     */
    public static char[] encodeToChar(byte[] source, boolean mustProvideLineSep) {
        // Check special case
        int sourceLen = source != null ? source.length : 0;
        if (sourceLen == 0)
            return new char[0];

        int evenLen = (sourceLen / 3) * 3;              // Length of even 24-bits.
        int resultCharCount = ((sourceLen - 1) / 3 + 1) << 2;   // Returned character count
        int resultLen = resultCharCount + (mustProvideLineSep ? (resultCharCount - 1) / 76 << 1 : 0); // Length of returned array
        char[] result = new char[resultLen];

        // Encode even 24-bits
        for (int s = 0, d = 0, cc = 0; s < evenLen;) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (source[s++] & 0xFF) << 16 | (source[s++] & 0xFF) << 8 | (source[s++] & 0xFF);

            // Encode the int into four chars
            result[d++] = CHARS[(i >>> 18) & 0x3F];
            result[d++] = CHARS[(i >>> 12) & 0x3F];
            result[d++] = CHARS[(i >>> 6) & 0x3F];
            result[d++] = CHARS[i & 0x3F];

            // Add optional line separator
            if (mustProvideLineSep && ++cc == 19 && d < resultLen - 2) {
                result[d++] = '\r';
                result[d++] = '\n';
                cc = 0;
            }
        }

        // Pad and encode last bits if source isn't even 24 bits.
        int left = sourceLen - evenLen; // 0 - 2.
        if (left > 0) {
            // Prepare the int
            int i = ((source[evenLen] & 0xFF) << 10) | (left == 2 ? ((source[sourceLen - 1] & 0xFF) << 2) : 0);

            // Set last four chars
            result[resultLen - 4] = CHARS[i >> 12];
            result[resultLen - 3] = CHARS[(i >>> 6) & 0x3F];
            result[resultLen - 2] = left == 2 ? CHARS[i & 0x3F] : '=';
            result[resultLen - 1] = '=';
        }
        return result;
    }

    /**
     * Decodes a BASE64 encoded char array. All illegal characters will be ignored and can handle both arrays with
     * and without line separators.
     *
     * @param encoded The source array. <tt>null</tt> or length 0 will return an empty array.
     * @return The decoded array of bytes. May be of length 0. Will be <tt>null</tt> if the legal characters
     *         (including '=') isn't divideable by 4.  (I.e. definitely corrupted).
     */
    @Api public static byte[] decode(char[] encoded) {
        // Check special case
        int sourceLen = encoded != null ? encoded.length : 0;
        if (sourceLen == 0)
            return new byte[0];

        // Count illegal characters (including '\r', '\n') to know what size the returned array will be,
        // so we don't have to reallocate & copy it later.
        int sepCount = 0; // Number of separator characters. (Actually illegal characters, but that's a bonus...)
        for (int i = 0; i < sourceLen; i++)  // If input is "pure" (I.e. no line separators or illegal chars) base64 this loop can be commented out.
            if (INTS[encoded[i]] < 0)
                sepCount++;

        // Check so that legal chars (including '=') are evenly divideable by 4 as specified in RFC 2045.
        if ((sourceLen - sepCount) % 4 != 0)
            return null;

        int pad = 0;
        for (int i = sourceLen; i > 1 && INTS[encoded[--i]] <= 0;)
            if (encoded[i] == '=')
                pad++;

        int len = ((sourceLen - sepCount) * 6 >> 3) - pad;

        byte[] result = new byte[len];       // Preallocate byte[] of exact length

        for (int s = 0, d = 0; d < len;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = 0;
            for (int j = 0; j < 4; j++) {   // j only increased if a valid char was found.
                int c = INTS[encoded[s++]];
                if (c >= 0)
                    i |= c << (18 - j * 6);
                else
                    j--;
            }
            // Add the bytes
            result[d++] = (byte) (i >> 16);
            if (d < len) {
                result[d++] = (byte) (i >> 8);
                if (d < len)
                    result[d++] = (byte) i;
            }
        }
        return result;
    }

    /**
     * Decodes a BASE64 encoded char array that is known to be resonably well formatted. The method is about twice as
     * fast as {@link #decode(char[])}. The preconditions are:<br>
     * + The array must have a line length of 76 chars OR no line separators at all (one line).<br>
     * + Line separator must be "\r\n", as specified in RFC 2045
     * + The array must not contain illegal characters within the encoded string<br>
     * + The array CAN have illegal characters at the beginning and end, those will be dealt with appropriately.<br>
     *
     * @param encoded The source array. Length 0 will return an empty array. <tt>null</tt> will throw an exception.
     * @return The decoded array of bytes. May be of length 0.
     */
    @Api public static byte[] decodeFast(char[] encoded) {
        // Check special case
        int sourceLen = encoded.length;
        if (sourceLen == 0)
            return new byte[0];

        int startIx = 0, endIx = sourceLen - 1;    // Start and end index after trimming.

        // Trim illegal chars from start
        while (startIx < endIx && INTS[encoded[startIx]] < 0)
            startIx++;

        // Trim illegal chars from end
        while (endIx > 0 && INTS[encoded[endIx]] < 0)
            endIx--;

        // get the padding count (=) (0, 1 or 2)
        int pad = encoded[endIx] == '=' ? (encoded[endIx - 1] == '=' ? 2 : 1) : 0;  // Count '=' at end.
        int contentCount = endIx - startIx + 1;   // Content count including possible separators
        int sepCount = sourceLen > 76 ? (encoded[76] == '\r' ? contentCount / 78 : 0) << 1 : 0;

        int len = ((contentCount - sepCount) * 6 >> 3) - pad; // The number of decoded bytes
        byte[] result = new byte[len];       // Preallocate byte[] of exact length

        // Decode all but the last 0 - 2 bytes.
        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = INTS[encoded[startIx++]] << 18 | INTS[encoded[startIx++]] << 12 | INTS[encoded[startIx++]] << 6 | INTS[encoded[startIx++]];

            // Add the bytes
            result[d++] = (byte) (i >> 16);
            result[d++] = (byte) (i >> 8);
            result[d++] = (byte) i;

            // If line separator, jump over it.
            if (sepCount > 0 && ++cc == 19) {
                startIx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            // Decode last 1-3 bytes (incl '=') into 1-3 bytes
            int i = 0;
            for (int j = 0; startIx <= endIx - pad; j++)
                i |= INTS[encoded[startIx++]] << (18 - j * 6);

            for (int r = 16; d < len; r -= 8)
                result[d++] = (byte) (i >> r);
        }

        return result;
    }

    /**
     * Encodes a raw byte array into a BASE64 <tt>byte[]</tt> representation i accordance with RFC 2045.
     *
     * @param decoded    The bytes to convert. If <tt>null</tt> or length 0 an empty array will be returned.
     * @param mustProvideLineSep pass true to ensure "\r\n" after 76 characters, unless end of file.<br>
     *                No line separator will be in breach of RFC 2045 which specifies max 76 per line but will be a
     *                little faster.
     * @return A BASE64 encoded array. Never <tt>null</tt>.
     */
    @Api public static byte[] encodeToByte(byte[] decoded, boolean mustProvideLineSep) {
        // Check special case
        int sourceLen = decoded != null ? decoded.length : 0;
        if (sourceLen == 0)
            return new byte[0];

        int evenLen = (sourceLen / 3) * 3;                              // Length of even 24-bits.
        int resultCharCount = ((sourceLen - 1) / 3 + 1) << 2;                   // Returned character count
        int resultLen = resultCharCount + (mustProvideLineSep ? (resultCharCount - 1) / 76 << 1 : 0); // Length of returned array
        byte[] result = new byte[resultLen];

        // Encode even 24-bits
        for (int s = 0, d = 0, cc = 0; s < evenLen;) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (decoded[s++] & 0xFF) << 16 | (decoded[s++] & 0xFF) << 8 | (decoded[s++] & 0xFF);

            // Encode the int into four chars
            result[d++] = (byte) CHARS[(i >>> 18) & 0x3F];
            result[d++] = (byte) CHARS[(i >>> 12) & 0x3F];
            result[d++] = (byte) CHARS[(i >>> 6) & 0x3F];
            result[d++] = (byte) CHARS[i & 0x3F];

            // Add optional line separator
            if (mustProvideLineSep && ++cc == 19 && d < resultLen - 2) {
                result[d++] = '\r';
                result[d++] = '\n';
                cc = 0;
            }
        }

        // Pad and encode last bits if source isn't an even 24 bits.
        int left = sourceLen - evenLen; // 0 - 2.
        if (left > 0) {
            // Prepare the int
            int i = ((decoded[evenLen] & 0xFF) << 10) | (left == 2 ? ((decoded[sourceLen - 1] & 0xFF) << 2) : 0);

            // Set last four chars
            result[resultLen - 4] = (byte) CHARS[i >> 12];
            result[resultLen - 3] = (byte) CHARS[(i >>> 6) & 0x3F];
            result[resultLen - 2] = left == 2 ? (byte) CHARS[i & 0x3F] : (byte) '=';
            result[resultLen - 1] = '=';
        }
        return result;
    }

    /**
     * Decodes a BASE64 encoded byte array. All illegal characters will be ignored and can handle both arrays with
     * and without line separators.
     *
     * @param encoded The source array. Length 0 will return an empty array. <tt>null</tt> will throw an exception.
     * @return The decoded array of bytes. May be of length 0. Will be <tt>null</tt> if the legal characters
     *         (including '=') isn't divideable by 4. (I.e. definitely corrupted).
     */
    @Api public static byte[] decode(byte[] encoded) {
        // Check special case
        int sourceLen = encoded == null ? 0 : encoded.length;
        if (sourceLen == 0)
            return new byte[0];

        // Count illegal characters (including '\r', '\n') to know what size the returned array will be,
        // so we don't have to reallocate & copy it later.
        int sepCount = 0; // Number of separator characters. (Actually illegal characters, but that's a bonus...)
        for (int i = 0; i < sourceLen; i++)      // If input is "pure" (I.e. no line separators or illegal chars) base64 this loop can be commented out.
            if (INTS[encoded[i] & 0xFF] < 0)
                sepCount++;

        // Check so that legal chars (including '=') are evenly dividable by 4 as specified in RFC 2045.
        if ((sourceLen - sepCount) % 4 != 0)
            return null;

        int pad = 0;
        for (int i = sourceLen; i > 1 && INTS[encoded[--i] & 0xFF] <= 0;)
            if (encoded[i] == '=')
                pad++;

        int len = ((sourceLen - sepCount) * 6 >> 3) - pad;

        byte[] result = new byte[len];       // Preallocate byte[] of exact length

        for (int s = 0, d = 0; d < len;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = 0;
            for (int j = 0; j < 4; j++) {   // j only increased if a valid char was found.
                int c = INTS[encoded[s++] & 0xFF];
                if (c >= 0)
                    i |= c << (18 - j * 6);
                else
                    j--;
            }

            // Add the bytes
            result[d++] = (byte) (i >> 16);
            if (d < len) {
                result[d++] = (byte) (i >> 8);
                if (d < len)
                    result[d++] = (byte) i;
            }
        }

        return result;
    }


    /**
     * Decodes a BASE64 encoded byte array that is known to be resonably well formatted. The method is about twice as
     * fast as {@link #decode(byte[])}. The preconditions are:<br>
     * + The array must have a line length of 76 chars OR no line separators at all (one line).<br>
     * + Line separator must be "\r\n", as specified in RFC 2045
     * + The array must not contain illegal characters within the encoded string<br>
     * + The array CAN have illegal characters at the beginning and end, those will be dealt with appropriately.<br>
     *
     * @param encoded The source array. Length 0 will return an empty array. <tt>null</tt> will throw an exception.
     * @return The decoded array of bytes. May be of length 0.
     */
    @Api public static byte[] decodeFast(byte[] encoded) {
        // Check special case
        int sourceLen = encoded == null ? 0 : encoded.length;
        if (sourceLen == 0)
            return new byte[0];

        int startIx = 0, endIx = sourceLen - 1;    // Start and end index after trimming.

        // Trim illegal chars from start
        while (startIx < endIx && INTS[encoded[startIx] & 0xFF] < 0)
            startIx++;

        // Trim illegal chars from end
        while (endIx > 0 && INTS[encoded[endIx] & 0xFF] < 0)
            endIx--;

        // get the padding count (=) (0, 1 or 2)
        int pad = encoded[endIx] == '=' ? (encoded[endIx - 1] == '=' ? 2 : 1) : 0;  // Count '=' at end.
        int contentCount = endIx - startIx + 1;   // Content count including possible separators
        int sepCount = sourceLen > 76 ? (encoded[76] == '\r' ? contentCount / 78 : 0) << 1 : 0;

        int len = ((contentCount - sepCount) * 6 >> 3) - pad; // The number of decoded bytes
        byte[] result = new byte[len];       // Preallocate byte[] of exact length

        // Decode all but the last 0 - 2 bytes.
        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = INTS[encoded[startIx++]] << 18 | INTS[encoded[startIx++]] << 12 | INTS[encoded[startIx++]] << 6
                | INTS[encoded[startIx++]];

            // Add the bytes
            result[d++] = (byte) (i >> 16);
            result[d++] = (byte) (i >> 8);
            result[d++] = (byte) i;

            // If line separator, jump over it.
            if (sepCount > 0 && ++cc == 19) {
                startIx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            // Decode last 1-3 bytes (incl '=') into 1-3 bytes
            int i = 0;
            for (int j = 0; startIx <= endIx - pad; j++)
                i |= INTS[encoded[startIx++]] << (18 - j * 6);

            for (int r = 16; d < len; r -= 8)
                result[d++] = (byte) (i >> r);
        }

        return result;
    }

    /**
     * Encodes a raw byte array into a BASE64 <tt>String</tt> representation i accordance with RFC 2045.
     *
     * @param decoded    The bytes to convert. If <tt>null</tt> or length 0 an empty array will be returned.
     * @param mustProvideLineSep pass true to provide "\r\n" after 76 characters, unless end of file.<br>
     *                No line separator will be in breach of RFC 2045 which specifies max 76 per line but will be a
     *                little faster.
     * @return A BASE64 encoded array. Never <tt>null</tt>.
     */
    @Api public static String encodeToString(byte[] decoded, boolean mustProvideLineSep) {
        // Reuse char[] since we can't create a String incrementally anyway and StringBuffer/Builder would be slower.
        return new String(encodeToChar(decoded, mustProvideLineSep));
    }

    /**
     * Decodes a BASE64 encoded <tt>String</tt>. All illegal characters will be ignored and can handle both strings with
     * and without line separators.<br>
     * <b>Note!</b> It can be up to about 2x the speed to call <tt>decode(str.toCharArray())</tt> instead. That
     * will create a temporary array though. This version will use <tt>str.charAt(i)</tt> to iterate the string.
     *
     * @param encoded The source string. <tt>null</tt> or length 0 will return an empty array.
     * @return The decoded array of bytes. May be of length 0. Will be <tt>null</tt> if the legal characters
     *         (including '=') isn't dividable by 4.  (I.e. definitely corrupted).
     */
    @Api public static byte[] decode(String encoded) {
        // Check special case
        int sourceLen = encoded != null ? encoded.length() : 0;
        if (sourceLen == 0)
            return new byte[0];

        // Count illegal characters (including '\r', '\n') to know what size the returned array will be,
        // so we don't have to reallocate & copy it later.
        int sepCount = 0; // Number of separator characters. (Actually illegal characters, but that's a bonus...)
        for (int i = 0; i < sourceLen; i++)  // If input is "pure" (I.e. no line separators or illegal chars) base64 this loop can be commented out.
            if (INTS[encoded.charAt(i)] < 0)
                sepCount++;

        // Check so that legal chars (including '=') are evenly divideable by 4 as specified in RFC 2045.
        if ((sourceLen - sepCount) % 4 != 0)
            return null;

        // Count '=' at end
        int pad = 0;
        for (int i = sourceLen; i > 1 && INTS[encoded.charAt(--i)] <= 0;)
            if (encoded.charAt(i) == '=')
                pad++;

        int len = ((sourceLen - sepCount) * 6 >> 3) - pad;

        byte[] result = new byte[len];       // Preallocate byte[] of exact length

        for (int s = 0, d = 0; d < len;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = 0;
            for (int j = 0; j < 4; j++) {   // j only increased if a valid char was found.
                int c = INTS[encoded.charAt(s++)];
                if (c >= 0)
                    i |= c << (18 - j * 6);
                else
                    j--;
            }
            // Add the bytes
            result[d++] = (byte) (i >> 16);
            if (d < len) {
                result[d++] = (byte) (i >> 8);
                if (d < len)
                    result[d++] = (byte) i;
            }
        }
        return result;
    }

    /**
     * Decodes a BASE64 encoded string that is known to be reasonably well formatted. The method is about twice as
     * fast as {@link #decode(String)}. The preconditions are:<br>
     * + The array must have a line length of 76 chars OR no line separators at all (one line).<br>
     * + Line separator must be "\r\n", as specified in RFC 2045
     * + The array must not contain illegal characters within the encoded string<br>
     * + The array CAN have illegal characters at the beginning and end, those will be dealt with appropriately.<br>
     *
     * @param source The source string. Length 0 will return an empty array. <tt>null</tt> will throw an exception.
     * @return The decoded array of bytes. May be of length 0.
     */
    @Api public static byte[] decodeFast(String source) {
        // Check special case
        int sourceLen = source.length();
        if (sourceLen == 0)
            return new byte[0];

        int startIx = 0, endIx = sourceLen - 1;    // Start and end index after trimming.

        // Trim illegal chars from start
        while (startIx < endIx && INTS[source.charAt(startIx) & 0xFF] < 0)
            startIx++;

        // Trim illegal chars from end
        while (endIx > 0 && INTS[source.charAt(endIx) & 0xFF] < 0)
            endIx--;

        // get the padding count (=) (0, 1 or 2)
        int pad = source.charAt(endIx) == '=' ? (source.charAt(endIx - 1) == '=' ? 2 : 1) : 0;  // Count '=' at end.
        int contentCount = endIx - startIx + 1;   // Content count including possible separators
        int sepCount = sourceLen > 76 ? (source.charAt(76) == '\r' ? contentCount / 78 : 0) << 1 : 0;

        int len = ((contentCount - sepCount) * 6 >> 3) - pad; // The number of decoded bytes
        byte[] result = new byte[len];       // Preallocate byte[] of exact length

        // Decode all but the last 0 - 2 bytes.
        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = INTS[source.charAt(startIx++)] << 18 | INTS[source.charAt(startIx++)] << 12 | INTS[source.charAt(startIx++)] << 6 | INTS[source.charAt(startIx++)];

            // Add the bytes
            result[d++] = (byte) (i >> 16);
            result[d++] = (byte) (i >> 8);
            result[d++] = (byte) i;

            // If line separator, jump over it.
            if (sepCount > 0 && ++cc == 19) {
                startIx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            // Decode last 1-3 bytes (incl '=') into 1-3 bytes
            int i = 0;
            for (int j = 0; startIx <= endIx - pad; j++)
                i |= INTS[source.charAt(startIx++)] << (18 - j * 6);

            for (int r = 16; d < len; r -= 8)
                result[d++] = (byte) (i >> r);
        }

        return result;
    }
}
