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

public final class TraceQueries {

    private static final Logger LOG = Logger.getLogger(TraceQueries.class.getName());

    public static final Id ROOT = Id.of("T");
    
    public static final PropertyPath ROOT_EXPR = PropertyPath.builder().setRoot(Id.of("T")).build();

    private TraceQueries() {
    }

    public static PropertyPath dataNodesPath(Iterable<Long> nodeIds) {
        checkNotEmpty(nodeIds, "Invalid node ids!");

        PropertyPath.Builder retb = PropertyPath.builder();
        retb.setRoot(ROOT);               

        retb.addProperties("TODO_DATA_NODES");
        for (Long nodeId : nodeIds) {            
            retb.addProperties(Long.toString(nodeId));
        }
        return retb.build();
    }



   

    public static boolean isQuery(Expr expr) {
        if (expr instanceof PropertyPath) {
            PropertyPath pp = (PropertyPath) expr;
            return ROOT.equals(pp.getRoot());
        }
        throw new UnsupportedOperationException("Provided expression is not a supported kind of TraceQuery!");
    }

    public static void checkQuery(Expr expr) {
        if (!isQuery(expr)) {
            throw new IllegalArgumentException("Provided expression " + expr + " is not a Trace Query! "
                    + " (i.e. Does it begin with   " + ROOT.getLabel() + "  ?");
        }
    }

}
