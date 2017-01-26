package org.ebay.datameta.util.guavaX;

import org.ebay.datameta.util.jdk.Api;

/**
 * A variant of {@link com.google.common.base.Function} that lets the {@link com.google.common.base.Function#apply(Object)}
 * method be defined to throw any exception, checked or unchecked.
 * @author michaelb Michael Bergens
 */
@Api public interface Xform<I,O,X extends Exception> {
    @Api O apply(I argument) throws X;
}
