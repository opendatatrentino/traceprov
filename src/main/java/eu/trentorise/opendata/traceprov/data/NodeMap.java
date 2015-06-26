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

import static com.google.common.base.Preconditions.checkNotNull;
import eu.trentorise.opendata.traceprov.schema.ARef;
import eu.trentorise.opendata.traceprov.schema.DocRef;
import java.util.HashMap;
import java.util.Map;


/** 
 * A data node containing a map of fields to other nodes.
 * @author David Leoni 
*/

public class NodeMap implements INode  {

    private static final NodeMap INSTANCE = new NodeMap();
    
    private static final long serialVersionUID = 1L;
    private Map<String, INode> values;
    private ARef provenance;
    
    private NodeMap(){
        this.values = new HashMap();
        this.provenance = DocRef.of();
    }

    public NodeMap(ARef provenance, Map<String, INode> values) {        
        checkNotNull(provenance);
        checkNotNull(values);        
        this.provenance = provenance;
        this.values = values;
    }
    
         
    
    public static NodeMap of(){
        return INSTANCE;
    }
    
    public static NodeMap of(ARef provenance, Map<String, INode> values){
       return new NodeMap(provenance, values);
    }
    
    Map<String, INode> getValues(){
        return values;
    }

    @Override
    public ARef getProvenance() {
        return provenance;
    }
}
