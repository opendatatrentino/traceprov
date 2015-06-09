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
 * Models a
 * <a href="http://www.w3.org/2009/08/skos-reference/skos.html#ConceptScheme">SKOS
 * ConceptScheme</a>
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as=SkosConceptScheme.class)
@JsonDeserialize(as=SkosConceptScheme.class)
abstract class ASkosConceptScheme {

    public static final String CLASS_URI="http://www.w3.org/2009/08/skos-reference/skos.html#ConceptScheme";
    
    private static final long serialVersionUID = 1L;
    
    /**
     * skos:prefLabel i.e. "A set of domains to classify documents"
     */
    @Value.Default
    @Value.Parameter
    public String getPrefLabel() {
        return "";
    }

    @Value.Default
    @Value.Parameter
    public String getUri() {
        return "";
    }

}
