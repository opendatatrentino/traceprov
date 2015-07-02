/*
 * Copyright 2015 Trento Rise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.trentorise.opendata.traceprov.schema;

import com.google.common.base.Joiner;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.OdtUtils.checkNotEmpty;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hold urls related to dcat.
 *
 * @author David Leoni
 */
public final class ProvRefs {

    private static final Logger LOG = Logger.getLogger(ProvRefs.class.getName());

    public static final String DUBLIC_CORE_TERMS_TITLE = "http://purl.org/dc/terms/title";

    public static final String DUBLIC_CORE_TERMS_SPATIAL = "http://purl.org/dc/terms/spatial";

    public static final String DUBLIC_CORE_TERMS_TEMPORAL = "http://purl.org/dc/terms/temporal";

    public static final String DUBLIC_CORE_TERMS_PUBLISHER = "http://purl.org/dc/terms/publisher";

    public static final String DUBLIC_CORE_TERMS_LICENSE = "http://purl.org/dc/terms/license";

    /**
     * Returns the json path for cells in a table without headers.
     *
     * @param rowIndex the row index, starting from 0. To select all rows, use
     * -1
     * @param columnIndex the column index, starting from 0. To select all
     * columns, use -1
     */
    public static String tablePath(int rowIndex, int colIndex) {
        checkArgument(rowIndex >= -1, "row index must be >= -1, found instead %s", rowIndex);
        checkArgument(colIndex >= -1, "col index must be >= -1, found instead %s", colIndex);
        if (rowIndex == -1 && colIndex == -1) {
            return "*";
        } else {
            return (rowIndex == -1 ? "*" : rowIndex) + "." + (colIndex == -1 ? "*" : colIndex);
        }
    }

    /**
     * Returns the json path for cells in a table with headers.
     *
     * @param rowIndex the row index, starting from 0. To select all rows, use
     * -1
     * @param header. To select all headers, use *
     */
    public static String tablePath(int rowIndex, String header) {
        checkArgument(rowIndex >= -1, "row index must be >= -1, found instead %s", rowIndex);
        checkNotEmpty(header, "Invalid header! To select all headers use *", "");
        if (rowIndex == -1 && header.equals("*")) {
            return "*";
        } else {
            return (rowIndex == -1 ? "*" : rowIndex) + "." + header;
        }
    }

    /**
     * Stupid Java
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
        throw new RuntimeException("Couldn't find generic type argument in method " + method);
    }

    /**
     * Returns the json path for a field and subfields starting from a given
     * {@code rootClass}. Wildcards for multiplicities are automatically
     * inserted in the result.
     *
     * @param propertyPath a path of property names like dataset, themes, uri
     * @return a json path with appropriate wildcards for cardinalities, i.e. "dataset.themes[*].uri"
     */
    public static String propertyRef(Class rootClass, String... propertyPath) {

        checkNotNull(rootClass);
        StringBuilder ret = new StringBuilder();
        Class curClass = rootClass;

        for (int i = 0; i < propertyPath.length; i++) {
            String property = propertyPath[i];
            try {
                BeanInfo info = Introspector.getBeanInfo(curClass);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();

                boolean foundProperty = false;
                for (PropertyDescriptor pd : pds) {

                    if (pd.getName().equals(property)) {
                        foundProperty = true;
                        Class candidateClass = pd.getReadMethod().getReturnType();
                        if (i > 0) {
                            ret.append(".");

                        }
                        ret.append(property);
                        if (Collection.class.isAssignableFrom(candidateClass)) {
                            ret.append("[*]");
                            try {
                                curClass = getCollectionType(pd.getReadMethod());
                                break;
                            }
                            catch (Exception ex) {
                                
                                for (int j = i + 1; j < propertyPath.length; j++) {
                                    ret.append(".");
                                    ret.append(propertyPath[j]);
                                }                                
                                LOG.log(Level.WARNING, "Error while parsing method types, accepting path as it is " + ret.toString(), ex); 
                                return ret.toString();
                            }
                        }
                        curClass = candidateClass;
                        break;
                    }
                }
                if (!foundProperty) {
                    throw new IllegalArgumentException("Couldn't find property " + property + " in property path " + Arrays.toString(propertyPath));
                }
            }
            catch (IntrospectionException ex) {
                throw new RuntimeException("Couldn't read bean properties from class " + curClass, ex);
            }

        }
        return ret.toString();
    }

    /**
     * Returns the json path for a field in a
     * {@link eu.trentorise.opendata.traceprov.data.DcatMetadata} metadata
     * object.
     *
     * @param propertyPath a path of propertY names like
     * "catalog","publisher","name"
     */
    public static String dcatRef(String... propertyPath) {
        Class curClass = DcatMetadata.class;
        for (String property : propertyPath) {
            try {
                BeanInfo info = Introspector.getBeanInfo(curClass);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();

                boolean foundProperty = false;
                for (PropertyDescriptor pd : pds) {

                    if (pd.getName().equals(property)) {
                        foundProperty = true;
                        curClass = pd.getReadMethod().getReturnType();
                        break;
                    }
                }
                if (!foundProperty) {
                    throw new IllegalArgumentException("Couldn't find property " + property + " in property path " + Arrays.toString(propertyPath));
                }
            }
            catch (IntrospectionException ex) {
                throw new RuntimeException("Couldn't read bean properties from class " + curClass, ex);
            }

        }
        return Joiner.on(".").join(propertyPath);
    }

}
