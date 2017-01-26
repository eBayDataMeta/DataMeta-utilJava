package test.ebay.datameta.util.jdk;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import static org.ebay.datameta.util.jdk.Ints.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
/**
 * TODO - remove, the class tested here has been deprecated.
 * @author Michael Bergens
 */
public class SafeIntOpsTest {

    public static final Logger L = LoggerFactory.getLogger(SafeIntOpsTest.class);

    @Test public void testSafeAddSuccess() {
        int a = MAX_VALUE - 1, b = 1;
        assertThat(MAX_VALUE, is(safeAdd(a, b)));
        a = MIN_VALUE + 1;
        Marker m = MarkerFactory.getMarker("a");
        L.info(m, "a = {}, b = {}", a, b);
        assertThat(MIN_VALUE, is(safeSubtract(a, b)));
        assertThat(MAX_VALUE, is(safeAdd(MAX_VALUE, 0)));
        assertThat(MAX_VALUE, is(safeSubtract(MAX_VALUE, 0)));
    }

    @Test(expected = ArithmeticException.class) public void testSafeAddFail() {
        safeAdd(MAX_VALUE, 1);
    }

    @Test(expected = ArithmeticException.class) public void testSafeSubFail() {
        safeSubtract(MIN_VALUE, 1);
    }

    @Test(expected = ArithmeticException.class) public void testSafeMulFail() {
        safeMultiply((MAX_VALUE >> 1) + 1, 2);
    }

    @Test(expected = ArithmeticException.class) public void testSafeMulDiv() {
        safeDivide(MIN_VALUE, -1);
    }

    @Test(expected = ArithmeticException.class) public void testIntDownCastFailUp() {
        intRangeCheck((long)MAX_VALUE + 1L);
    }

    @Test(expected = ArithmeticException.class) public void testIntDownCastFailDown() {
        intRangeCheck((long)MIN_VALUE - 1L);
    }

    @Test(expected = ArithmeticException.class) public void testIntDownNegFail() {
        safeNegate(MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class) public void testIntDownAbsFail() {
        safeAbs(MIN_VALUE);
    }
}
