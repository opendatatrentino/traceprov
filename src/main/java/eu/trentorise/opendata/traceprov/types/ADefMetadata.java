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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.validation.Ref;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as=DefMetadata.class)
@JsonDeserialize(as=DefMetadata.class)
abstract class ADefMetadata implements Serializable {
    
    private static final long serialVersionUID = 1L;    
    
    /**
     * A reference to a document and position where the definition is located
     */
    @Value.Default
    public Ref getRef(){
        return Ref.of();
    }
    
    /**
     * The id of the object being defined as it is identified on the system of
     * origin. Since this could be arbitrary it may or not be an IRI and may be
     * non human readable, i.e. http://mycompany.com/types/3867
     *
     */
    @Value.Default
    public String getOriginId() {
        return "";
    }

    /**
     *
     * The high-level most standard concept that most closely describes the
     * object being defined. For example, it could be a Dublin core class or attribute.
     *
     * The concept id should be the complete expanded IRI of a standard datatype
     * that can represent faithfully this one, for example for integers it could
     * be "http://www.w3.org/2001/XMLSchema#int"
     *
     * @see #getId()
     *
     */
    @Value.Default
    public Concept getConcept() {
        return Concept.of();
    }

    /**
     * The human readable name of the object being defined (may contain spaces), i.e. 'Names of persons'
     */
    @Value.Default
    public Dict getName() {
        return Dict.of();
    }

    /**
     * The description of the object being defined, i.e. 'A list of non-null non-empty person names. '. Should basically be the same as the javadoc.
     */
    @Value.Default
    public Dict getDescription() {
        return Dict.of(); 
    }
}
