package org.ebay.datameta.util.jdk;

import javax.annotation.concurrent.NotThreadSafe;

import static org.ebay.datameta.util.jdk.MathUtil.MERSENNE_13;
import static org.ebay.datameta.util.jdk.MathUtil.MERSENNE_5;

/**
 * Two ints tuple with getting a copy, and comparison; designed to be used with composition/delegation but not
 * subclassing, with specifics which component means what in the context.
 * In comparison, compares {@link #number} first.
 *
 * <p>Not thread safe because the members are not final.</p>
 *
 * @author Michael Bergens
 */
@NotThreadSafe public class IntAndString implements Copyable<IntAndString>, Comparable<IntAndString> {
    /**
     * Public because the delegation hosts will be deeply involved with this field.
     * Compared first in comparison.
     */
    public int number;
    /**
     * Public because the delegation hosts will be deeply involved with this field.
     * Compared last in comparison.
     */
    public String text;

    public IntAndString() {}

    public IntAndString(final int number, final String text) {
        this.number = number;
        this.text = text;
    }

    /**
     * Gets brand new deep copy.
     */
    @Override public IntAndString getCopyOf() {
        return new IntAndString(number, text);
    }

    /**
     * Considers null a zero infinity.
     */
    @Override public int compareTo(IntAndString that) {
        if(this == that) return 0;
        if(that == null) return 1;

        final int highCompareResult = this.number < that.number ?
             -1 : (this.number > that.number ? 1 : 0);

        return highCompareResult == 0 ? (
            this.text == null ? (
                that.text == null ? 0 : -1
            ) : ( that.text == null ? // unfortunately, java.lang.String.compareTo does not handle null argument, NPE!
                   1 : this.text.compareTo(that.text)
                )
        ): highCompareResult;
    }

    /* if the other turns out a different class,
     -  that's a programming error which will and should result in ClassCastException */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") @Override public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null) return false;
        final IntAndString that = (IntAndString) other;

        //noinspection StringEquality
        return this.number == that.number /* which takes care of both null case */ && (
            this.text == that.text || (this.text != null && this.text.equals(that.text))
            );
    }

    @Override public int hashCode() {
        /* both constants are Mersenne primes */
        return (text == null ? MERSENNE_13 : text.hashCode() *  MERSENNE_5) ^ number;
    }

    @Override public String toString() {
        return "{" + number + ",\"" + text + "\"}";
    }

}
