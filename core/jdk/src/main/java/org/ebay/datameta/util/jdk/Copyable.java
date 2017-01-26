package org.ebay.datameta.util.jdk;

/**
 * Generified version of {@link Cloneable}, bean-friendly and not throwing any checked exceptions.
 * @author Michael Bergens
 * @param <T> the type to copy.
 */
public interface Copyable<T> {
    /**
     * Gets a copy of this object without throwing any checked exceptions. Each implementor
     * should explain what kind of copy it gets, deep, shallow, how deep/shallow if applicable, what's the catch if any.
     */
    @Api T getCopyOf();
}
