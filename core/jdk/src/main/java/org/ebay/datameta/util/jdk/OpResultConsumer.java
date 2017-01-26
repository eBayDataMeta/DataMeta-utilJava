package org.ebay.datameta.util.jdk;

/**
 * Consumer of an operation result.
 * @author Michael Bergens
 *
 * @param <T> the type of the result value of the operation, see {@link OpResult#getValue()}.
 * @see OpState
 * @see OpResult
 */
public interface OpResultConsumer<T> {
   @Api void consume(OpResult<T> result);
}
