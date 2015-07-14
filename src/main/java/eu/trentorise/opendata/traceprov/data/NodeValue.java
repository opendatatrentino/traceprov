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

import com.google.common.base.Preconditions;
import static com.google.common.base.Preconditions.checkNotNull;

import eu.trentorise.opendata.commons.validation.Ref;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Node containing a ground json-compatible value, like null, number or String.
 *
 * @author David Leoni
 */
public class NodeValue extends ANode {

    private static final long serialVersionUID = 1L;
    private static final NodeValue INSTANCE = new NodeValue();
    
    private NodeValue(){
        super();
    }
    
    private NodeValue(Ref ref, NodeMetadata nodeMetadata, @Nullable Object value){
        super(ref, nodeMetadata, value);
        Preconditions.checkArgument(value == null || value instanceof Number || value instanceof String);
    }
    
    

    /**
     * A ground json-compatible value, like null, number, string.
     */
    @Nullable
    @Override
    public Object getData(){
        return super.getData();
    }

    
    @Override
    public void accept(INodeVisitor visitor, ANode parent, String field, int pos) {
        visitor.visit((NodeValue) this, parent, field, pos);
    }

    @Override
    public Object asSimpleType() {
        SimpleMapTransformer tran = new SimpleMapTransformer();        
        accept(tran, NodeValue.of(), "",0);        
        return tran.getResult();
    }

    /**
     * Construct new immutable {@code NodeValue} instance.
     *
     * @param value a String, a Number or null
     */
    public static NodeValue of(@Nullable Object value) {
        return new NodeValue(Ref.of(), NodeMetadata.of(), value);
    }
    
    /**
     * Construct new immutable {@code NodeValue} instance.
     *
     * @param value a String, a Number or a null
     * @param provenance a reference to the provenance. If unknown, use {@link DocRef#of()}
     */
    public static NodeValue of(Ref provenance, NodeMetadata metadata, @Nullable Object value) {
        return new NodeValue(Ref.of(), metadata, value);
    }    
    
    /**
     * Returns default instance.
     *     
     */
    public static NodeValue of() {
        return INSTANCE;
    }
   
    @Override
    public String toString() {
        return "NodeValue{" + "ref=" + getRef() + ", metadata=" + getMetadata() + ", data=" + getData() + '}';
    }
    
}
