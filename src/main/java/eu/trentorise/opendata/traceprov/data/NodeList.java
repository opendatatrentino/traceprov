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

import com.google.common.collect.ImmutableList;
import static com.google.common.base.Preconditions.checkNotNull;

import eu.trentorise.opendata.commons.validation.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * {@link ProvFile} body node containing an array of other nodes.
 *
 * @author David Leoni
 */
public class NodeList extends ANode implements  Iterable<ANode> {

    private static final long serialVersionUID = 1L;
    private static final NodeList INSTANCE = new NodeList();
    private static final int MAX_PRINTED_NODES = 10;

    

    private NodeList() {
        super(Ref.of(), NodeMetadata.of(), new ArrayList());        
    }

    private NodeList(Ref ref, NodeMetadata metadata,  Iterable<? extends ANode> nodes) {
        super(ref, metadata, nodes);
        checkNotNull(nodes);
    }

    @Override
    public Iterator<ANode> iterator() {
        return getData().iterator();
    }

    /**
     * Returns the list of sub nodes.
     */
    @Override
    public Iterable<ANode> getData(){
        return (Iterable<ANode>) super.getData();
    }    
    
    public static NodeList of() {
        return INSTANCE;
    }

    public static NodeList of(Iterable<? extends ANode> nodes) {
        return of(Ref.of(), NodeMetadata.of(), nodes);
    }

    public static NodeList of(ANode... nodes) {
        return new NodeList(Ref.of(), NodeMetadata.of(), ImmutableList.copyOf(nodes));
    }

    public static NodeList of(Ref ref, NodeMetadata metadata, Iterable<? extends ANode> nodes) {
        return new NodeList(ref, metadata, nodes);
    }
        
    public static NodeList of(Ref ref, ANode... nodes) {
        return new NodeList(ref, NodeMetadata.of(), Arrays.asList(nodes));
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ANode n : getData()){
            if (i > 0){
                sb.append(", ");
            }            
            if (i < MAX_PRINTED_NODES){
                sb.append(n.toString());
            } else {
                sb.append("...");
                break;                
            }
            i++;
        }
        sb.append("]");
        return "NodeList{ref=" + getRef() +  ", metadata=" + getMetadata() + ", data=" + sb.toString()  + '}';
    }
   
    
    @Override
    public void accept(INodeVisitor visitor, ANode parent, String field, int pos) {
        int i = 0;
        Iterator<ANode> iter = getData().iterator();

        while (iter.hasNext()) {
            ANode node = iter.next();
            node.accept(visitor, this, field, i);
            i++;
        }

        visitor.visit((NodeList) this, parent, field, pos);

    }

    @Override
    public Object asSimpleType() {
        SimpleMapTransformer tran = new SimpleMapTransformer();
        accept(tran, NodeValue.of(), "", 0);
        return tran.getResult();
    }


    
    
}
