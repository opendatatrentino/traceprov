package eu.trentorise.opendata.traceprov.data;

import javax.annotation.Nullable;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 * TODO Do we really need this interface? Let's just use factory methods in {@link DataNodes} for now.
 * 
 *
 */
public interface DataNodeFactory {

    /**
     * Creates a DataNode tree recursively out of a Java object. Map and
     * Iterable instances will be converted respectively to DataMap and
     * DataArray. The choice of Maps and Collections backing such nodes is left
     * to implementations of this interface.
     */
    TraceNode makeNode(
	    Ref ref, 
	    NodeMetadata metadata, 
	    @Nullable Object obj);

}
