package org.ebay.datameta.util.text;

import org.ebay.datameta.util.jdk.Api;

/**
 * MIME constants.
 * @author michaelb Michael Bergens
 * @see #CSV_TYPE
 * @see #JSON_TYPE
 * @see #GIF_TYPE
 * @see #HTML_TYPE
 * @see #JPG_TYPE
 * @see #TEXT_PLAIN_TYPE
 * @see #XML_TYPE
 * @see #APP_X_JAVASCRIPT 
 */
@Api public class MimeUtil {
    @Api public static final String CSV_TYPE = "application/vnd.ms-excel";
    @Api public static final String JSON_TYPE = "application/json";
    @Api public static final String GIF_TYPE = "image/gif";
    @Api public static final String HTML_TYPE = "text/html";
    @Api public static final String JPG_TYPE = "image/jpeg";
    @Api public static final String TEXT_PLAIN_TYPE = "text/plain";
    @Api public static final String XML_TYPE = "text/xml";
    @Api public static final String APP_X_JAVASCRIPT = "application/x-javascript";
}
