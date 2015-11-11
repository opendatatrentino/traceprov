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
import eu.trentorise.opendata.traceprov.data.TraceData.Builder;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;




import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import eu.trentorise.opendata.commons.validation.Ref;
import java.io.ObjectStreamException;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * 
 * A data node that encapsulates a Java object which is supposed to respect bean
 * conventions. Also, getters are not supposed to throw exceptions.
 * 
 * @author David Leoni
 */
// todo implement asDataMap? abstract map interface from DataMap?
 
//@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@Immutable
public final class DataObject<T> extends TraceData {
  private final @Nullable T rawValue;
  private final long id;
  private final Ref ref;
  private final NodeMetadata metadata;
   

  
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

  public List<TraceData> values() {

	TypeRegistry typeRegistry = TraceDb.getDb().getTypeRegistry();
	
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	List<TraceData> ret = new ArrayList();
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
  public TraceData get(String propertyName) {
	checkNotEmpty(propertyName, "Invalid property name!");
	PropertyDescriptor[] propertyDescriptors;

	TypeRegistry typeRegistry = TraceDb.getDb().getTypeRegistry();
	
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
  
  public List<Map.Entry<String, ? extends TraceData>> entries(){

	TypeRegistry typeRegistry = TraceDb.getDb().getTypeRegistry();
	
	PropertyDescriptor[] propertyDescriptors;

	try {
	    propertyDescriptors = Introspector.getBeanInfo(getRawValue().getClass(), Object.class)
		    .getPropertyDescriptors();

	} catch (IntrospectionException ex) {
	    throw new TraceProvException(
		    "Error while accessing properties of Java object of class " + getRawValue().getClass(), ex);
	}

	List<Map.Entry<String, ? extends TraceData>> ret = new ArrayList();
	
	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

	    @Nullable
	    Method readMethod = propertyDescriptor.getReadMethod();

	    if (readMethod != null) {
		String propertyName = stripIsGet(readMethod.getName());
		Object res;
		try {
		    res = readMethod.invoke(getRawValue());
		    
		    Entry<String, TraceData> entry = Maps.immutableEntry(propertyName, 
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
	    TraceData parent, 
	    String field, 
	    int pos) {
	values();
	for (Map.Entry<String, ? extends TraceData> entry : entries()) {
	    entry.getValue().accept(visitor, this, entry.getKey(), 0);
	}
	visitor.visit((DataObject) this, parent, field, pos);
  };

  @Override
  public Object asSimpleType() {
	TypeRegistry typeRegistry = TraceDb.getDb().getTypeRegistry();
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
  
  
  private DataObject() {
    this.rawValue = null;
    this.id = super.getId();
    this.ref = Preconditions.checkNotNull(super.getRef());
    this.metadata = Preconditions.checkNotNull(super.getMetadata());
  }

  private DataObject(DataObject.Builder<T> builder) {
    this.rawValue = builder.rawValue ;
    this.id = builder.idIsSet()
        ? builder.id
        : super.getId();
    this.ref = builder.ref != null
        ? builder.ref
        : Preconditions.checkNotNull(super.getRef());
    this.metadata = builder.metadata != null
        ? builder.metadata
        : Preconditions.checkNotNull(super.getMetadata());
  }

  private DataObject(
      @Nullable T rawValue,
      long id,
      Ref ref,
      NodeMetadata metadata) {
    this.rawValue = rawValue;
    this.id = id;
    this.ref = ref;
    this.metadata = metadata;
  }

  /**
   * {@inheritDoc}
   * By default returns null
   */
  @Override
  public @Nullable T getRawValue() {
    return rawValue;
  }

  /**
   * The numerical id of the datanode inside a {@link TraceDb}. If unknown
   * defaults to -1.
   */
  @Override
  public long getId() {
    return id;
  }

  /**
   * A reference to position in the original file from which this node comes
   * from. If unknown, {@link Ref#of()} is returned.
   */
  @Override
  public Ref getRef() {
    return ref;
  }

  /**
   * Provenance information about the node.
   */
  @Override
  public NodeMetadata getMetadata() {
    return metadata;
  }

  /**
   * Copy current immutable object by setting value for {@link ADataObject#getRawValue() rawValue}.
   * Shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value new value for rawValue, can be {@code null}
   * @return modified copy of the {@code this} object
   */
  public final DataObject withRawValue(@Nullable java.lang.Object value) {
    if (this.rawValue == value) {
      return this;
    }
    @Nullable java.lang.Object newValue = value;
    return validate(new DataObject(newValue, this.id, this.ref, this.metadata));
  }

  /**
   * Copy current immutable object by setting value for {@link ADataObject#getId() id}.
   * Value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value new value for id
   * @return modified copy of the {@code this} object
   */
  public final DataObject<T> withId(long value) {
    if (this.id == value) {
      return this;
    }
    long newValue = value;
    return validate(new DataObject(this.rawValue, newValue, this.ref, this.metadata));
  }

  /**
   * Copy current immutable object by setting value for {@link ADataObject#getRef() ref}.
   * Shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value new value for ref
   * @return modified copy of the {@code this} object
   */
  public final DataObject<T> withRef(Ref value) {
    if (this.ref == value) {
      return this;
    }
    Ref newValue = Preconditions.checkNotNull(value);
    return validate(new DataObject(this.rawValue, this.id, newValue, this.metadata));
  }

  /**
   * Copy current immutable object by setting value for {@link ADataObject#getMetadata() metadata}.
   * Shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value new value for metadata
   * @return modified copy of the {@code this} object
   */
  public final DataObject<T> withMetadata(NodeMetadata value) {
    if (this.metadata == value) {
      return this;
    }
    NodeMetadata newValue = Preconditions.checkNotNull(value);
    return validate(new DataObject(this.rawValue, this.id, this.ref, newValue));
  }

  /**
   * This instance is equal to instances of {@code DataObject} with equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    return this == another
        || (another instanceof DataObject && equalTo((DataObject) another));
  }

  private boolean equalTo(DataObject another) {
    return Objects.equal(rawValue, another.rawValue)
        && id == another.id
        && ref.equals(another.ref)
        && metadata.equals(another.metadata);
  }

  /**
   * Computes hash code from attributes: {@code rawValue}, {@code id}, {@code ref}, {@code metadata}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + Objects.hashCode(rawValue);
    h = h * 17 + Longs.hashCode(id);
    h = h * 17 + ref.hashCode();
    h = h * 17 + metadata.hashCode();
    return h;
  }

  /**
   * Prints immutable value {@code DataObject{...}} with attribute values,
   * excluding any non-generated and auxiliary attributes.
   * @return string representation of value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("DataObject")
        .add("rawValue", rawValue)
        .add("id", id)
        .add("ref", ref)
        .add("metadata", metadata)
        .toString();
  }

  /**
   * Simple representation of this value type suitable Jackson binding
   * @deprecated Do not use this type directly, it exists only for <em>Jackson</em>-binding infrastructure
   */
  static final class Json {
    @JsonProperty
    @Nullable java.lang.Object rawValue;
    @JsonProperty
    @Nullable Long id;
    @JsonProperty
    @Nullable Ref ref;
    @JsonProperty
    @Nullable NodeMetadata metadata;
  }

  /**
   * @return JSON-bindable data structure
   * @deprecated Do not use this method directly, it exists only for <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonValue
  Json toJson() {
    Json json = new Json();
    json.rawValue = this.rawValue;
    json.id = this.id;
    json.ref = this.ref;
    json.metadata = this.metadata;
    return json;
  }

  /**
   * @param json JSON-bindable data structure
   * @return immutable value type
   * @deprecated Do not use this method directly, it exists only for <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonCreator
  static DataObject fromJson(Json json) {
    DataObject.Builder builder = DataObject.builder();
    if (json.rawValue != null) {
      builder.setRawValue(json.rawValue);
    }
    if (json.id != null) {
      builder.setId(json.id);
    }
    if (json.ref != null) {
      builder.setRef(json.ref);
    }
    if (json.metadata != null) {
      builder.setMetadata(json.metadata);
    }
    return builder.build();
  }

  private static final DataObject INSTANCE = validate(new DataObject());

  /**
   * Returns default immutable singleton value of {@code DataObject}
   * @return immutable instance of DataObject
   */
  public static DataObject of() {
    return INSTANCE;
  }

  private static DataObject validate(DataObject instance) {
    instance.check();
    return INSTANCE != null && INSTANCE.equalTo(instance) ? INSTANCE : instance;
  }

  /**
   * Creates immutable copy of {@link ADataObject}.
   * Uses accessors to get values to initialize immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance instance to copy
   * @return copied immutable DataObject instance
   */
  public static DataObject copyOf(DataObject instance) {
    if (instance instanceof DataObject) {
      return (DataObject) instance;
    }
    return DataObject.builder()
        .from(instance)
        .build();
  }

  private static final long serialVersionUID = 1L;

  private Object readResolve() throws ObjectStreamException {
    return validate(this);
  }

  /**
   * Creates builder for {@link eu.trentorise.opendata.traceprov.data.DataObject DataObject}.
   * @return new DataObject builder
   */
  public static <T> DataObject.Builder<T> builder() {
    return new DataObject.Builder();
  }

  /**
   * Builds instances of {@link eu.trentorise.opendata.traceprov.data.DataObject DataObject}.
   * Initialize attributes and then invoke {@link #build()} method to create
   * immutable instance.
   * <p><em>{@code Builder} is not thread safe and generally should not be stored in field or collection,
   * but used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder<W> extends TraceData.Builder {
    private static final long NONDEFAULT_BIT_ID = 0x1L;
    private long nondefaultBitset;
    private @Nullable W rawValue;
    private long id;
    private @Nullable Ref ref;
    private @Nullable NodeMetadata metadata;

    private Builder() {}

    /**
     * Fill builder with attribute values from provided {@link eu.trentorise.opendata.traceprov.data.TraceData} instance.
     * @param instance instance to copy values from
     * @return {@code this} builder for chained invocation
     */
    public final Builder from(TraceData instance) {
      Preconditions.checkNotNull(instance);
      from((Object) instance);
      return this;
    }

    /**
     * Fill builder with attribute values from provided {@link eu.trentorise.opendata.traceprov.data.ADataObject} instance.
     * @param instance instance to copy values from
     * @return {@code this} builder for chained invocation
     */
    public final Builder<W> from(DataObject<W> instance) {
      Preconditions.checkNotNull(instance);
      from((Object) instance);
      return this;
    }

    private void from(Object object) {
      if (object instanceof TraceData) {
        TraceData instance = (TraceData) object;
        setId(instance.getId());
        setRef(instance.getRef());
        setMetadata(instance.getMetadata());
      }
      if (object instanceof DataObject) {
        DataObject<W> instance = (DataObject<W>) object;
        @Nullable W rawValueValue = instance.getRawValue();
        if (rawValueValue != null) {
          setRawValue(rawValueValue);
        }
      }
    }

    /**
     * Initializes value for {@link DataObject#getRawValue() rawValue}.
     * <p><em>If not set, this attribute will have default value returned by initializer of {@link ADataObject#getRawValue() rawValue}.</em>
     * @param rawValue value for rawValue, can be {@code null}
     * @return {@code this} builder for chained invocation
     */
    public final Builder<W> setRawValue(@Nullable Object rawValue) {
      this.rawValue = (W) rawValue;
      return this;
    }

    /**
     * Initializes value for {@link ADataObject#getId() id}.
     * <p><em>If not set, this attribute will have default value returned by initializer of {@link ADataObject#getId() id}.</em>
     * @param id value for id
     * @return {@code this} builder for chained invocation
     */
    public final Builder<W> setId(long id) {
      this.id = id;
      nondefaultBitset |= NONDEFAULT_BIT_ID;
      return this;
    }

    /**
     * Initializes value for {@link ADataObject#getRef() ref}.
     * <p><em>If not set, this attribute will have default value returned by initializer of {@link ADataObject#getRef() ref}.</em>
     * @param ref value for ref
     * @return {@code this} builder for chained invocation
     */
    public final Builder<W> setRef(Ref ref) {
      this.ref = Preconditions.checkNotNull(ref);
      return this;
    }

    /**
     * Initializes value for {@link ADataObject#getMetadata() metadata}.
     * <p><em>If not set, this attribute will have default value returned by initializer of {@link ADataObject#getMetadata() metadata}.</em>
     * @param metadata value for metadata
     * @return {@code this} builder for chained invocation
     */
    public final Builder<W> setMetadata(NodeMetadata metadata) {
      this.metadata = Preconditions.checkNotNull(metadata);
      return this;
    }

    /**
     * Builds new {@link eu.trentorise.opendata.traceprov.data.DataObject DataObject}.
     * @return immutable instance of DataObject
     */
    public DataObject<W> build() {
      return DataObject.validate(new DataObject(this));
    }

    private boolean idIsSet() {
      return (nondefaultBitset & NONDEFAULT_BIT_ID) != 0;
    }
  }
}
