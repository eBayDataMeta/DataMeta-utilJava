package org.ebay.datameta.util.jdk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Used to annotate package-info.java files for one thing so they can distributed to dependents.
 * @author Michael Bergens
 */
@Retention(RetentionPolicy.RUNTIME) public @interface KeepForRuntime {}
