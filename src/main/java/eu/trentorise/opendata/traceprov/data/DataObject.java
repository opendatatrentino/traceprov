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

import com.google.common.collect.Maps;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 * A data node that encapsulates a Java object which is supposed to respect bean
 * conventions. Also, getters are not supposed to throw exceptions.
 * 
 * @author David Leoni
 */
public class DataObject<T> extends DataNode {

    private static final DataObject INSTANCE = new DataObject();

    private static final long serialVersionUID = 1L;

    private DataObject() {
	super(Ref.of(), NodeMetadata.of(), new Object());

    }

    private DataObject(Ref ref, NodeMetadata metadata, @Nullable T obj) {
	super(ref, metadata, obj);
    }

    public static DataObject of() {
	return INSTANCE;
    }

    public static DataObject of(Ref ref, NodeMetadata metadata, @Nullable Object obj) {
	return new DataObject(ref, metadata, obj);
    }

    /**
     * Returns the wrapped Java object. The object is copied with
     */
    @Override
    public T getValue() {

	return (T) super.getValue();
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
	    propertyDescriptors = Introspector.getBeanInfo(getValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getValue().getClass(), ex);
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

    public List<String> keySet() {

	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getValue().getClass(), ex);
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

    public List<DataNode> values(TypeRegistry typeRegistry) {

	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getValue().getClass(), ex);
	}

	List<DataNode> ret = new ArrayList();
	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null) {
		String propertyName = stripIsGet(readMethod.getName());
		Object res;
		try {
		    res = readMethod.invoke(getValue());
		    Ref newRef = DataNodes.makeSubRef(getRef(), propertyName);

		    NodeMetadata newMetadata = DataNodes.makeMetadata(getMetadata(),
			    res, typeRegistry);

		    ret.add(DataNodes.makeNode(newRef, newMetadata, res, typeRegistry));

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + getValue().getClass(),
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
    public DataNode get(String propertyName, TypeRegistry typeRegistry) {
	checkNotEmpty(propertyName, "Invalid property name!");
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getValue().getClass(), ex);
	}

	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null && stripIsGet(readMethod.getName()).equals(propertyName)) {
		Object res;
		try {
		    res = readMethod.invoke(getValue());
		    Ref newRef = DataNodes.makeSubRef(getRef(), propertyName);

		    NodeMetadata newMetadata = DataNodes.makeMetadata(getMetadata(),
			    res, typeRegistry);

		    return DataNodes.makeNode(newRef, newMetadata, res, typeRegistry);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + getValue().getClass(),
			    ex);
		}

	    }

	}

	throw new TraceProvNotFoundException("Couldn't find property " + propertyName);
    }
    
    public List<Map.Entry<String, ? extends DataNode>> entrySet(TypeRegistry typeRegistry){
	
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getValue().getClass(), ex);
	}

	List<Map.Entry<String, ? extends DataNode>> ret = new ArrayList();
	
	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null) {
		String propertyName = stripIsGet(readMethod.getName());
		Object res;
		try {
		    res = readMethod.invoke(getValue());
		    Ref newRef = DataNodes.makeSubRef(getRef(), propertyName);

		    NodeMetadata newMetadata = DataNodes.makeMetadata(getMetadata(),
			    res, typeRegistry);
		    
		    Entry<String, DataNode> entry = Maps.immutableEntry(propertyName, 
			    DataNodes.makeNode(newRef, newMetadata, res, typeRegistry));
		    ret.add(entry);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
		    throw new TraceProvException(
			    "Error while invoking getter " + readMethod.toString() + "  of Java object of class "
				    + getValue().getClass(),
			    ex);
		}

	    }

	}

	return ret;
    }

    @Override
    public void accept(DataVisitor visitor, DataNode parent, String field, int pos) {
	values(visitor.getTypeRegistry());
	for (Map.Entry<String, ? extends DataNode> entry : entrySet(visitor.getTypeRegistry())) {
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
