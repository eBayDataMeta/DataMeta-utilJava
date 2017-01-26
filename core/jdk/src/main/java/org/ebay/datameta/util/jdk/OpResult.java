package org.ebay.datameta.util.jdk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.ebay.datameta.util.jdk.OpState.ERROR;
import static org.ebay.datameta.util.jdk.OpState.FAIL;
import static org.ebay.datameta.util.jdk.OpState.INIT;
import static org.ebay.datameta.util.jdk.OpState.OK;

/**
 * Result of an operation including the state, the value and information about an error if any.
 * Thread safety, immutability hinges on the {@link RESULT_TYPE} - if it is immutable, then
 * this class is immutable and thread safe.
 * 
 * @author Michael Bergens
 *
 * @param <RESULT_TYPE>
 * @see OpState
 * @see #getInitInstance()
 * @see #getInitInstance(Object)
 * @see #getOkInstance(Object)
 * @see #getFailInstance(Object, String)
 * @see #getErrorInstance(Object, String, Throwable)
 */
@SuppressWarnings("NullableProblems") public class OpResult<RESULT_TYPE> {

    /**
     * Builds the OK instance with an optional value.
     * @param value may be null if it's legit for the case.
     */
    @Api public static <T> OpResult<T> getOkInstance(@Nullable final T value) {
        return new OpResult<T>(OK, value, null, null);
    }
    
    /**
     * Builds the fail instance with optional value and required error message.
     * @param value can be null.
     * @param errorMessage must not be null.
     */
    @Api public static <T> OpResult<T> getFailInstance(@Nullable final T value, @Nonnull final String errorMessage) {
        //noinspection ConstantConditions
        assert errorMessage != null: "Must supply an error message explaining the failure";
        return new OpResult<T>(FAIL, value, errorMessage, null);
    }
    
    /**
     * Builds the fail instance with optional value, optional extra error message and required Throwable.
     * @param value may be present which is unlikely for an error.
     * @param errorMessage an extra error message on top of he required {@link Throwable} if any.
     * @param cause required cause of the error.
     */
    @Api public static <T> OpResult<T> getErrorInstance(@Nullable final T value, @Nullable final String errorMessage
            , @Nonnull final Throwable cause) {
        //noinspection ConstantConditions
        assert cause != null: "Must supply the cause of the error";
        return new OpResult<T>(ERROR, value, errorMessage, cause);
    }
    
    /**
     * Builds the init instance with an optional value.
     * @param value if it is somehow available during init state.
     * @see #getInitInstance()
     */
    @Api public static <T> OpResult<T> getInitInstance(@Nullable final T value) {
        return new OpResult<T>(INIT, value, null, null);
    }

    /**
     * Builds the bare init instance.
     * @see #getInitInstance(Object)
     */
    public static <T> OpResult<T> getInitInstance() {
        return new OpResult<T>(INIT, null, null, null);
    }

    @Nonnull private final OpState state;
    @Nullable private final RESULT_TYPE value;
    @Nullable private final String errorMessage;
    @Nullable private final Throwable failureCause;

    private OpResult(final OpState state, final RESULT_TYPE value, final String errorMessage, final Throwable cause) {
        this.state = state;
        this.value = value;
        this.errorMessage = errorMessage;
        this.failureCause = cause;
    }

    @Api @Nonnull public OpState getState() { return state; }

    @Api @Nullable public RESULT_TYPE getValue() { return value; }

    @Api @Nullable public String getErrorMessage() { return errorMessage; }

    @Api @Nullable public Throwable getFailureCause() { return failureCause; }
}
