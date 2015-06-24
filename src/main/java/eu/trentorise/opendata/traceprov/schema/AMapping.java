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
package eu.trentorise.opendata.traceprov.schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.SimpleStyle;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * A mapping from an element in a source file to a target schema attribute path.
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as=Mapping.class)
@JsonDeserialize(as=Mapping.class)
abstract class AMapping implements Serializable {
        private static final long serialVersionUID = 1L;
        
    @Value.Default
    @Value.Parameter    
    public ARef getSourceRef(){
        return DocRef.of();
    };
    
    @Value.Default
    @Value.Parameter    
    public SchemaRef getTargetRef(){
        return SchemaRef.of();
    };
}