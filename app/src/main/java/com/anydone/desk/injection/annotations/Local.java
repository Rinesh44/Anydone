package com.anydone.desk.injection.annotations;

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A qualifier that is used to denote a local datastore
 */
@Qualifier @Retention(RUNTIME) public @interface Local {
}
