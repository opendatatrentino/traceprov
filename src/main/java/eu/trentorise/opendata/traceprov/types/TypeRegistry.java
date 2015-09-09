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
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Todo this is much in WTF status
 *
 * @author David Leoni
 */
public class TypeRegistry {

    private Map<String, Type> types;

    private Map<String, Def<ClassType>> classDefs;

    private TypeRegistry() {
        this.types = new HashMap();
        this.classDefs = new HashMap();
    }

    /**
     * @return The previous value associated with key, or null if there was no
     * mapping for key.
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
     * @throws eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException
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
     * 
     * @return The previous value associated with key, or null if there was no
     * mapping for key.
     */
    @Nullable
    public Type put(Type type) {
        checkNotNull(type);
        checkNotEmpty(type.getId(), "Invalid type datatype id!");
        Type ret = this.types.get(type.getId());
        this.types.put(type.getId(), type);
        return ret;
    }

    
    public boolean hasType(Type type) {
        throw new UnsupportedOperationException("todo implement me!");
    }
    
    public boolean hasDatatype(String datatypeId) {
        checkNotEmpty(datatypeId, "Invalid type id!");
        return !(types.get(datatypeId) == null);
    }

    /**
     * Returns the default instance of a given datatype id
     * @throws eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException
     */
    public Type getDatatype(String datatypeId) {
        checkNotEmpty(datatypeId, "Invalid datatype id!");
        Type type = types.get(datatypeId);
        if (type == null) {
            throw new TraceProvNotFoundException("Couldn't find datatypeId " + datatypeId);
        }
        return type;
    }

    Type guessType(Object obj) {
        throw new UnsupportedOperationException("todo implement me");
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
        TypeRegistry reg = new TypeRegistry();
        reg.put(AnyType.of());
        reg.put(BooleanType.of());
        reg.put(ClassType.of());
        reg.put(DateTimeType.of());
        reg.put(DictType.of());
        reg.put(DoubleType.of());
        reg.put(FloatType.of());
        reg.put(FunctionType.of());
        reg.put(RefType.of());
        reg.put(IntType.of());
        reg.put(JavaDateType.of());
        reg.put(ListType.of());
        reg.put(LongType.of());
        reg.put(MapType.of());
        reg.put(NullType.of());
        reg.put(StringType.of());
        reg.put(TupleType.of());
        reg.put(UndefinedType.of());
        return reg;
    }

}
