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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.Dict;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * Models <a href="http://xmlns.com/foaf/spec/" target="_blank">a minimal FOAF
 * Agent</a>
 *
 * @author David Leoni
 */
 @JsonTypeInfo(
	use = JsonTypeInfo.Id.CLASS,
	include = JsonTypeInfo.As.PROPERTY, 
	property = "@type") 
public abstract class AFoafAgent implements Serializable {

    public static final String CLASS_URI = "http://xmlns.com/foaf/0.1/Agent";

    private static final long serialVersionUID = 1L;


    /**
     *  <a href="http://xmlns.com/foaf/0.1/mbox" target="_blank">the mail
     * box </a> of the agent.
     */
    @Value.Default
    public String getMbox() {
        return "";
    }

    /**
     * <a href="http://xmlns.com/foaf/0.1/homepage" target="_blank">the
     * homepage </a> of the agent
     */
    @Value.Default
    public String getHomepage() {
        return "";
    }

    /**
     * Returns <a href="http://xmlns.com/foaf/0.1/name" target="_blank">the
     * agent name </a>
     */
    @Value.Default
    public Dict getName() {
        return Dict.of();
    }

    /**
     * The uri of the agent.
     */
    @Value.Default
    public String getUri() {
        return "";
    }

}
