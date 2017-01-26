package org.ebay.datameta.util.jdk;

/**
 * The Integer utilities, covering:
 * <ul>
 * <li>Int overflow-safe operations - those are not efficient but working, there seems to be
 * no better implementation out there.</li>
 * </ul>
 * <p/>
 * References:
 * <ul>
 * <li><a href="https://www.securecoding.cert.org/confluence/display/java/NUM00-J.+Detect+or+prevent+integer+overflow">Source for the safe ops</a></li>
 * </ul>
 *
 * TODO - remove, switch to using <a href="https://google.github.io/guava/releases/19.0/api/docs/com/google/common/math/LongMath.html">Guava Library for this purpose</a>
 *
 * @author Michael Bergens
 */
public class Ints {
    /**
     * Safe add with no overflow guarantee.
     * @return the sum of left and right.
     * @throws ArithmeticException in case of overflow
     */
    public static int safeAdd(int left, int right) throws ArithmeticException {
        if (right > 0 ? left > Integer.MAX_VALUE - right : left < Integer.MIN_VALUE - right) {
            throw new ArithmeticException("Integer overflow: " + left + " + " + right);
        }
        return left + right;
    }

    /**
     * Safe subtract with no overflow guarantee.
     * @return the difference of right from the left, result of subtraction of left from the right.
     * @throws ArithmeticException in case of overflow
     */
    public static int safeSubtract(int left, int right) throws ArithmeticException {
        if (right > 0 ? left < Integer.MIN_VALUE + right : left > Integer.MAX_VALUE + right) {
            throw new ArithmeticException("Integer overflow: " + left + " - " + right);
        }
        return left - right;
    }

    /**
     * Safe multiply with no overflow guarantee.
     * @return the product of left and right.
     * @throws ArithmeticException in case of overflow
     */
    public static int safeMultiply(int left, int right) throws ArithmeticException {
        if (right > 0 ? left > Integer.MAX_VALUE / right
            || left < Integer.MIN_VALUE / right : (
            right < -1 ? left > Integer.MIN_VALUE / right || left < Integer.MAX_VALUE / right :
                right == -1 && left == Integer.MIN_VALUE)
            ) {
            throw new ArithmeticException("Integer overflow: " + left + " * " + right);
        }
        return left * right;
    }

    /**
     * Safe integer division with no overflow guarantee.
     * @return the quotient, result of integer division of rigth by the left.
     * @throws ArithmeticException in case of overflow
     */
    public static int safeDivide(int left, int right) throws ArithmeticException {
        if ((left == Integer.MIN_VALUE) && (right == -1)) {
            throw new ArithmeticException("Integer overflow: " + left + " / " + right);
        }
        return left / right;
    }

    /**
     * Safe negate with no overflow guarantee; based on the fact that {@link Integer#MIN_VALUE}'s absolute
     * value equals {@link Integer#MAX_VALUE} plus one; if you try to negate {@link Integer#MIN_VALUE} in Java,
     * the result will be the original value of {@link Integer#MIN_VALUE}.
     * Means, <tt>-Integer.MIN_VALUE == Integer.MIN_VALUE</tt> which is wrong; this method would throw an exception instead.
     * @return the result of negation of the argument,  <tt>-a</tt>.
     * @throws ArithmeticException in case of overflow.
     */
    public static int safeNegate(int a) throws ArithmeticException {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow: -" + a);
        }
        return -a;
    }

    /**
     * See {@link #safeNegate(int)}, same applies here.
     * @throws ArithmeticException in case of overflow.
     */
    public static int safeAbs(int a) throws ArithmeticException {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow: |" + a + '|');
        }
        return Math.abs(a);
    }

    /**
     * Verifies that the long value fits in the int range.
     * @return the argument unchanged.
     * @throws ArithmeticException if the argument falls outside of the range
     * {@link Integer#MIN_VALUE}<tt>..</tt>{@link Integer#MAX_VALUE} inclusively
     */
    public static long intRangeCheck(long value) throws ArithmeticException {
        if ((value < Integer.MIN_VALUE) || (value > Integer.MAX_VALUE)) {
            throw new ArithmeticException("Integer overflow, (int)" + value);
        }
        return value;
    }
}
