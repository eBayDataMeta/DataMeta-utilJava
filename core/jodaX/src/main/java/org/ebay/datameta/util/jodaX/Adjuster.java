package org.ebay.datameta.util.jodaX;

import org.joda.time.*;

/** Contract for datetime adjusting by a string of specs.
 * @see #adjust(MutableDateTime, String) 
 * @author michaelb Michael Bergens
 */
abstract class Adjuster {

    /**
     * This method should adjust the origin by the spec if it can, if it can not, for example, when the method
     * does not recognize the specs, it should leave the origin alone. In other words, the implementors should
     * be responsible to skip adjustment for the wrong specs.
     */
    public abstract void adjust(final MutableDateTime origin, final String specs);

    /** Some wording to identify the adjuster for logging etc.
     * @return {@link Class#getSimpleName()} of the current class.
     */
    @Override public String toString() {
        return getClass().getSimpleName(); // works well except for anonymous classes.
    }

}
