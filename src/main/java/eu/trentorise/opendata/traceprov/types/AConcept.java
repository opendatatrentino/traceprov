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
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.SimpleStyle;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * A high level concept. Examples could be Dublin core terms or WordNet synsets.
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = Concept.class)
@JsonDeserialize(as = Concept.class)
abstract class AConcept implements Serializable{
    
    private static final long serialVersionUID = 1L;
    /**
     * The id of the concept, which could possibly be an IRI. 
     */
    @Value.Default
    public String getId() {
        return "";
    }
    
    /**
     * The name of the concept, or {@link dict.of()} if unknown.
     */
    @Value.Default
    public Dict getName() {
        return Dict.of();
    }

    /**
     * Gets the description of the concept, or {@link dict.of()} if unknown.
     *
     */
    @Value.Default
    public Dict getDescription() {
        return Dict.of();
    }
}
