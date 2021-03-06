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
package eu.trentorise.opendata.traceprov.types;

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;
import static eu.trentorise.opendata.traceprov.validation.Preconditions.checkType;

import eu.trentorise.opendata.traceprov.TraceProvModule;
import eu.trentorise.opendata.traceprov.TraceProvs;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.esotericsoftware.kryo.Kryo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Todo this is much in WTF status
 * 
 * NOT threadsafe.
 *
 * @author David Leoni
 */
public class TypeRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = Logger.getLogger(TypeRegistry.class.getSimpleName());

    private Map<String, TraceType> types;

    private ObjectMapper objectMapper;

    private Kryo kryo;

    /**
     * Maps Java Class canonical name (i.e. java.util.HashMap) to TraceProv Type
     * id (todo put example)
     */
    private Map<String, String> canonicalTypes;

    private Map<String, Def<ClassType>> classDefs;

    private TypeRegistry() {
        this.types = new HashMap();
        this.classDefs = new HashMap();
        this.objectMapper = new ObjectMapper();
        TraceProvModule.registerModulesInto(this.objectMapper);
        this.canonicalTypes = new HashMap();
        this.kryo = new Kryo();
    }

    private TypeRegistry(ObjectMapper objectMapper) {
        this();
        checkNotNull(objectMapper);
        this.objectMapper = objectMapper;
    }

    /**
     * 
     * @return The previous value associated with key, or null if there was no
     *         mapping for key.
     */
    @Nullable
    public Def<ClassType> put(String id, Def<ClassType> classDef) {
        checkNotEmpty(id, "Invalid classDef id!");
        checkNotNull(classDef);
        Def<ClassType> ret = this.classDefs.get(id);
        this.classDefs.put(id, classDef);
        return ret;
    }

    public boolean hasClassDef(String classDefId) {
        checkNotEmpty(classDefId, "Invalid classDef id!");
        return !(classDefs.get(classDefId) == null);
    }

    /**
     * @throws eu.trentorise.opendata.traceprov.exceptions.
     *             TraceProvNotFoundException
     */
    public Def<ClassType> getClassDef(String classDefId) {
        checkNotEmpty(classDefId, "Invalid classDef id!");
        Def<ClassType> classDef = classDefs.get(classDefId);
        if (classDef == null) {
            throw new TraceProvNotFoundException("Couldn't find classDefId " + classDefId);
        }
        return classDef;
    }

    /**
     * Registers a new type.
     * 
     * If the type's default Java class is not yet registered as canonical class
     * for some type, it is associated to provided type.
     * 
     * @return The previous value associated with key, or null if there was no
     *         mapping for key.
     */
    @Nullable
    public TraceType put(TraceType type) {
        checkType(type);
        TraceType ret = this.types.get(type.getId());
        this.types.put(type.getId(), type);
        String canTypeId = this.canonicalTypes.get(type.getJavaClass());
        if (canTypeId == null) {
            setCanonicalType(type.getJavaClass(), type);
        }
        return ret;
    }

    /**
     * 
     * @throws IllegalStateException
     *             if the provided type is not registered.
     */
    public void checkRegistered(String typeId) {
        checkNotEmpty(typeId, "Invalid TraceType id!");
        if (!contains(typeId)) {
            throw new IllegalStateException("Provided tracetype with id " + typeId + " is not registered!");
        }
    }

    /**
     * 
     * @throws IllegalStateException
     *             if the provided type is not registered.
     */
    public void checkRegistered(TraceType traceType) {
        checkNotNull(traceType);
        checkRegistered(traceType.getId());
    }

    /**
     * Returns the canonical TraceProv Type associated to the given Java class.
     * 
     * @throws TraceProvNotFoundException
     */
    public TraceType getCanonicalType(Class clazz) {
        checkNotNull(clazz);
        String typeId = this.canonicalTypes.get(clazz.getCanonicalName());
        if (typeId == null) {
            throw new TraceProvNotFoundException(
                    "There is no canonical type associated to class " + clazz.getCanonicalName());
        }
        return get(typeId);
    }

    /**
     * Associates given Java class to a registered TraceProv type. To a Java
     * class can be associated only one TraceProv type, but many TraceProv types
     * can be associated to the same Java class.
     * 
     * @throws IllegalStateException
     *             if the provided type is not registered.
     */
    public void setCanonicalType(Class clazz, TraceType traceType) {
        checkNotNull(clazz);
        checkRegistered(traceType);
        String clazzName = clazz.getCanonicalName();
        this.canonicalTypes.put(clazzName, traceType.getId());
    }

    public boolean contains(String typeId) {
        checkNotEmpty(typeId, "Invalid type id!");
        return !(types.get(typeId) == null);
    }

    /**
     * Returns the default instance of a given datatype id
     * 
     * @throws eu.trentorise.opendata.traceprov.exceptions.
     *             TraceProvNotFoundException
     */
    public TraceType get(String typeId) {
        checkNotEmpty(typeId, "Invalid type id!");
        TraceType type = types.get(typeId);
        if (type == null) {
            throw new TraceProvNotFoundException("Couldn't find typeId " + typeId);
        }
        return type;
    }

    /**
     * Creates new empty registry
     */
    public static TypeRegistry empty() {
        return new TypeRegistry();
    }

    /**
     * Creates new registry filled with default datatypes in
     * {@link eu.trentorise.opendata.traceprov.types}
     */
    public static TypeRegistry of() {
        ObjectMapper om = new ObjectMapper();
        TraceProvModule.registerModulesInto(om);
        return TypeRegistry.of(om);
    }

    /**
     * Creates new registry filled with default datatypes in
     * {@link eu.trentorise.opendata.traceprov.types}
     * 
     * * @param objectMapper The object mapper to use for serialization and
     * deserialization. Normally it can be safely shared with other instances
     * except when they reconfigure it - in this case other threads must not use
     * the object mapper during reconfiguration.
     * 
     */
    public static TypeRegistry of(ObjectMapper objectMapper) {
        TypeRegistry reg = new TypeRegistry(objectMapper);
        reg.put(AnyType.of());
        reg.put(BooleanType.of());
        reg.put(CollectionType.of());
        reg.put(ClassType.builder()
                         .setId(TraceProvs.TRACEPROV_NAMESPACE + ".types.ClassType")
                         .build());
        reg.put(DateTimeType.of());
        reg.put(DictType.of());
        reg.put(DoubleType.of());
        reg.put(FloatType.of());
        reg.put(FunctionType.of());
        reg.put(IntType.of());
        reg.put(JavaDateType.of());
        reg.put(ListType.of());
        reg.put(LocalizedStringType.of());
        reg.put(LongType.of());
        reg.put(MapType.of());
        reg.put(NullType.of());
        reg.put(RefType.of());
        reg.put(SetType.of());
        reg.put(StringType.of());
        reg.put(TupleType.of());
        reg.put(UndefinedType.of());
        reg.put(TraceTypes.PUBLISHER_TYPE);
        return reg;
    }

    public ImmutableMap<String, TraceType> getTypes() {
        return ImmutableMap.copyOf(this.types);
    }

    /**
     * Returns the canincal TraceProv type of which the given {@obj} is the
     * instance.
     * 
     * @see #getTypesFromInstance(Object)
     * @throws TraceProvNotFoundException
     *             if {@code obj} cannot be associated to any registered Type
     */
    public TraceType getCanonicalTypeFromInstance(@Nullable Object obj) {

        if (obj == null) {
            return NullType.of();
        } else {
            return getCanonicalType(obj.getClass());
        }
    }

    /**
     * Returns possible types of which the given {@obj} can be an instance.
     * First type is the canonical one.
     */
    public ImmutableList<TraceType> getTypesFromInstance(@Nullable Object obj) {
        ImmutableList.Builder<TraceType> retb = ImmutableList.builder();

        for (TraceType t : this.types.values()) {
            if (t.isInstance(obj)) {
                retb.add(t);
            }
        }

        return retb.build();
    }

    /**
     * Returns true if provided object is instance of TraceType having given
     * typeid
     */
    public boolean isInstance(@Nullable Object obj, String typeId) {
        checkNotEmpty(typeId, "Invalid TraceType id!");
        for (TraceType t : types.values()) {
            if (t.isInstance(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * todo write about sharing todo ObjectMapper should be saved separately!
     */
    @JsonIgnore
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Returns a shallow copy of provided object. Always performs a deep copy, even
     * if the object is known to be immutable. As a consequence, this operation
     * can be quite expensive.
     */
    // todo currently it is not configurable.
    public <W> W shallowCopy(W obj) {
        return kryo.copyShallow(obj);
    }
    
    /**
     * Returns a deep copy of provided object. Always performs a deep copy, even
     * if the object is known to be immutable. As a consequence, this operation
     * can be quite expensive.
     */
    // todo currently it is not configurable.
    public <W> W deepCopy(W obj) {
        return kryo.copy(obj);
    }
}
