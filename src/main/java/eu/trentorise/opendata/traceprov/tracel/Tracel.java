package eu.trentorise.opendata.traceprov.tracel;

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Iterables;

import eu.trentorise.opendata.traceprov.tracel.APropertyPath;

/**
 * TraceProv Expression Language
 *
 */
public final class Tracel {

    private static final Logger LOG = Logger.getLogger(Tracel.class.getSimpleName());

    private Tracel() {
    }

    public static boolean isValidId(String id) {
        checkNotNull(id);
        return !id.isEmpty() && !id.contains(" ") && !Character.isDigit(id.charAt(0));
    }

    public static void checkValidId(String id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid identifier! Found " + id);
        }

    }

    /**
     * 
     * Checks provided path can start from given {@code rootClass}. 
     * 
     * @param propertyPath
     *            a path of property names like dataset, themes, uri,
     *            "my Absurdly Named Property". Root field of the path must be among the fields or {@code rootClass}
     * 
     * @throws IllegalArgumentException
     *             if {@code propertyPath} does not correspond to actual fields
     *             in the java classes.
     * 
     */
    public static PropertyPath checkPathFromClass(Class rootClass, PropertyPath propertyPath) {

        checkNotNull(rootClass);

        Class curClass = rootClass;        
        
        Iterator<String> iter = propertyPath.labels()
                                              .iterator();

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

                        if (Collection.class.isAssignableFrom(candidateClass)) {
                            try {
                                curClass = getCollectionType(pd.getReadMethod());
                                break;
                            } catch (Exception ex) {
                               
                                LOG.log(Level.WARNING,
                                        "Error while parsing method types, accepting path as it is " + propertyPath,
                                        ex);
                                return propertyPath;
                            }
                        }
                        curClass = candidateClass;
                        break;
                    }
                }
                if (!foundProperty) {
                    throw new IllegalArgumentException(
                            "Couldn't find property " + property + " in property path " + propertyPath);
                }
            } catch (IntrospectionException ex) {
                throw new RuntimeException("Couldn't read bean properties from class " + curClass, ex);
            }
            i++;
        }
        return propertyPath;
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
