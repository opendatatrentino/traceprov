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
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * {@link TraceFile} body node containing an array of other nodes.
 *
 * @author David Leoni
 */
public class DataArray extends DataNode implements  Iterable<DataNode> {

    private static final long serialVersionUID = 1L;
    private static final DataArray INSTANCE = new DataArray();
    private static final int MAX_PRINTED_NODES = 10;   

    private DataArray() {
        super(Ref.of(), NodeMetadata.of(), new ArrayList());        
    }

    private DataArray(Ref ref, NodeMetadata metadata,  Iterable<? extends DataNode> nodes) {
        super(ref, metadata, nodes);
        checkNotNull(nodes);
    }

    @Override
    public Iterator<DataNode> iterator() {
        return getValue().iterator();
    }

    /**
     * Returns the list of sub nodes.
     */
    @Override
    public Iterable<DataNode> getValue(){
        return (Iterable<DataNode>) super.getValue();
    }    
    
    public static DataArray of() {
        return INSTANCE;
    }

    public static DataArray of(Iterable<? extends DataNode> nodes) {
        return of(Ref.of(), NodeMetadata.of(), nodes);
    }

    public static DataArray of(DataNode... nodes) {
        return new DataArray(Ref.of(), NodeMetadata.of(), ImmutableList.copyOf(nodes));
    }

    public static DataArray of(Ref ref, NodeMetadata metadata, Iterable<? extends DataNode> nodes) {
        return new DataArray(ref, metadata, nodes);
    }
        
    public static DataArray of(Ref ref, DataNode... nodes) {
        return new DataArray(ref, NodeMetadata.of(), Arrays.asList(nodes));
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (DataNode n : getValue()){
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
    public void accept(DataVisitor visitor, DataNode parent, String field, int pos) {
        int i = 0;
        Iterator<DataNode> iter = getValue().iterator();

        while (iter.hasNext()) {
            DataNode node = iter.next();
            node.accept(visitor, this, field, i);
            i++;
        }

        visitor.visit((DataArray) this, parent, field, pos);

    }

    @Override
    public Object asSimpleType(TypeRegistry typeRegistry) {
        SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);
        accept(tran, DataValue.of(), "", 0);
        return tran.getResult();
    }


    
    
}
