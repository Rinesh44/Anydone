package com.treeleaf.anydone.serviceprovider.injection.annotations;

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A qualifier that is used to denote global datastore
 */
@Qualifier @Retention(RUNTIME) public @interface Remote {
}
