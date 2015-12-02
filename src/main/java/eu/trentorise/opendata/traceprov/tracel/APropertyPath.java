package eu.trentorise.opendata.traceprov.tracel;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.db.TraceDb;

/**
 * A Path made only of {@link AProperty}
 *
 *
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = PropertyPath.class)
@JsonDeserialize(as = PropertyPath.class)
abstract class APropertyPath extends TracePath {

    private static final Logger LOG = Logger.getLogger(APropertyPath.class.getSimpleName());

    public abstract List<Property> getProperties();

    /**
     * The root element. By default returns "$"
     */
    @Value.Default
    public Id getRoot() {
        return Id.of();
    }

    @Override
    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRoot().asString());
        for (Property prop : getProperties()) {
            sb.append(prop.asString());
        }
        return sb.toString();
    }

    /**
     * See {@link #propertyPath(java.lang.Class, java.lang.Iterable) }
     */
    public static PropertyPath of(String rootField, Class rootClass, String... propertyPath) {
        return of(rootField, rootClass, Arrays.asList(propertyPath));
    }

    /**
     * Returns the trace path for a field and subfields starting from a given
     * {@code rootClass}.
     *
     * @param propertyPath
     *            a path of property names like dataset, themes, uri
     * 
     * @throws IllegalArgumentException
     *             if {@code propertyPath} does not correspond to actual fields
     *             in the java classes.
     */
    public static PropertyPath of(String rootField, Class rootClass,  Iterable<String> propertyPath) {

        checkNotNull(rootClass);
        PropertyPath.Builder retb = PropertyPath.builder();
        
        retb.setRoot(Id.of(rootField));
        
        Class curClass = rootClass;

        Iterator<String> iter = propertyPath.iterator();

        int i = 0;
        while (iter.hasNext()) {

            String property = iter.next();
            try {
                BeanInfo info = Introspector.getBeanInfo(curClass);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();

                boolean foundProperty = false;
                for (PropertyDescriptor pd : pds) {

                    if (pd.getName()
                          .equals(property)) {
                        foundProperty = true;
                        Class candidateClass = pd.getReadMethod()
                                                 .getReturnType();

                        retb.addProperties(Property.of(property));
                        if (Collection.class.isAssignableFrom(candidateClass)) {
                            try {
                                curClass = getCollectionType(pd.getReadMethod());
                                break;
                            } catch (Exception ex) {

                                while (iter.hasNext()) {
                                    retb.addProperties(Property.of(iter.next()));
                                }
                                LOG.log(Level.WARNING,
                                        "Error while parsing method types, accepting path as it is " + retb.toString(),
                                        ex);
                                return retb.build();
                            }
                        }
                        curClass = candidateClass;
                        break;
                    }
                }
                if (!foundProperty) {
                    throw new IllegalArgumentException("Couldn't find property " + property + " in property path "
                            + Iterables.toString(propertyPath));
                }
            } catch (IntrospectionException ex) {
                throw new RuntimeException("Couldn't read bean properties from class " + curClass, ex);
            }
            i++;
        }
        return retb.build();
    }

    /**
     * Looks in the return type of provided method and gives back the type of a
     * collection, like String in {@code List<String>}
     *
     * @throws IllegalArgumentException
     *             if method doesn't return generics
     */
    private static Class getCollectionType(Method method) {

        Type[] genericParameterTypes = method.getGenericParameterTypes();

        for (Type genericParameterType : genericParameterTypes) {
            if (genericParameterType instanceof ParameterizedType) {
                ParameterizedType aType = (ParameterizedType) genericParameterType;
                Type[] parameterArgTypes = aType.getActualTypeArguments();
                for (Type parameterArgType : parameterArgTypes) {
                    Class parameterArgClass = (Class) parameterArgType;
                    return parameterArgClass;
                }
            }
        }
        throw new IllegalArgumentException("Couldn't find generic type argument in method " + method);
    }

}
