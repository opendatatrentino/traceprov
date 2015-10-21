package eu.trentorise.opendata.traceprov.data;

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.DataNode.Builder;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 * A data node that encapsulates a Java object which is supposed to respect bean
 * conventions. Also, getters are not supposed to throw exceptions.
 * 
 * @author David Leoni
 */
// todo implement asDataMap? abstract map interface from DataMap?
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DataObject.class)
@JsonDeserialize(as = DataObject.class)
class ADataObject extends DataNode {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     * By default returns null
     */
    @Nullable
    @Value.Default
    @Override
    public Object getRawValue() {		
	return null;	
    }

    
    private static String stripIsGet(String getterName) {
	if (getterName.startsWith("is")) {
	    return getterName.substring(2);
	} else if (getterName.startsWith("get")) {
	    return getterName.substring(3);
	} else {
	    throw new IllegalArgumentException("Provided method name doesn't look like a bean getter: " + getterName);
	}

    }

    /**
     * Returns true if map has given property
     * 
     * @see #getPropertyDef(java.lang.String)
     */
    public boolean has(String propertyName) {
	checkNotEmpty(propertyName, "Invalid property name!");
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null && stripIsGet(readMethod.getName()).equals(propertyName)) {
		return true;
	    }

	}

	return false;
    }

    public List<String> keys() {

	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	List<String> ret = new ArrayList();

	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();
	    if (readMethod != null) {
		ret.add(stripIsGet(readMethod.getName()));
	    }
	}
	return ret;
    }

    public List<DataNode> values() {

	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	List<DataNode> ret = new ArrayList();
	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null) {
		String propertyName = stripIsGet(readMethod.getName());
		Object res;
		try {
		    res = readMethod.invoke(getRawValue());
		    ret.add(DataNodes.makeSubnode(this,propertyName, res));

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + getRawValue().getClass(),
			    ex);
		}

	    }

	}

	return ret;

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
	PropertyDescriptor[] propertyDescriptors;

	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	
	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null && stripIsGet(readMethod.getName()).equals(propertyName)) {
		Object res;
		try {
		    res = readMethod.invoke(getRawValue());

		    return DataNodes.makeSubnode(this, propertyName, res);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + getRawValue().getClass(),
			    ex);
		}

	    }

	}

	throw new TraceProvNotFoundException("Couldn't find property " + propertyName);
    }
    
    public List<Map.Entry<String, ? extends DataNode>> entries(){

	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	List<Map.Entry<String, ? extends DataNode>> ret = new ArrayList();
	
	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null) {
		String propertyName = stripIsGet(readMethod.getName());
		Object res;
		try {
		    res = readMethod.invoke(getRawValue());
		    
		    Entry<String, DataNode> entry = Maps.immutableEntry(propertyName, 
			    DataNodes.makeSubnode(this, propertyName, res));
		    ret.add(entry);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + getRawValue().getClass(),
			    ex);
		}

	    }

	}

	return ret;
    }

    @Override
    public void accept(
	    DataVisitor visitor, 
	    DataNode parent, 
	    String field, 
	    int pos) {
	values();
	for (Map.Entry<String, ? extends DataNode> entry : entries()) {
	    entry.getValue().accept(visitor, this, entry.getKey(), 0);
	}
	visitor.visit((DataObject) this, parent, field, pos);
    };

    @Override
    public Object asSimpleType() {
	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);
	accept(tran, DataValue.of(), "", 0);
	return tran.getResult();
    }


    public static DataObject of(Ref ref, NodeMetadata metadata, @Nullable Object obj) {
	return DataObject.builder()
		.setRef(ref)
		.setMetadata(metadata)
		.setRawValue(obj)
		.build();
    }

    @Override
    public Builder fromThis() {	
	return DataObject.builder().from(this);
    }
    
    public static abstract class  Builder extends DataNode.Builder {	
	
    }

}
