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
import java.util.Objects;


/** 
 * A data node containing a map of fields to other nodes.
 * @author David Leoni 
*/

public class NodeMap implements INode  {

    private static final NodeMap INSTANCE = new NodeMap();
    
    private static final long serialVersionUID = 1L;
    private Map<String,? extends INode> nodes;
    private ARef provenance;
    
    private NodeMap(){
        this.nodes = new HashMap();
        this.provenance = DocRef.of();
    }

    public NodeMap(ARef provenance, Map<String, ? extends INode> nodes) {
        checkNotNull(provenance);
        checkNotNull(nodes);        
        this.provenance = provenance;
        this.nodes = nodes;
    }
                 
    public static NodeMap of(){
        return INSTANCE;
    }
    
    public static NodeMap of(ARef provenance, Map<String, ? extends  INode> nodes){
       return new NodeMap(provenance, nodes);
    }
    
    public Map<String, ? extends INode> getValues(){
        return nodes;
    }

    @Override
    public ARef getProvenance() {
        return provenance;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nodes);
        hash = 37 * hash + Objects.hashCode(this.provenance);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodeMap other = (NodeMap) obj;
        if (!Objects.equals(this.nodes, other.nodes)) {
            return false;
        }
        if (!Objects.equals(this.provenance, other.provenance)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NodeMap{" + "nodes=" + nodes + ", provenance=" + provenance + '}';
    }

        
    @Override
    public void accept(INodeVisitor visitor, INode parent, String field, int pos){
        for (Map.Entry<String, ? extends INode> entry : nodes.entrySet()){
            entry.getValue().accept(visitor, this, entry.getKey(), 0);
        }
        visitor.visit(this, parent, field, pos);
    };    
    
    
    @Override
    public Object asSimpleType() {
        SimpleMapTransformer tran = new SimpleMapTransformer();        
        accept(tran, NodeValue.of(), "",0);        
        return tran.getResult();
    }
}
