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

import eu.trentorise.opendata.commons.validation.Ref;
import java.util.HashMap;
import java.util.Map;



/** 
 * A data node containing a map of fields to other nodes.
 * @author David Leoni 
*/

public class NodeMap extends ANode  {

    private static final NodeMap INSTANCE = new NodeMap();
    
    private static final long serialVersionUID = 1L;    
    
    private NodeMap(){
        super(Ref.of(), NodeMetadata.of(), new HashMap());
        
    }

    private NodeMap(Ref ref, NodeMetadata metadata,  Map<String, ? extends ANode> nodes) {
        super(ref, metadata, nodes);
        checkNotNull(nodes);                
    }
                 
    public static NodeMap of(){
        return INSTANCE;
    }
    
    public static NodeMap of(Ref ref, NodeMetadata metadata, Map<String, ? extends  ANode> nodes){
       return new NodeMap(ref, metadata, nodes);
    }
    
    @Override
    public Map<String, ? extends ANode> getData(){
        return (Map<String, ? extends ANode>) super.getData();
    }

        
    @Override
    public void accept(INodeVisitor visitor, ANode parent, String field, int pos){
        for (Map.Entry<String, ? extends ANode> entry : getData().entrySet()){
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
