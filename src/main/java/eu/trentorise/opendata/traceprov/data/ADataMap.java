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

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * A data node wrapping a backing map.
 * 
 * Valid keys must be non-null strings. If they are not, errors will be thrown
 * lazily when keys are accessed.
 * 
 * Values will be lazily wrapped into DataNodes upon access.
 * 
 * @author David Leoni
 */
// todo a Forwarding map would be probably better
// todo currently only String keys are supported, but Long would make sense too
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DataMap.class)
@JsonDeserialize(as = DataMap.class)
class ADataMap extends TraceData implements Map<String, TraceData> {

    private static final long serialVersionUID = 1L;

    public static DataMap of(
	    Ref ref,
	    NodeMetadata metadata,
	    Map<String, ?> rawValue) {

	return DataMap.builder()
		.setRef(ref)
		.setMetadata(metadata)
		.setRawValue(rawValue)
		.build();
    }    

    
    /**
     * By default returns {@link ImmutableMap#of()}
     */
    @Value.Default
    @Override
    public Object getRawValue() {
	return ImmutableMap.of();
    }

  
    /**
     * Checks provided object is non-null String. If so, returns it, otherwise
     * throws exception
     * 
     * @throws IllegalStateException
     * 
     */
    private static String checkKey(@Nullable Object candidateKey) {
	if (candidateKey == null) {
	    throw new IllegalStateException("Found null key in backing map of a DataMap!");
	}
	if (candidateKey instanceof String) {
	    return (String) candidateKey;
	} else {
	    throw new IllegalStateException("Found non-string key in backing map of a DataMap! Key class is "
		    + candidateKey.getClass().getName());
	}
    }

   

    @Override
    public void accept(DataVisitor visitor, TraceData parent, String field, int pos) {
	Map<Object, Object> map = (Map) getRawValue();	
	for (Map.Entry entry : map.entrySet()) {
	    String key = checkKey(entry.getKey());
	    DataNodes.makeSubnode(
		    (DataMap) this,
		    key,
		    entry.getValue())
		    .accept(visitor, (DataMap)this, key, 0);
	}
	visitor.visit((DataMap) this, parent, field, pos);

    }

    @Override
    public Object asSimpleType() {
	TypeRegistry typeRegistry = TraceDb.getDb().getTypeRegistry();
	SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);
	accept(tran, DataValue.of(), "", 0);
	return tran.getResult();
    }



    @Override
    public int size() {
	Map map = (Map) getRawValue();
	return map.size();

    }

    @Override
    public boolean isEmpty() {
	Map map = (Map) getRawValue();
	return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
	checkKey(key);
	Map map = (Map) getRawValue();
	return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
	throw new UnsupportedOperationException("TODO Semantics of containsValue are not clear enough yet!! (i.e. contains DataNode or rawValue??)");
    }
    
    /**
     * Values may be lazily computed. 
     * 
     */
    // todo write more doc
    @Override
    public Collection<TraceData> values() {
	return Collections2.transform(entrySet(),
		new Function<Map.Entry<String, TraceData>, TraceData>(){

		    @Override
		    public TraceData apply(java.util.Map.Entry<String, TraceData> input) {
			return input.getValue();
		    }
	    
	});
    }    

    @Override
    public TraceData get(Object propertyName) {
	Map map = (Map) getRawValue();
	String prop = checkKey(propertyName);
	checkNotNull(propertyName, "Invalid property name!");
	if (map.containsKey(propertyName)) {
	    return DataNodes.makeSubnode((DataMap) this,
		    prop,
		    (Object) map.get(propertyName));

	} else {
	    return null;
	}

    }

    /**
     * @deprecated
     */
    @Override
    public TraceData put(String key, TraceData value) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported");
    }

    /**
     * @deprecated
     */
    @Override
    public TraceData remove(Object key) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * @deprecated
     */
    @Override
    public void putAll(Map<? extends String, ? extends TraceData> m) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated
     */
    @Override
    public void clear() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class ForwardingCollectionSet<T> extends ForwardingCollection<T>implements Set<T> {

	private Collection delegate;

	ForwardingCollectionSet(Collection<T> delegate) {
	    checkNotNull(delegate);
	    this.delegate = delegate;
	}

	protected Collection<T> delegate() {
	    return delegate;
	}
    }

    /**
     * Returned set might be lazily computed.
     */
    // todo write more
    @Override
    public Set<String> keySet() {

	Map map = (Map) getRawValue();  
	
	Set<String> keySet = map.keySet();

	return new ForwardingCollectionSet(Collections2.transform(map.keySet(),
		new Function<Object, String>() {
		    @Override
		    public String apply(Object input) {
			return checkKey(input);
		    }
		}));
    }

    /**
     * Entryset may be lazily computed.
     */
    // todo write more doc
    @Override
    public Set<Map.Entry<String, TraceData>> entrySet() {
	final DataMap thisDataMap = (DataMap) this;

	Map map = (Map) getRawValue();
	return new ForwardingCollectionSet(Collections2.transform(map.entrySet(),
		new Function<Map.Entry, Map.Entry<String, TraceData>>() {
		    @Override
		    public Map.Entry<String, TraceData> apply(Map.Entry input) {
			String skey = checkKey(input.getKey());

			return Maps.immutableEntry(
				skey,
				DataNodes.makeSubnode(thisDataMap,
					skey, input.getValue()));

		    }
		}));

    }
    
 
    @Override
    public Builder fromThis() {
	return DataMap.builder().from(this);
    }

    @Value.Check
    protected void check() {
	super.check();

	if (!(getRawValue() instanceof Map)) {
	    String className = getRawValue() == null ? "null" : getRawValue().getClass().getCanonicalName();
	    throw new IllegalStateException("Only supported raw value for now is Map! Found instead " + className);
	}
    }

    public static abstract class  Builder extends TraceData.Builder {	
	
    }

}
