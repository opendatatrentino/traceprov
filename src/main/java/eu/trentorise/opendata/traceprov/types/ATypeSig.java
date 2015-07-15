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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import org.immutables.value.Value;

@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = TypeSig.class)
@JsonDeserialize(as = TypeSig.class)
abstract class ATypeSig {
    
    /*
        Dict -> language dict
        CanonicalName -> camel cased english name
        TypeSig ->  Null                     
                    | String                     
                    | Int    
                    | Long
                    | Float
                    | Double
                    | Date
                    | Class
                    | ClassId
                    | TypeSig[]
        Class -> Id, 
                 Concept, 
                 CanonicalName, 
                 name : Dict, 
                 description : Dict, 
                 PropertyDef[]
    
        PropertyDef -> Id, Concept, CanonicalName, Name, TypeSig
    */
    
    /**
     * Although Json-ld always allow inserting multiple values (because of the
     * open world assumption), we demand multiple values must be explicitly
     * declared. todo review this
     *
     */
    @Value.Default
    public boolean isList() {
        return false;
    }
    
    @Value.Default
    public boolean getDatatype() {
        return false;
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