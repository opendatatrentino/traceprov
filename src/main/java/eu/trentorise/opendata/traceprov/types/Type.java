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

/**
 * todo think about originId
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
     * Returns the fully qualified java namespace of the datatype WITH the
     * generics type information, like
     * {@code eu.trentorise.opendata.traceprov.types.MapType<StringType, IntegerType>}
     *
     */    
    public String getId() {
        return this.getClass().getCanonicalName();
    }

    /**
     * The id of the type being defined as it is identified on the system of
     * origin. Since this could be arbitrary it may or not be an IRI and may be
     * non human readble, i.e. http://mycompany.com/types/3867
     *
     */    
    public String getOriginId() {
        return getId(); // todo check/document default
    }

    /**
     *
     * The high-level most standard concept that most closely describes the
     * datatype. For example, it could be a Dublin core class or attribute.
     *
     * The concept id should be the complete expanded IRI of a standard datatype
     * that can represent faithfully this one, for example for integers it could
     * be "http://www.w3.org/2001/XMLSchema#int"
     *
     * @see #getId()
     *
     */
    public Concept getConcept(){
        return Concept.of();
    } 

    /**
     * The human readable name of the datatype (may contain spaces), i.e. 'List
     * of strings'
     */
    public Dict getName() {
        return Dict.of(this.getClass().getSimpleName());
    }

    /**
     * The description of the datatype, i.e. 'A finite list of objects of a
     * given type'. Should basically be the same as the javadoc.
     */
    public Dict getDescription() {
        return Dict.of(""); // todo returning the javadoc would be nice, although not easy (reflection is not enough for this)
    }

    /**
     * Returns the canonical Java class to represent instances of the type.
     * Other classes may be added with type converters todo define better
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
