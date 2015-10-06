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

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * A data node containing a map of fields to other nodes.
 * 
 * @author David Leoni
 */

public class DataMap extends DataNode {

    private static final DataMap INSTANCE = new DataMap();

    private static final long serialVersionUID = 1L;

    DataMap() {
	super(Ref.of(), NodeMetadata.of(), new HashMap());

    }

    protected DataMap(
	    Ref ref,
	    NodeMetadata metadata,
	    Map<String, ? extends DataNode> nodes) {
	super(ref, metadata, nodes);
	checkNotNull(nodes);
    }

    public static DataMap of() {
	return INSTANCE;
    }

    public static DataMap of(
	    Ref ref,
	    NodeMetadata metadata,
	    Map<String, ? extends DataNode> nodes) {

	return new DataMap(ref, metadata, nodes);
    }


    @Override
    public Map<String, ? extends DataNode> getValue() {
	return (Map<String, ? extends DataNode>) super.getValue();
    }

    /**
     * Returns true if map has given property
     * 
     * @see #getPropertyDef(java.lang.String)
     */
    public boolean has(String propertyName) {
	checkNotEmpty(propertyName, "Invalid property name!");
	return getValue().containsKey(propertyName);
    }

    /**
     * Returns given property, or throws exception if not found
     * 
     * @throws eu.trentorise.opendata.traceprov.exceptions.
     *             TraceProvNotFoundException
     * @see #hasPropertyDef(java.lang.String)
     */
    public DataNode get(String propertyName) {
	checkNotEmpty(propertyName, "Invalid property name!");
	if (getValue().containsKey(propertyName)) {
	    return getValue().get(propertyName);
	} else {
	    throw new TraceProvNotFoundException("Couldn't find property '" + propertyName + "'");
	}
    }

    @Override
    public void accept(DataVisitor visitor, DataNode parent, String field, int pos) {
	for (Map.Entry<String, ? extends DataNode> entry : getValue().entrySet()) {
	    entry.getValue().accept(visitor, this, entry.getKey(), 0);
	}
	visitor.visit(this, parent, field, pos);
    };

    @Override
    public Object asSimpleType(TypeRegistry typeRegistry) {
	SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);
	accept(tran, DataValue.of(), "", 0);
	return tran.getResult();
    }

}
