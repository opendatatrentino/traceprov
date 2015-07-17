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

import com.fasterxml.jackson.annotation.JsonTypeInfo;

// todo check this Id.NAME is correct
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class AType {

    /**
     * <h3> Nulls </h3>
     * In data we only support 'null', finding 'undefined' in json data would be
     * considered an error.
     *
     * We postulate existance of Null datatype even if users cannot declare it
     * as a type, (this is the same behaviour of ATypescript). This is because
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
     * AType ->
     *        PrimitiveType
     *      | ATypeId
     *      | List<Type>
     *      | Map<String | Int, AType>
     * </pre>
     */
    /**
     * <pre>
     *      JSON-LD CONTEXT ->
     *             AType -> Id,
     * Concept,
     * CanonicalName,
     * name : Dict,
     * description : Dict,
     * PropertyDef[]
     *
     * PropertyDef -> Id, Concept, CanonicalName, Name, AType
     *
     * </pre>
     */
    /**
     * Although Json-ld always allow inserting multiple values (because of the
     * open world assumption), we demand multiple values must be explicitly
     * declared. todo review this
     *
     */
    AType() {

    }

    /**
     * Returns the complete expanded IRI of the datatype.
     */
    public abstract String datatypeId();

    public boolean isInstance(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    public boolean isEmpty(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    public boolean isDegenerate(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

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
