package eu.trentorise.opendata.traceprov.data;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.Type;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

public final class DataNodes {

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
    public static Ref makeSubRef(Ref ref, int index) {
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
	    @Nullable Object obj,
	    TypeRegistry typeRegistry) {

	checkNotNull(metadata);
	checkNotNull(typeRegistry);

	if (obj == null) {
	    return metadata.withType(AnyType.of());
	} else {
	    try {
		Type canType = typeRegistry.getCanonicalTypeFromInstance(obj);
		return metadata.withType(canType);
	    } catch (TraceProvNotFoundException ex) {
		return metadata.withType(AnyType.of());
	    }
	}
    }

    public static DataNode makeNode(
	    Ref ref,
	    NodeMetadata metadata,
	    @Nullable Object obj,
	    TypeRegistry typeRegistry) {

	return new SimpleDataNodeFactory().makeNode(ref, metadata, obj, typeRegistry);
    }
}
