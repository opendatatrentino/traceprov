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
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * The schema of a json, loosely modeled after what you can express with
 * a jsonld context and ATypescript.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = ClassDef.class)
@JsonDeserialize(as = ClassDef.class)
abstract class AClassDef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The id of the type, which may be an IRI to a well-known type like i.e.
     * https://schema.org/Person
     */
    @Value.Default
    public String getId() {
        return "";
    }

   /**
     * The high-level concept describing the schema. If unknown,
     * {@link Concept#of()} will be used.
     *
     */
    @Value.Default
    public Concept getConcept() {
        return Concept.of();
    }
    
    /**
     * Name of the type preferably in English and camelcased, i.e. CreativeWork,
     * BroadcastService
     */
    @Value.Default
    public String getName() {
        return "";
    }

    /**
     * The property definitions of the schema
     */
    public abstract List<PropertyDef> getPropertyDefs();

    /**
     * The unique indexes tht may constrain sets of schema values to be unique
     * in the array they are in.
     *
     */
    public abstract List<UniqueIndex> getUniqueIndexes();

 

}
