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
import com.google.common.base.Preconditions;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.ADataMap.Builder;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.immutables.value.Value;

/**
 * Node containing a ground json-compatible value, like null, number or String.
 * To hold more complex objects use {@link DataObject} 
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DataValue.class)
@JsonDeserialize(as = DataValue.class)
abstract class ADataValue extends DataNode {

    private static final long serialVersionUID = 1L;    


    /**
     * A ground json-compatible value, like null, number, string. By default
     * returns null.
     */
    @Nullable
    @Override
    @Value.Default
    public Object getRawValue() {
	return null;
    }     

    @Override
    public void accept(DataVisitor visitor, DataNode parent, String field, int pos) {
	visitor.visit((DataValue) this, parent, field, pos);
    }

    @Override
    public Object asSimpleType() {
	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);
	accept(tran, DataValue.of(), "", 0);
	return tran.getResult();
    }

    /**
     * Construct new immutable {@code DataValue} instance.
     *
     * @param value
     *            a String, a Number or null
     */
    public static DataValue of(@Nullable Object value) {
	return DataValue.builder()
		.setRawValue(value)
		.build();
    }

    /**
     * Construct new immutable {@code DataValue} instance.
     *
     * @param value
     *            a String, a Number or a null
     * @param ref
     *            a reference to the provenance. If unknown, use
     *            {@link DocRef#of()}
     */
    public static DataValue of(
	    Ref ref, 
	    NodeMetadata metadata, 
	    @Nullable Object value) {
	return DataValue.builder()
		.setRef(ref)
		.setMetadata(metadata)
		.setRawValue(value)
		.build();
    }



    @Value.Check
    protected void check() {
	super.check();
	Preconditions.checkArgument(getRawValue() == null
		|| getRawValue() instanceof Number
		|| getRawValue() instanceof String);
    }
    
    @Override
    public Builder fromThis() {
	return DataValue.builder();
    }
    
    public static abstract class Builder extends DataNode.Builder {	
	
    }


}
