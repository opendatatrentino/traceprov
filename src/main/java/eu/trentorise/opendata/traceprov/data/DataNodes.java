package eu.trentorise.opendata.traceprov.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.TraceProvs;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

public final class DataNodes {

    private static Logger LOG = Logger.getLogger(DataNodes.class.getSimpleName());
    public static final String DATANODES_IRI = TraceProvs.TRACEPROV_IRI + "/db/datanodes/";
    
    
    private DataNodes() {
    }

    public static Ref makeSubRef(Ref ref, String propertyName) {
	checkNotNull(ref);
	checkNotNull(propertyName);

	if (ref.getTracePath().isEmpty()) {
	    return ref;
	} else {
	    return ref.withTracePath(ref.getTracePath() + "." + propertyName);
	}
    }

    /**
     * 
     * @param index
     *            index starting from zero (included).
     */
    public static Ref makeSubRef(Ref ref, long index) {
	checkNotNull(ref);
	checkArgument(index >= 0);

	if (ref.getTracePath().isEmpty()) {
	    return ref;
	} else {
	    return ref.withTracePath(ref.getTracePath() + "[" + index + "]");
	}
    }

    /**
     * Returns new metadata according to provided object type.
     */
    public static NodeMetadata makeMetadata(
	    NodeMetadata metadata,
	    @Nullable Object obj) {

	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	checkNotNull(metadata);

	if (obj == null) {
	    return metadata.withType(AnyType.of());
	} else {
	    try {
		TraceType canType = typeRegistry.getCanonicalTypeFromInstance(obj);
		return metadata.withType(canType);
	    } catch (TraceProvNotFoundException ex) {
		return metadata.withType(AnyType.of());
	    }
	}
    }

    /**
     * Wraps provided object into a shallow DataNode. Children nodes will be
     * converted to DataNode lazily upon request. If {@code obj} is already a
     * DataNode it is returned as it is.
     */
    public static TraceNode makeNode(
	    Ref ref,
	    NodeMetadata metadata,
	    @Nullable Object obj) {

	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();

	if (obj instanceof TraceNode) {
	    return (TraceNode) obj;
	} else if (obj == null || obj instanceof String || obj instanceof Number) {
	    TraceType canType = typeRegistry.getCanonicalTypeFromInstance(obj);
	    NodeMetadata newMetadata;
	    if (metadata.getType().equals(AnyType.of())) {
		newMetadata = metadata.withType(canType);
	    } else {
		newMetadata = metadata;
	    }
	    return DataValue.of(ref, newMetadata, obj);

	} else if (obj instanceof Map) {
	    Map map = (Map) obj;
	    return DataMap.of(ref, metadata, map);
	} else if (obj instanceof Iterable) {
	    Iterable coll = (Iterable) obj;
	    return DataArray.of(ref, metadata, coll);
	} else {
	    return DataObject.of(ref, metadata, obj);
	}
    }

    /**
     * TODO do we need it?
     */
    public static TraceNode makeDeepNode(
	    Ref ref,
	    NodeMetadata metadata,
	    @Nullable Object obj) {

	return new SimpleDataNodeFactory()
		.makeNode(ref, metadata, obj);
    }

    
    
    /**
     * Creates a shallow DataNode as children of another DataArray. Further
     * subchildren nodes will be converted lazily to DataNode upon request. If
     * {@code obj} is already a DataNode it is returned as it is.
     */
    public static TraceNode makeSubnode(
	    DataArray dataArray,
	    long index,
	    @Nullable Object obj) {

	return DataNodes.makeNode(
		DataNodes.makeSubRef(
			dataArray.getRef(),
			index),
		DataNodes.makeMetadata(
			dataArray.getMetadata(),
			obj),
		obj);
    }

    /**
     * Creates a shallow DataNode as children of another DataNode. Further
     * subchildren nodes will be converted to DataNode upon request. If
     * {@code obj} is already a DataNode it is returned as it is.
     */
    public static TraceNode makeSubnode(
	    TraceNode dataMap,
	    String propertyName,
	    @Nullable Object obj) {

	return DataNodes.makeNode(
		DataNodes.makeSubRef(
			dataMap.getRef(),
			propertyName),
		DataNodes.makeMetadata(
			dataMap.getMetadata(),
			obj),
		obj);
    }


}
