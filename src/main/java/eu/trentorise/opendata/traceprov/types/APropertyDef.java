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
import org.immutables.value.Value;

/**
 * Definition of a property that can be found in a json-ld file.
 *
 * !! TODO !! WHAT ABOUT THE TYPE? CAN IT BE A STRUCTURE? (i.e. opening hour)
 * CAN IT BE AN URL POINTING TO SOME TYPE? (that is, a relational type) CAN IT
 * BE A UNION TYPE? i.e. Number | null REGEX SUPPORT? i.e. for dates
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = PropertyDef.class)
@JsonDeserialize(as = PropertyDef.class)
abstract class APropertyDef {

    /**
     * The id of the property, which may be an IRI to a well-known property like
     * i.e.
     * <a href="http://schema.org/name" target="_blank">http://schema.org/name</a>
     */
    @Value.Default
    public String getId() {
        return "";
    }

    /**
     * Human readable property name as found in json files.
     */
    @Value.Default
    public Dict getName() {
        return Dict.of();
    }

    /**
     * The high-level concept describing the property. If unknown,
     * {@link Concept#of()} is used.
     *
     */
    @Value.Default
    public Concept getConcept() {
        return Concept.of();
    }

    /**
     * The type signature of the property. If unknown, {@link AnyType.of()} is
     * used.
     */
    @Value.Default
    public Type getType() {
        return AnyType.of();
    }

}
