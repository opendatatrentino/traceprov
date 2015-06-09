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
package eu.trentorise.opendata.traceprov.schema;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import org.immutables.value.Value;

/**
 * Represent a reference to an element of a dcat dataset, like for example the
 * title
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
abstract class ADcatRef extends Ref {
    private static final long serialVersionUID = 1L;
    /**
     * Returns the URI of the class containing the referenced element (i.e.
     * {@link eu.trentorise.opendata.traceprov.dcat.ADcatDataset#CLASS_URI})
     */
    @Value.Default
    @Value.Parameter
    public String getClassUri() {
        return "";
    }

    /**
     * Let's say we are referring to a title of a dcat dataset, then we would
     * return {@link DcatRefs#DUBLIC_CORE_TERMS_TITLE}, as DCAT expresses
     * dataset title with a Dublin core vocabulary attribute
     */
    @Value.Default
    @Value.Parameter
    public String getPropertyUri() {
        return "";
    }
    

}
