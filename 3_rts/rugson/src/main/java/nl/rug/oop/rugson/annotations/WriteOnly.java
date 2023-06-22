package nl.rug.oop.rugson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a field should not be serialized.
 * This differs from transient, as when a field is marked transient, it is
 * not serialized, but it is also not deserialized. This annotation is used
 * to indicate that a field should not be serialized, but should be
 * deserialized.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteOnly {
}
