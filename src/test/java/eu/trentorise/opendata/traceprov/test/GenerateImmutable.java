package eu.trentorise.opendata.traceprov.test;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/**
 * NOTICE THE FOLLOWING IS JUST WISHFUL THINKING, IT IS NOT POSSIBLE IN JAVA TO
 * 'PACK' ANNOTATIONS INTO ONE. I KEEP IT HERE ONLY FOR FUTURE REFERENCE, FOR
 * NOW WE WILL JUST 'COPY AND PASTE' MANUALLY THE ANNOTATIONS ON EACH INTERFACE.
 *
 * @deprecated
 * 
 * Annotated interface or abstract class (let's suppose it's named MyClass) will
 * be used as template for generating a corresponding immutable class named
 * ImmutableMyClass, along with a builder to create such immutable objects.
 *
 * This annotation will configure
 * <a href="http://immutables.github.io/">Immutables</a> to expect the annotated
 * class to have bean style getters. The generated builder will have bean style
 * setters. Also, generated immutable objects will all have an empty object
 * retrievable with a method of the form ImmutableMyClass.of()
 * 
 * @author David Leoni
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Value.Immutable(singleton = true)
@Value.Style(get = {"is*", "get*"}, init = "set*")
public @interface GenerateImmutable {
}
