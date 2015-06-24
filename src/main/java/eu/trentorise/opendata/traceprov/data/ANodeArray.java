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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.schema.DocRef;
import eu.trentorise.opendata.traceprov.schema.ARef;
import java.util.List;
import org.immutables.value.Value;


/**
 * {@link ProvFile} body node containing an array of other nodes.
 * @author David Leoni 
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as=NodeArray.class)
@JsonDeserialize(as=NodeArray.class)
public abstract class ANodeArray extends ANode {

    private static final long serialVersionUID = 1L;
    
    @Value.Default    
    @Override
    public ARef getProvenance(){
        return DocRef.of();
    }           
    
    /**
     * Returns the elements of the array
     */    
    @Value.Default
    public  List<? extends ANode> getElements(){
        return ImmutableList.of();
    }
}
