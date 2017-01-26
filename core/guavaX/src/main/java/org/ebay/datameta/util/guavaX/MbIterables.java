package org.ebay.datameta.util.guavaX;

import org.ebay.datameta.util.jdk.Api;
import com.google.common.collect.Iterators;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author michaelb Michael Bergens
 */
@Api public class MbIterables {

    @Api public static <T> Iterable<T> forArray(final T[] array) {
        return new Iterable<T>() {
            @Override public Iterator<T> iterator() {
                return Iterators.forArray(array);
            }
        };
    }

    @Api public static <T> Iterable<T> forEnumeration(final Enumeration<T> enumeration) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.forEnumeration(enumeration);
            }
        };
    }
}
