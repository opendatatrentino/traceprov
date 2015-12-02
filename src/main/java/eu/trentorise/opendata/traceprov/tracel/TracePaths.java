package eu.trentorise.opendata.traceprov.tracel;

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
}
