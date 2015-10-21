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
package eu.trentorise.opendata.traceprov.data;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.DataArray.Builder;
import eu.trentorise.opendata.traceprov.db.TraceDb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.google.common.base.Preconditions;

/**
 * A node of the TraceProv tree format representation. Use {@link DataVisitor}
 * and
 * {@link #accept(eu.trentorise.opendata.traceprov.data.IDataVisitor, eu.trentorise.opendata.traceprov.data.Data, java.lang.String, int)
 * accept} to navigate it. Each DataNode contains a raw java object, which may
 * contain references to other DataNodes.
 *
 * @author David Leoni
 */
public abstract class TraceData implements Serializable {

    /**
     * The numerical id of the datanode inside a {@link TraceDb}. If unknown
     * defaults to -1.
     */
    @Value.Default
    public long getId() {
	return -1;
    }

    /**
     * A reference to position in the original file from which this node comes
     * from. If unknown, {@link Ref#of()} is returned.
     */
    @Value.Default
    public Ref getRef() {
	return Ref.of();
    }

    /**
     * Provenance information about the node.
     */
    @Value.Default
    public NodeMetadata getMetadata() {
	return NodeMetadata.of();
    }

    /**
     * The Java object backing this node, which can't be another DataNode
     * (although the object can contain references to other DataNodes).
     * <p>
     * <b>DO NOT MODIFY THE RETURNED OBJECT</b>. If you need to modify the
     * object, use the copy returned by {@link #copyRawValue()} instead.
     * <p>
     */
    @Value.Default
    @Nullable
    public Object getRawValue() {
	return null;
    }

    /**
     * A deep copy of the Java object backing this node (immutable objects won't
     * be copied). If you just need to read the object without modifying it, use
     * {@link #getValue()} instead.
     */
    // todo say it's using current type reg
    public Object copyRawValue() {
	return TraceDb.getCurrentDb()
		.getTypeRegistry()
		.getCanonicalTypeFromInstance(getRawValue())
		.deepCopy(getRawValue());
    }

    /**
     * Allows traversing the data tree with a {@link DataVisitor}.
     * 
     * <p>
     * Calls {@code accept} on subnodes and then {@code visitor.visit} on this
     * node.
     * </p>
     *
     * @param visitor
     *            a node visitor.
     * @param parent
     *            the parent of the node. If unknown, use {@link DataValue#of()}
     * @param field
     *            The immediate field of a map under which current node is
     *            stored. If unknown, use "".
     * @param pos
     *            The position of the node in the parent array. If it is not in
     *            an array, use 0.
     */
    // todo write about default type reg
    public abstract void accept(DataVisitor visitor, TraceData parent, String field, int pos);

    /**
     * Converts the node to a simple Map/List tree suitable for JSON
     * serialization without the metadata information such as provenance.
     *
     * @return a tree made of the following objects:
     *         HashMap/ArrayList/String/Number/null
     */
    // todo asSimpleJavaType maybe is more descriptive
    public abstract Object asSimpleType();

    @Value.Check
    protected void check() {
	if (getRawValue() instanceof TraceData) {
	    throw new UnsupportedOperationException(
		    "Cannot contain data nodes! Raw value class: " + getRawValue().getClass().getName());
	}
    }

    /**
     * Creates a builder initialized with this values.
     */
    public abstract Builder fromThis();

    public abstract static class Builder {

	public abstract Builder from(TraceData instance);

	public abstract Builder setRawValue(Object rawVal);

	public abstract Builder setId(long id);

	public abstract Builder setRef(Ref ref);

	public abstract Builder setMetadata(NodeMetadata metadata);
	
	public abstract TraceData build();
    }
}