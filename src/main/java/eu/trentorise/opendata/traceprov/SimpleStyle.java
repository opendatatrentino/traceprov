package eu.trentorise.opendata.traceprov;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/**
 * Annotated abstract class (let's suppose it's named AbstractMyClass) will be
 * used as template for generating a corresponding immutable class named
 * MyClass. Annotated class is supposed to have few fields, thus no builder will
 * be generated. Immutable class will have public visibility.
 *
 * This annotation will configure
 * <a href="http://immutables.github.io/">Immutables</a> to expect the annotated
 * class to have bean style getters. The generated builder will have bean style
 * setters. Also, generated immutable objects will all have an empty object
 * retrievable with a method of the form ImmutableMyClass.of().
 *
 * NOTE: Annotated abstract class name MUST begin with 'Abstract'.
 *
 * @author David Leoni
 * @see SimpleStyle
 */
@Value.Style(get = {"is*", "get*"},
        init = "set*",
        typeAbstract = {"Abstract*"},
        typeImmutable = "",
        defaults = @Value.Immutable(singleton = true,
                builder = false,
                visibility = Value.Immutable.ImplementationVisibility.PUBLIC))
@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface SimpleStyle {
}
