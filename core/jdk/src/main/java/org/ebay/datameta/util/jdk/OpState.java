package org.ebay.datameta.util.jdk;

/**
 * Status of an operation/process, with the condition "DONE" split into {@link #OK}, {@link #FAIL}
 * and {@link #ERROR}, tailored to non-interruptable non-blockable asynchronous processes. For blockable
 * process, can reuse {@link Thread.State}.
 * 
 * @author Michael Bergens
 * @see #INIT
 * @see #RUNNING
 * @see #OK
 * @see #FAIL
 * @see Thread.State
 */
public enum OpState {
    /**
     * The process has not started yet.
     */
    INIT,
    /**
     * The process is running.
     */
    RUNNING,
    /**
     * The process is completed with success.
     */
    OK,
    /**
     * The process is completed with a programmatic failure, extra details
     * should be provided somewhere. This is different from {@link #ERROR} which
     * signifies programmatic error with an instance of {@link Throwable}
     * involved.
     * 
     * @see #ERROR
     */
    FAIL,
    /**
     * The process has been aborted for the reason of an {@link Throwable}.
     * 
     * @see #FAIL
     */
    ERROR,
}
