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
import eu.trentorise.opendata.traceprov.schema.DocRef;
import eu.trentorise.opendata.traceprov.schema.ARef;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Node containing a ground json-compatible value, like null, number or String.
 *
 * @author David Leoni
 */
public class NodeValue implements INode {

    private static final long serialVersionUID = 1L;
    private static final NodeValue INSTANCE = new NodeValue();

    private ARef provenance;
    private Object value;
    
    private NodeValue(){
        this.provenance = DocRef.of();
        this.value = null;
    }
    
    private NodeValue(ARef provenance, @Nullable Object value){
        checkNotNull(provenance);
        Preconditions.checkArgument(value == null || value instanceof Number || value instanceof String);
        this.provenance = provenance;
        this.value = value;
    }
    
    
    @Override
    public ARef getProvenance() {
        return provenance;
    }

    /**
     * A ground json-compatible value, like null, number, string.
     */
    @Nullable
    public Object getValue(){
        return value;
    }

    
    @Override
    public void accept(INodeVisitor visitor, INode parent, String field, int pos) {
        visitor.visit((NodeValue) this, parent, field, pos);
    }

    @Override
    public Object asSimpleType() {
        SimpleMapTransformer tran = new SimpleMapTransformer();        
        accept(tran, NodeValue.of(), "",0);        
        return tran.getResult();
    }

    /**
     * Construct new immutable {@code NodeValue} instance using defualt
     * provenance {@link DocRef#of()}
     *
     * @param value a String, a Number or a null
     */
    public static NodeValue of(@Nullable Object value) {
        return new NodeValue(DocRef.of(), value);
    }
    
    /**
     * Construct new immutable {@code NodeValue} instance.
     *
     * @param value a String, a Number or a null
     * @param provenance a reference to the provenance. If unknown, use {@link DocRef#of()}
     */
    public static NodeValue of(ARef provenance, @Nullable Object value) {
        return new NodeValue(DocRef.of(), value);
    }    
    
    /**
     * Returns default instance.
     *     
     */
    public static NodeValue of() {
        return INSTANCE;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.provenance);
        hash = 37 * hash + Objects.hashCode(this.value);
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
        final NodeValue other = (NodeValue) obj;
        if (!Objects.equals(this.provenance, other.provenance)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NodeValue{" + "provenance=" + provenance + ", value=" + value + '}';
    }
    
    
}
