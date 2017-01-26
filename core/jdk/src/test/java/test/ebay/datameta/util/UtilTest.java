package test.ebay.datameta.util.jdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.ebay.datameta.util.jdk.IntAndString;
import org.ebay.datameta.util.jdk.MathUtil;
import org.ebay.datameta.util.jdk.TwoInts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilTest {
    private static Logger L = LoggerFactory.getLogger(UtilTest.class);

    @Before public void init() throws Exception {}

    @After public void destroy() throws Exception {}

    private void assertEqs(TwoInts i1, TwoInts i2) {
        L.debug("Asserting {} == {}",  i1, i2);
        assertEquals("Two TwoInts eq: " + i1 + ", " + i2, i1, i2);
    }

    private void assertYes(TwoInts i1, TwoInts i2, boolean value) {
        assertTrue("Two TwoInts comparison: " + i1 + ", " + i2, value);
    }

    private void assertNo(TwoInts i1, TwoInts i2, boolean value) {
        assertFalse("Two TwoInts not eq: " + i1 + ", " + i2, value);
    }

    private void assertLess(TwoInts i1, TwoInts i2) {
        assertTrue("" + i1 + "<" + i2, i1.compareTo(i2) < 0);
    }
    private void assertGreater(TwoInts i1, TwoInts i2) {
        assertTrue("" + i1 + ">" + i2, i1.compareTo(i2) > 0);
    }

    private void assertCompareEq(TwoInts i1, TwoInts i2) {
        assertTrue("" + i1 + "==" + i2, i1.compareTo(i2) == 0);
    }

    private void assertEqs(IntAndString i1, IntAndString i2) {
        assertEquals("IntAndString-a-String eq: " + i1 + ", " + i2, i1, i2);
    }

    private void assertYes(IntAndString i1, IntAndString i2, boolean value) {
        assertTrue("IntAndString-a-String comparison: " + i1 + ", " + i2, value);
    }

    private void assertNo(IntAndString i1, IntAndString i2, boolean value) {
        assertFalse("IntAndString-a-String not eq: " + i1 + ", " + i2, value);
    }

    private void assertCompareEq(IntAndString i1, IntAndString i2) {
        assertTrue("" + i1 + "==" + i2, i1.compareTo(i2) == 0);
    }
    private void assertLess(IntAndString i1, IntAndString i2) {
        assertTrue("" + i1 + "<" + i2, i1.compareTo(i2) < 0);
    }
    private void assertGreater(IntAndString i1, IntAndString i2) {
        assertTrue("" + i1 + ">" + i2, i1.compareTo(i2) > 0);
    }

    @Test public void testTwoInts() throws Exception {
        L.info("Two TwoInts");
        TwoInts i11 = new TwoInts(1, 1);
        TwoInts i01 = new TwoInts(0, 1);
        TwoInts i12 = new TwoInts(1, 2);
        TwoInts i21 = new TwoInts(2, 1);
        TwoInts j11 = new TwoInts(1, 1);
        assertEqs(i11, j11);
        assertYes(i11, j11, i11.equals(j11));
        assertCompareEq(i11, j11);
        assertNo(i11, i12, i11.equals(i12));
        assertLess(i01, i11);
        assertLess(i11, i12);
        assertLess(i11, i21);
        assertGreater(i11, i01);
        assertGreater(i12, i11);
        assertGreater(i21, i11);
    }

    @Test public void testIntAndString() throws Exception {
        L.info("Int-and-String");
        IntAndString i1a = new IntAndString(1, "a");
        IntAndString i0a = new IntAndString(0, "a");
        IntAndString i1b = new IntAndString(1, "b");
        IntAndString i2a = new IntAndString(2, "a");
        IntAndString j1a = new IntAndString(1, "a");
        IntAndString i1n = new IntAndString(1, null);
        IntAndString i0n = new IntAndString(0, null);
        IntAndString i2n = new IntAndString(2, null);
        IntAndString j1n = new IntAndString(1, null);
        assertEqs(i1a, j1a);
        assertYes(i1a, j1a, i1a.equals(j1a));
        assertCompareEq(i1a, j1a);
        assertNo(i1a, i1b, i1a.equals(i1b));
        assertLess(i0a, i1a);
        assertLess(i1a, i1b);
        assertLess(i1a, i2a);
        assertGreater(i1a, i0a);
        assertGreater(i1b, i1a);
        assertGreater(i2a, i1a);
        assertEqs(i1n, j1n);
        assertYes(i1n, j1n, i1n.equals(j1n));
        assertCompareEq(i1n, j1n);
        assertNo(i1n, i1a, i1n.equals(i1a));
        assertNo(i1a, i1n, i1a.equals(i1n));
        assertLess(i0n, i1n);
        assertLess(i0n, i2n);
        assertGreater(i1n, i0n);
        assertGreater(i2n, i0n);
        assertLess(i1n, i1a);
        assertGreater(i1a, i1n);
    }

    private void runFnv(final String source) {
        L.info("from \"" + source
            + "\": FNV 32 mul: " + Long.toHexString(MathUtil.hashSmear((int)MathUtil.fnvHashMul32(source)))
            + ", FNV 32 XOR: " + Long.toHexString(MathUtil.hashSmear((int)MathUtil.fnvHashXor32(source)))
            + ", FNV 64 mul: " + Long.toHexString(MathUtil.hashSmear((int)MathUtil.fnvHashMul64(source)))
            + ", FNV 64 XOR: " + Long.toHexString(MathUtil.hashSmear((int)MathUtil.fnvHashXor64(source)))
            + ", JDK String hash: " + Long.toHexString(MathUtil.hashSmear(source.hashCode()))
        );
    }
    @Test public void testFnv() {

        runFnv("ab");
        runFnv("ac");
        runFnv("bb");
        runFnv("");
    }
}
