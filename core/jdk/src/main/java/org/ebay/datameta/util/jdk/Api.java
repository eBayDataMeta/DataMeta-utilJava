package org.ebay.datameta.util.jdk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * External facing API, can be used to mark the methods for IDE inspections; should be also documented in details.
 * @author Michael Bergens
 */
@Retention(RetentionPolicy.RUNTIME) public @interface Api {}
