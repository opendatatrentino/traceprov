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
package eu.trentorise.opendata.traceprov.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.schema.ARef;
import eu.trentorise.opendata.traceprov.schema.DocRef;
import java.util.Map;
import org.immutables.value.Value;

/** 
 * {@link ProvFile} body node containing a map of fields to other nodes.
 * @author David Leoni 
*/
@Value.Immutable
@SimpleStyle
@JsonSerialize(as=NodeMap.class)
@JsonDeserialize(as=NodeMap.class)
abstract class ANodeMap extends ANode {

    private static final long serialVersionUID = 1L;
    
    @Value.Default
    @Override
    public ARef getProvenance(){
        return DocRef.of();
    }
    
    /**
     * Returns the elements of the map.
     */
    @Value.Default
    public Map<String, ? extends ANode> getElements(){
        return ImmutableMap.of();
    }
}
