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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkNotNull;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.TraceData.Builder;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.xml.crypto.Data;

import org.immutables.value.Value;

/**
 * {@link TraceFile} body node containing an array of other nodes.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DataArray.class)
@JsonDeserialize(as = DataArray.class)
abstract class ADataArray extends TraceData implements Iterable<TraceData> {

    private static final long serialVersionUID = 1L;
    private static final int MAX_PRINTED_NODES = 10;
    private static final Logger LOG = Logger.getLogger(DataArray.class.getSimpleName());

    @Override
    public Iterator<TraceData> iterator() {
	return new NodeIterator(this);
    }

 
    /**
     * 
     * @param rawValues NOTE: the iterable MUST NOT be anoter DataNode! (although it can contain DataNode(s)
     */
    public static DataArray of(Ref ref, NodeMetadata metadata, Iterable rawValues) {
	return DataArray.builder()
		.setRef(ref)
		.setMetadata(metadata)
		.setRawValue(rawValues)
		.build();
    }

    /**
     * @param rawValues They can be DataNode(s)
     */
    public static DataArray of(Ref ref, NodeMetadata metadata, Object... rawValues) {
	return DataArray.builder()
		.setRef(ref)
		.setRawValue(Lists.newArrayList(rawValues))
		.build();
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	int i = 0;
	for (TraceData n : this) {
	    if (i > 0) {
		sb.append(", ");
	    }
	    if (i < MAX_PRINTED_NODES) {
		sb.append(n.toString());
	    } else {
		sb.append("...");
		break;
	    }
	    i++;
	}
	sb.append("]");
	return "NodeList{ref=" + getRef() + ", metadata=" + getMetadata() + ", data=" + sb.toString() + '}';
    }

    @Override
    public void accept(DataVisitor visitor, TraceData parent, String field, int pos) {
	int i = 0;
	Iterable iterable = (Iterable) getRawValue();
	Iterator<TraceData> iter = iterable.iterator();

	while (iter.hasNext()) {
	    TraceData node = iter.next();
	    node.accept(visitor, this, field, i);
	    i++;
	}

	visitor.visit((DataArray) this, parent, field, pos);

    }

    @Override
    public Object asSimpleType() {
	TypeRegistry typeRegistry = TraceDb.getDb().getTypeRegistry();
	SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);
	accept(tran, DataValue.of(), "", 0);
	return tran.getResult();
    }

    private static class NodeIterator implements Iterator<TraceData> {

	private Iterator iterator;
	private DataArray dataArray;
	private long index;

	public NodeIterator(ADataArray dataArray) {
	    checkNotNull(dataArray);
	    
	    Iterable iterable = (Iterable) dataArray.getRawValue();
	    this.iterator = iterable.iterator();
	    this.index = 0;
	}

	@Override
	public boolean hasNext() {
	    return iterator.hasNext();
	}

	@Override
	public TraceData next() {
	    Object obj = iterator.next();
	    TraceData ret;

	    if (obj instanceof TraceData) {
		ret = (TraceData) obj;
	    } else {
		ret = DataNodes.makeSubnode(dataArray, index, obj);
	    }

	    index += 1;
	    return ret;

	}

	@Override
	public void remove() {
	    throw new UnsupportedOperationException("REMOVE IS NOT UNSUPPORTED!");
	}

    }

    /**
     * By default returns {@link ImmutableList#of()}
     */
    @Value.Default
    @Override
    public Object getRawValue() {
	return ImmutableList.of();
    }
    
    @Value.Check
    protected void check() {
	super.check();

	if (!(getRawValue() instanceof Iterable)) {
	    String className = getRawValue() == null ? "null" : getRawValue().getClass().getCanonicalName();
	    throw new IllegalStateException("Only supported raw value for now is Iterable! Found instead " + className);
	}
    }

    @Override
    public Builder fromThis() {	
	return DataArray.builder().from(this);
    }
    
    
    public static abstract class Builder extends TraceData.Builder {	
	
    }
}
