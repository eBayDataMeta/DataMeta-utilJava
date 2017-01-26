package org.ebay.datameta.util.jdk;

import javax.annotation.concurrent.NotThreadSafe;

import static org.ebay.datameta.util.jdk.MathUtil.MERSENNE_7;

/**
 * Two ints tuple with getting a copy, and comparison; designed to be used with composition/delegation but not
 * subclassing, with specifics which int means what in the context.
 *
 * <p>Not thread safe because the members are not final.</p>
 *
 * <p>There are alternatives to using this class:
 * <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/tuple/Pair.html">
 *     apache-commons-lang3's Pair</a> or <a href="http://www.javatuples.org">Java Tuples</a>
 *     that <a href="http://mvnrepository.com/artifact/org.javatuples/javatuples/">are available in public Maven repos</a>
 * </p>
 * @author michaelb Michael Bergens
 */
@NotThreadSafe public class TwoInts implements Copyable<TwoInts>, Comparable<TwoInts> {

    /**
     * Public because the delegation hosts will be deeply involved with this field.
     * Compared first in comparison.
     */
    public int first;
    /**
     * Public because the delegation hosts will be deeply involved with this field.
     * Compared last in comparison.
     */
    public int last;

    @Api public TwoInts() {}

    public TwoInts(final int first, final int last) {
        this.first = first;
        this.last = last;
    }

    @Api protected int getFirst() {
        return first;
    }

    @Api protected int getLast() {
        return last;
    }

    @Api protected void setFirst(int value) {
        first = value;
    }

    @Api protected void setLast(int value) {
        last = value;
    }

    /**
     * Gets brand new deep copy.
     */
    @Override public TwoInts getCopyOf() {
        return new TwoInts(first, last);
    }

    @Override public int compareTo(TwoInts that) {
        if(this == that) return 0;
        if(that == null) return 1;

        final int highCompareResult = this.first < that.first ?
             -1 : (this.first > that.first ? 1 : 0);

        return highCompareResult == 0 ?
            (
                this.last < that.last ?
                    -1 : (this.last > that.last ? 1 : 0)
            ) : highCompareResult;
    }

    /* if the other turns out a different class,
     -  that's a programming error which will and should result in ClassCastException */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") @Override public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null) return false;
        final TwoInts that = (TwoInts) other;
        return this.first == that.first && this.last == that.last;
    }

    @Override public int hashCode() {
        return last * MERSENNE_7 ^ first;
    }

    @Override public String toString() {
        return "{" + first + ',' + last + '}';
    }
}
