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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hold urls related to dcat.
 *
 * @author David Leoni
 */
public final class ProvRefs {

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
     * Returns the json path for a field and subfields starting from a given
     * {@code rootClass}. todo specify better
     *
     * @param propertyPath a path of property names like
     * "catalog","publisher","name"
     */
    public static String propertyRef(Class rootClass, String... propertyPath) {

        checkNotNull(rootClass);

        Class curClass = rootClass;
        for (String property : propertyPath) {
            try {
                BeanInfo info = Introspector.getBeanInfo(curClass);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();

                boolean foundProperty = false;
                for (PropertyDescriptor pd : pds) {

                    if (pd.getName().equals(property)) {
                        foundProperty = true;
                        Class candidateClass = pd.getReadMethod().getReturnType();
                        if (Collection.class.isAssignableFrom(candidateClass)) {
                            TypeVariable[] typeParameters = curClass.getTypeParameters();
                            Type[] bounds = typeParameters[0].getBounds();
                            if (bounds.length == 1) {
                                curClass = (Class) bounds[0];
                                break;
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
        return Joiner.on(".").join(propertyPath);
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
