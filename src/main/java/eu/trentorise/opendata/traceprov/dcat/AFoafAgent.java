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

import eu.trentorise.opendata.commons.Dict;

import eu.trentorise.opendata.commons.BuilderStyle;
import org.immutables.value.Value;

/**
 * Models a minimal FOAF Agent: http://xmlns.com/foaf/spec/
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStyle
public abstract class AFoafAgent {

    public static final String CLASS_URI="http://xmlns.com/foaf/0.1/Agent";
    
    
    /**
     * Returns the uri of the agent.
     */
    @Value.Default
    public String getUri() {
        return "";
    }

    /**
     * http://xmlns.com/foaf/0.1/name
     */
    @Value.Parameter
    @Value.Default
    public Dict getName(){
        return Dict.of();
    };

    /**
     * Returns the mail box http://xmlns.com/foaf/0.1/mbox
     */
    @Value.Default
    @Value.Parameter
    public String getMbox() {
        return "";
    }

}
