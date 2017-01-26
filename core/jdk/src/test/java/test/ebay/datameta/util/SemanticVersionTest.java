package test.ebay.datameta.util.jdk;

import org.ebay.datameta.util.jdk.SemanticVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.BUILD;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.MAJOR;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.MINOR;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.NONE;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.UPDATE;
import static org.ebay.datameta.util.jdk.SemanticVersion.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Michael Bergens
 */
public class SemanticVersionTest {

  private static Logger L = LoggerFactory.getLogger(SemanticVersionTest.class);

  @Before public void init() throws Exception {
  }

  @After public void destroy() throws Exception {
  }

  @Test(expected = IllegalArgumentException.class) public void testInitialDot() {
    parse(".2.3");
  }

  @Test(expected = IllegalArgumentException.class) public void testLastDot() {
    parse("1.2.");
  }

  @Test(expected = IllegalArgumentException.class) public void testAlphanumMajor() {
    parse("1a.2.3");
  }

  @Test(expected = IllegalArgumentException.class) public void testAlphanumMinor() {
    parse("1.2a.3");
  }

  @Test(expected = IllegalArgumentException.class) public void testAlphanumUpdate() {
    parse("1.2.a3");
  }
  // there is no test for Alphanum Build because it's covered by testMajMinUpdWithTrail

  @Test(expected = IllegalArgumentException.class) public void testMinusMajor() {
    parse("-1.2.3");
  }

  @Test(expected = IllegalArgumentException.class) public void testMinusMinor() {
    parse("1.-2.3");
  }

  @Test(expected = IllegalArgumentException.class) public void testMinusBuild() {
    parse("1.2.-3");
  }

  @Ignore private String fullVersionInfo(final SemanticVersion v) {
    return String.format("%s; maj=%d, min=%d, upd=%d, bld=%s", v.toLongString(), v.getMajor(), v.getMinor(), v.getUpdate(), v.getBuild());
  }

  /**
   * Helper method.
   */
  @Ignore
  private void assertVersion(final SemanticVersion v, final long maj, final long min, final long upd, final Long bld) {
    assertThat(v.getMajor(), is(maj));
    assertThat(v.getMinor(), is(min));
    assertThat(v.getUpdate(), is(upd));
    assertThat(v.getBuild(), is(bld));
  }

  @Test public void testMajMinUpdBuildNoTrail() {
    final SemanticVersion v = parse("12.345.6.7");
    L.info("Parsed: {}", fullVersionInfo(v));
    assertVersion(v, 12, 345, 6, 7L);
  }

  @Test public void testMajMinUpdBuildWithTrail() {
    final SemanticVersion v = parse("12.345.6.7.blah-blah-yada.yada");
    L.info("Parsed: {}", fullVersionInfo(v));
    assertVersion(v, 12, 345, 6, 7L);
  }

  @Test public void testMajMinUpdNoTrail() {
    final SemanticVersion v = parse("12.345.6");
    L.info("Parsed: {}", fullVersionInfo(v));
    assertVersion(v, 12, 345, 6, null);
  }

  @Test public void testSimpleEq() {
    final SemanticVersion v1 = parse("1.22.33");
    final SemanticVersion v2 = parse("1.22.33");
    L.info("Parsed: {}", fullVersionInfo(v1));
    assertTrue(v1.compareTo(v2) == 0);
  }

  @Test public void testMajMinUpdWithTrail() {
    final SemanticVersion v = parse("12.345.6.blah-blah-yada.yada");
    L.info("Parsed: {}", fullVersionInfo(v));
    assertVersion(v, 12, 345, 6, null);
  }

  @Test public void testCompareMinMajUpd() {
    final SemanticVersion v1 = parse("5.6.7"), v2 = parse("12.15.16");
    // stringwise, v1 > v2
    assert (v1.getSource().compareTo(v2.getSource()) > 0);
    // versionwise, v1 < v2
    assert (v1.compareTo(v2) < 0);
  }

  @Test public void testCompareMinMajBldGtNull() {
    final SemanticVersion v1 = parse("5.6.7.8"), v2 = parse("5.6.7");
    // stringwise, v1 > v2
    assert (v1.getSource().compareTo(v2.getSource()) > 0);
    // versionwise,  also v1 > v2
    assert (v1.compareTo(v2) > 0);
  }

  @Test public void testCompareMinMajBldLtNull() {
    final SemanticVersion v1 = parse("5.6.7"), v2 = parse("5.6.7.8");
    // stringwise, v1 < v2
    assert (v1.getSource().compareTo(v2.getSource()) < 0);
    // versionwise, also v1 < v2
    assert (v1.compareTo(v2) < 0);
  }

  @Test public void testCompareMinMajBldLtInt() {
    final SemanticVersion v1 = parse("5.6.7.3"), v2 = parse("5.6.7.12");
    // stringwise, v1 < v2
    assert (v1.getSource().compareTo(v2.getSource()) > 0);
    // versionwise though, v1 < v2
    assert (v1.compareTo(v2) < 0);
  }

  @Test public void testCompareMinMajBldEq() {
    final SemanticVersion v1 = parse("5.6.7.8"), v2 = parse("5.6.7.8");
    // stringwise, v1 == v2
    assert (v1.getSource().compareTo(v2.getSource()) == 0);
    // versionwise, also v1 == v2
    assert (v1.compareTo(v2) == 0);
  }

  @Test public void testDiffLevels() {
    assertThat(parse("1.22.333").getDiffLevel(parse("1.22.333")), is(NONE));
    assertThat(parse("1.2.3").getDiffLevel(parse("1.2.3.blah")), is(NONE));
    assertThat(parse("1.2.3.4").getDiffLevel(parse("1.2.3.4.blah")), is(NONE));
    assertThat(parse("1.2.3").getDiffLevel(parse("2.2.3.blah")), is(MAJOR));
    assertThat(parse("1.2.3").getDiffLevel(parse("1.4.3.blah")), is(MINOR));
    assertThat(parse("1.2.3").getDiffLevel(parse("1.2.4.blah")), is(UPDATE));
    assertThat(parse("1.2.3.4").getDiffLevel(parse("1.2.3.blah")), is(BUILD));
    assertThat(parse("1.2.3").getDiffLevel(parse("1.2.3.4.blah")), is(BUILD));
    assertThat(parse("1.2.3.4").getDiffLevel(parse("1.2.3.5.blah")), is(BUILD));
  }
}
