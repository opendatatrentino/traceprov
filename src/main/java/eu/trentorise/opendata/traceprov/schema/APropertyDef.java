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
package eu.trentorise.opendata.traceprov.schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.SimpleStyle;
import org.immutables.value.Value;


/**
 * Definition of a property that can be found in a json-ld file.
 *
 *  !! TODO !!  WHAT ABOUT THE TYPE? 
 * CAN IT BE A STRUCTURE? (i.e. opening hour)
 * CAN IT BE AN URL POINTING TO SOME TYPE? (that is, a relational type)
 * CAN IT BE A UNION TYPE? i.e.   Number | null
 * REGEX SUPPORT? i.e. for dates
 * 
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = PropertyDef.class)
@JsonDeserialize(as = PropertyDef.class)
abstract class APropertyDef {
        
    /**
     * The id of the property, which may be an IRI to a well-known property like i.e.     
     * <a href="http://schema.org/name" target="_blank">http://schema.org/name</a>
     */
    @Value.Default
    @Value.Parameter
    public String getId() {
        return "";
    }

    /**
     * Human readable property name as found in json files.
     */
    @Value.Default
    @Value.Parameter
    public String getName() {
        return "";
    }

    /**
     * Although Json-ld always allow inserting multiple values (because of the
     * open world assumption), we demand multiple values must be explicitly
     * declared. todo review this
     *
     */
    @Value.Default
    @Value.Parameter
    public boolean isList() {
        return false;
    }

}
