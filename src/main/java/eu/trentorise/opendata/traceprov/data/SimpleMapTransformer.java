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

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Produces a simple HashMap/ArrayList version of an {@link TraceData} tree. Only
 * data values will be kept (metadata will be stripped). Maps must have String keys.
 * 
 * @author David Leoni
 */
final class SimpleMapTransformer implements DataVisitor {
    private LinkedList<Map.Entry> stack;
    private TypeRegistry typeRegistry;
    
    
    public SimpleMapTransformer(TypeRegistry typeRegistry) {
	checkNotNull(typeRegistry);
	this.typeRegistry = typeRegistry;
	stack = new LinkedList();
    }

    @Override
    public void visit(DataMap dataMap, TraceData parent, String key, int pos) {

	Map map = new HashMap();

	for (Map.Entry e : dataMap.entrySet()){
	
	    Map.Entry stackEntry = stack.removeFirst();
	    map.put(stackEntry.getKey(), stackEntry.getValue());
	}
	stack.addFirst(Maps.immutableEntry(key, map));
    }

    @Override
    public void visit(DataObject dataObject, TraceData parent, String key, int pos) {

	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(dataObject.getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + dataObject.getRawValue().getClass(), ex);
	}

	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null) {
		Object res;
		try {
		    res = readMethod.invoke(dataObject.getRawValue());
		    stack.addFirst(Maps.immutableEntry(readMethod.getName(), res));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + dataObject.getRawValue().getClass(),
			    ex);
		}

	    }

	}
    }

    @Override
    public void visit(DataArray nodeList, TraceData parent, String key, int pos) {
	List ret = new ArrayList();

	for (int i = 0; i < Iterables.size(nodeList); i++) {
	    Map.Entry entry = stack.removeFirst();
	    ret.add(entry.getValue());
	}
	Collections.reverse(ret);
	stack.addFirst(Maps.immutableEntry(key, ret));

    }

    @Override
    public void visit(DataValue DataValue, TraceData parent, String key, int pos) {
	stack.addFirst(Maps.immutableEntry(key, DataValue.getRawValue()));
    }

    public Object getResult() {
	checkState(stack.size() == 1, "Stack should have only one element, found instead %s elements", stack.size());
	return stack.getFirst().getValue();
    }
}