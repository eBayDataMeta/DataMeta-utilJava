package org.ebay.datameta.util.jdk;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.BUILD;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.MAJOR;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.MINOR;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.NONE;
import static org.ebay.datameta.util.jdk.SemanticVersion.DiffLevel.UPDATE;
import static java.util.stream.Collectors.joining;

/**
 * Extended encapsulation of the <a href="http://semver.org" target=_blank>Semantic Version</a> as
 * <tt>Major</tt>.<tt>Minor</tt>.<tt>Update</tt>[.<tt>build</tt>], with the <tt>build</tt> item optional.
 *
 * After transitioning to Java 9, consider <tt><a href="https://docs.oracle.com/javase/9/docs/api/java/lang/Runtime.Version.html" target=_blank>java.lang.Runtime.Version</a></tt>
 * @author Michael Bergens
 */
public class SemanticVersion implements Comparable<SemanticVersion> {

    public enum DiffLevel {
        /** The versions are equal */
        NONE,
        MAJOR,
        MINOR,
        UPDATE,
        BUILD
    }
    /**
     * The Split By the Dots pattern.
     */
    private static final Pattern DOTS_SPLIT = Pattern.compile("\\.");

    /**
     * All digits match pattern - to identify the parts of the Semantic Versioning.
     */
    private static final Pattern DIGITS = Pattern.compile("^[0-9]+$");

    /**
     * Major Version index in the {@link #items} list.
     */
    private static final int MAJOR_INDEX = 0;
    /**
     * Minor Version index in the {@link #items} list.
     */
    private static final int MINOR_INDEX = MAJOR_INDEX + 1;
    /**
     * Update Version index in the {@link #items} list.
     */
    private static final int UPDATE_INDEX = MINOR_INDEX + 1;
    /**
     * Build Version index in the {@link #items} list.
     */
    private static final int BUILD_INDEX = UPDATE_INDEX + 1;

    private static final int ITEMS_MIN_SIZE = UPDATE_INDEX + 1;
    private static final int ITEMS_MAX_SIZE = BUILD_INDEX + 1;

    /**
     * Per the {@link Comparable} contract, return value for "this is greater than the other" condition.
     */
    private static final int GT = 1;

    /**
     * Per the {@link Comparable} contract, return value for "this is equal to the other" condition.
     */
    private static final int EQ = 0;

    /**
     * Per the {@link Comparable} contract, return value for "this is lesser than the other" condition.
     */
    private static final int LT = -1;

    /**
     * The source for the version.
     */
    private final String source;

    /**
     * Semantic Versioning items, starting from Major. There <b>must</b> be at least 3 of those, at most 4.
     */
    private final List<Long> items;

    private final String semanticPartsOnly;

    /**
     * The method of obtaining an instance of the class: the constructor is made private to make it clear that the
     * instance is created by parsing the provided string. Other factory methods may be added if needed along with
     * matching constructor(s).
     */
    public static SemanticVersion parse(final String src) {
        return new SemanticVersion(src);
    }

    /**
     * The constructor that parses a string into the semantic version parts, exposed via the factory method {@link #parse(String)}.
     */
    private SemanticVersion(final String src) {
        this.source = src;
        items = new ArrayList<>(ITEMS_MAX_SIZE);
        for(final String item: DOTS_SPLIT.split(source)) {
            final Matcher m = DIGITS.matcher(item);

            if(m.matches()) items.add(Long.valueOf(item));
            else break; /* break at the first non-numeric item.
            the rest is simply discarded because it's for human eyes only. Checking the alphanumeric metadata
            for which symbols are used would have a performance impact and is pointless for the cause.
            */


        }
        if(items.size() < ITEMS_MIN_SIZE || items.size() > ITEMS_MAX_SIZE) throw new IllegalArgumentException(
            "Invalid semantic version format: \"" + src + "\"");

        if(getBuild() != null && getBuild() == 0) throw new IllegalArgumentException(
            "Invalid semantic version format: \"" + src + "\": build version can not be zero.");

        semanticPartsOnly = items.stream().map(Object::toString).collect(joining("."));
    }

    public String getSource() { return source; }

    /**
     * Unbox: major version <b>must</b> be present.
     */
    public long getMajor() { return items.get(MAJOR_INDEX);}
    /**
     * Unbox: minor version <b>must</b> be present.
     */
    public long getMinor() { return items.get(MINOR_INDEX);}
    /**
     * Unbox: update (a.k.a. "patch") <b>must</b> be present.
     */
    public long getUpdate() { return items.get(UPDATE_INDEX);}

    /**
     * The Build part is optional - return <tt>null</tt> if none.
     */
    public Long getBuild() { return items.size() > BUILD_INDEX ? items.get(BUILD_INDEX) : null; }

    public DiffLevel getDiffLevel(final SemanticVersion other) {
        if(getMajor() != other.getMajor()) return MAJOR;
        if(getMinor() != other.getMinor()) return MINOR;
        if(getUpdate() != other.getUpdate()) return UPDATE;
        if (
                (
                    getBuild() != null && other.getBuild() != null && getBuild().compareTo(other.getBuild()) != EQ
                )
                ||
                (
                    getBuild() == null && other.getBuild() != null
                )
                ||
                (
                    getBuild() != null && other.getBuild() == null
                )
            ) return BUILD;
       return NONE;
    }
    /**
     * Returns semantic parts only delimited by dots, discarding any trailing strings.
     */
    public String getSemanticPartsOnly() { return semanticPartsOnly; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticVersion)) return false;

        SemanticVersion that = (SemanticVersion) o;

        return items.equals(that.items);
    }

    @Override public int hashCode() { return items.hashCode(); }

    @Override public int compareTo(@SuppressWarnings("NullableProblems") /* See the next comment for the why */ SemanticVersion o) {

        /* IntelliJ Idea thinks that the parameter is contracted as non-nullable. Which is not true: it's only
           a recommendation in Comparable's JavaDoc, but there is no annotation in the JDK denoting the parameter as non-nullable.
           Let Idea stand corrected on that:
        */
        //noinspection ConstantConditions
        if(o == null) throw new NullPointerException("Attempt to compare " + getClass().getSimpleName()
            + " \"" + toString() + "\" to a null");

        for(int i = 0; i < ITEMS_MIN_SIZE; i++) {
            final int cmp = items.get(i).compareTo(o.items.get(i));
            if(cmp != EQ) return cmp; // not equal: end of the story, that's the comparison result
        }
        if(items.size() == o.items.size() && items.size() == ITEMS_MIN_SIZE) return EQ; // size same and no build info, all equals
        // if we are here, the Minor, Major and the Update are equal. See what's up with the build if any:
        if(items.size() > o.items.size()) return GT; /* this object is newer (version bigger) because it has a build number but the other does not */
        if(items.size() < o.items.size()) return LT; /* this object is older (version lesser) because it does not have a build number but the other does */
        return items.get(BUILD_INDEX).compareTo(o.items.get(BUILD_INDEX));
    }

    @Override public String toString() { return source; }

    /**
     * For detailed logging and debugging.
     */
    public String toLongString() {
        return getClass().getSimpleName() + '{' + source + '(' + semanticPartsOnly + ")}";
    }
}
