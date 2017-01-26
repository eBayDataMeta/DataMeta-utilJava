package org.ebay.datameta.util.jdk;

/**
 * Generic {@link Throwable} handler - provides flexibility of handling throwables, whether you need to log them
 * or what not.
 * @param <T> concrete subclass of a {@link Throwable}, allows you specify which exception namely you need to handle.
 * @author Michael Bergens
 */
public interface CatchHandler<T extends Throwable> {
    /**
     * Implement handling the throwable. Like logging it.
     */
    void handle(T t);
}
