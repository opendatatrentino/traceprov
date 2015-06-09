/* 
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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
package eu.trentorise.opendata.traceprov.dcat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import org.immutables.value.Value;


/**
 * Models a <a href="http://xmlns.com/foaf/0.1/Person" target="_blank"> foaf:Person </a>
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as=FoafPerson.class)
@JsonDeserialize(as=FoafPerson.class)
 abstract class AFoafPerson extends AFoafAgent {
    public static final String CLASS_URI="http://xmlns.com/foaf/0.1/Person";
    
    private static final long serialVersionUID = 1L;

}