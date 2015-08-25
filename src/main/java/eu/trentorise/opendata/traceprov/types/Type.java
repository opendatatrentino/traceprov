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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.trentorise.opendata.commons.Dict;
import java.io.Serializable;
import java.util.Locale;

/**
 *
 * A type expression, including basic data types like Int, boolean, etc. Complex
 * ones are defined with {@link ClassType}.
 *
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Type implements Serializable {

    /**
     *
     * todo review
     * <h3> Nulls </h3>
     * In data we only support 'null', finding 'undefined' in json data would be
     * considered an error.
     *
     * We postulate existance of Null datatype even if users cannot declare it
     * as a type, (this is the same behaviour of Typescript). This is because
     * every object can be null (even primitive datatypes).
     *
     * <h3> Grammar </h3>
     * <pre>
     *      Dict -> language dict
     *      CanonicalName -> camel cased english name
     *
     * PrimitiveType ->
     *        String  with regex
     *      | Int
     *      | Long
     *      | Float
     *      | Double
     *      | Date
     *
     * Type ->
     *        PrimitiveType
     *      | TypeId
     *      | List<Type>
     *      | Map<String | Int, Type>
     * </pre>
     */
    /**
     * <pre>
     *      JSON-LD CONTEXT ->
     *             Type -> Id,
     * Concept,
     * CanonicalName,
     * name : Dict,
     * description : Dict,
     * PropertyDef[]
     *
     * PropertyDef -> Id, Concept, CanonicalName, Name, Type
     *
     * </pre>
     *
     * Although Json-ld always allow inserting multiple values (because of the
     * open world assumption), we demand multiple values must be explicitly
     * declared. todo review this
     *
     */
    protected Type() {

    }

    /**
     *
     * A string with the fully qualified name valid as unique identifier for
     * traceprov operations. The namespace must be suitable use in programming
     * languages, for example "eu.trentorise.opendata.traceprov.types.IntType"
     */
    public String getId() {
        return this.getClass().getCanonicalName();
    }

    /**
     * Allows traversing the type tree with a {@link TypeVisitor}.
     *
     * <p>
     * Calls {@code accept} on subnodes and then {@code visitor.visit} on this
     * node. todo this should be abstract!</p>
     */
    public void accept(TypeVisitor v) {
        v.visit(this);
    }

    /**
     * The metadata associated to the type expression when it is defined, i.e.
     * the name, description, provenance...
     */
    public DefMetadata getMetadata() {
        return DefMetadata.builder()
                .setConcept(Concept.of())
                .setOriginId(getId())
                .setName(Dict.of(Locale.ENGLISH, this.getClass().getSimpleName()))
                .build();
    }

    /**
     * The canonical Java class to represent instances of the type. Other
     * classes may be added with type converters todo define better
     */
    public abstract Class getJavaClass();

    /**
     * Returns whether or not the given {
     *
     * @object} is an instance of this type.
     */
    public boolean isInstance(Object object) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * todo review
     *
     * Returns whether or not the given {@code object} is to be considered empty
     * (i.e. empty string, empty list, Person with only string fields which are
     * all empty....)
     *
     * @throws IllegalArgumentException if {@code object} is not an instance of
     * this type (that is,
     * {@link #isInstance(java.lang.Object) isInstance(object)} returns false)
     */
    public boolean isEmpty(Object object) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * todo review
     *
     * Returns whether or not the given {@code object} is to be considered
     * degenerate (i.e. empty string, string with only spaces, empty list,
     * Person with only string fields which only have spaces......)
     *
     * @throws IllegalArgumentException if {@code object} is not an instance of
     * this type (that is,
     * {@link #isInstance(java.lang.Object) isInstance(object)} returns false)
     */
    public boolean isDegenerate(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * todo review
     *
     * Returns whether or not the given {@code object} is to be considered dirty
     * (i.e. string containing the word "null", string with different encodings
     * within....)
     *
     * @throws IllegalArgumentException if {@code object} is not an instance of
     * this type (that is,
     * {@link #isInstance(java.lang.Object) isInstance(object)} returns false)
     */
    public boolean isDirty(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    /*
     * *
     *
     * @param datatype a dataType as defined in
     * {@link eu.trentorise.opendata.opendatarise.types.OdrDataTypes}.
     * @param list
     * @param etypeURL an entity type URL in case the dataType is either a
     * STRUCTURE or an ENTITY. Otherwise, it must be empty.
     * @param etypeName the name of the etype in case the data either a
     * STRUCTURE or an ENTITY. Otherwise, it must be {@link Dict#of()}.
     
     public OdrType(String datatype, boolean list, String etypeURL, Dict etypeName) {
        
     } */
}
