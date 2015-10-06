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

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import javax.annotation.Nullable;

/**
 * Node containing a ground json-compatible value, like null, number or String.
 *
 * @author David Leoni
 */
public class DataValue extends DataNode {

    private static final long serialVersionUID = 1L;
    private static final DataValue INSTANCE = new DataValue();
    
    private DataValue(){
        super();
    }
    
    private DataValue(Ref ref, NodeMetadata nodeMetadata, @Nullable Object value){
        super(ref, nodeMetadata, value);
        Preconditions.checkArgument(value == null || value instanceof Number || value instanceof String);
    }
    
    

    /**
     * A ground json-compatible value, like null, number, string.
     */
    @Nullable
    @Override
    public Object getValue(){
        return super.getValue();
    }

    
    @Override
    public void accept(DataVisitor visitor, DataNode parent, String field, int pos) {
        visitor.visit((DataValue) this, parent, field, pos);
    }

    @Override
    public Object asSimpleType(TypeRegistry typeRegistry) {
        SimpleMapTransformer tran = new SimpleMapTransformer(typeRegistry);        
        accept(tran, DataValue.of(), "",0);        
        return tran.getResult();
    }

    /**
     * Construct new immutable {@code DataValue} instance.
     *
     * @param value a String, a Number or null
     */
    public static DataValue of(@Nullable Object value) {
        return new DataValue(Ref.of(), NodeMetadata.of(), value);
    }
    
    /**
     * Construct new immutable {@code DataValue} instance.
     *
     * @param value a String, a Number or a null
     * @param provenance a reference to the provenance. If unknown, use {@link DocRef#of()}
     */
    public static DataValue of(Ref provenance, NodeMetadata metadata, @Nullable Object value) {
        return new DataValue(Ref.of(), metadata, value);
    }    
    
    /**
     * Returns default instance.
     *     
     */
    public static DataValue of() {
        return INSTANCE;
    }
   
    @Override
    public String toString() {
        return "DataValue{" + "ref=" + getRef() + ", metadata=" + getMetadata() + ", data=" + getValue() + '}';
    }
    
}
