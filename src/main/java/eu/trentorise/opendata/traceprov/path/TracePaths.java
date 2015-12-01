package eu.trentorise.opendata.traceprov.path;

import static com.google.common.base.Preconditions.checkArgument;
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public final class TracePaths {

    private static final Logger LOG = Logger.getLogger(TracePaths.class.getName());    
    
    private TracePaths() {
    }

    /**
     * See {@link #propertyPath(java.lang.Class, java.lang.Iterable) }
     */
    public static String propertyPath(Class rootClass, String... propertyPath) {
        return propertyPath(rootClass, ImmutableList.copyOf(propertyPath));
    }

    /**
     * Returns the trace path for a field and subfields starting from a given
     * {@code rootClass}. Wildcards for multiplicities are automatically
     * inserted in the result.
     *
     * @param propertyPath
     *            a path of property names like dataset, themes, uri
     * @return a trace path with appropriate wildcards for cardinalities, i.e.
     *         "dataset.themes[*].uri"
     * @throws IllegalArgumentException
     *             if {@code propertyPath} does not correspond to actual fields
     *             in the java classes.
     */
    public static String propertyPath(Class rootClass, Iterable<String> propertyPath) {

        checkNotNull(rootClass);
        StringBuilder ret = new StringBuilder();
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
                        if (i > 0) {
                            ret.append(".");

                        }
                        ret.append(property);
                        if (Collection.class.isAssignableFrom(candidateClass)) {
                            ret.append("[ALL]");
                            try {
                                curClass = getCollectionType(pd.getReadMethod());
                                break;
                            } catch (Exception ex) {

                                while (iter.hasNext()) {
                                    ret.append(".");
                                    ret.append(iter.next());
                                }
                                LOG.log(Level.WARNING,
                                        "Error while parsing method types, accepting path as it is " + ret.toString(),
                                        ex);
                                return ret.toString();
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
        return ret.toString();
    }

    public static String dataNodesPath(Iterable<Long> nodeIds) {
        checkNotEmpty(nodeIds, "Invalid node ids!");

        StringBuilder sb = new StringBuilder();
        sb.append("$[");
        boolean first = true;

        for (Long nodeId : nodeIds) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(nodeId);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns the json path for cells in a table without headers. See
     * {@link eu.trentorise.opendata.traceprov.validation.CsvValidator} for
     * examples of tabular models.
     *
     * @param rowIndex
     *            the row index, starting from 0. To select all rows, use -1
     * @param columnIndex
     *            the column index, starting from 0. To select all columns, use
     *            -1
     */
    public static String tablePath(int rowIndex, int colIndex) {
        checkArgument(rowIndex >= -1, "row index must be >= -1, found instead %s", rowIndex);
        checkArgument(colIndex >= -1, "col index must be >= -1, found instead %s", colIndex);
        return "$[" + (rowIndex == -1 ? "ALL" : rowIndex) + "][" + (colIndex == -1 ? "ALL" : colIndex) + "]";
    }

    /**
     * Returns the json path for cells in a table with headers. See
     * {@link eu.trentorise.opendata.traceprov.services.CsvValidator} for
     * examples of tabular models.
     *
     * @param rowIndex
     *            the row index, starting from 0. To select all rows, use -1
     * @param header.
     *            To select all headers, use ALL
     */
    public static String tablePath(int rowIndex, String header) {
        checkArgument(rowIndex >= -1, "row index must be >= -1, found instead %s", rowIndex);
        checkNotEmpty(header, "Invalid header! To select all headers use ALL", "");
        return "$[" + (rowIndex == -1 ? "ALL" : rowIndex) + "]." + header;
    }

    /**
     * Looks in the return type of provided method and gives back the type of a
     * collection, like String in {@code List<String>}
     *
     * @throws IllegalArgumentException
     *             if method doesn't return generids
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
