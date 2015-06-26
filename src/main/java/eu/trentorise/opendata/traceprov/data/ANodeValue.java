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
import com.google.common.base.Preconditions;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.schema.DocRef;
import eu.trentorise.opendata.traceprov.schema.ARef;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * Node containing a ground json-compatible value, like
 * null, number or String.
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = NodeValue.class)
@JsonDeserialize(as = NodeValue.class)
abstract class ANodeValue implements INode {

    private static final long serialVersionUID = 1L;

    @Value.Default
    @Override
    public ARef getProvenance() {
        return DocRef.of();
    }            
            
    /**
     * A ground json-compatible value, like null, number, string.     
     */    
    @Nullable
    public abstract Object getValue();
    
        @Value.Check
     protected void check() {
       Preconditions.checkState(getValue() == null || getValue() instanceof Number || getValue() instanceof String);
     }   
}
