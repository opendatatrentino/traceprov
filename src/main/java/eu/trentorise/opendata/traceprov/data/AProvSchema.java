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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.schema.Schema;
import eu.trentorise.opendata.traceprov.services.ValidationError;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * Tree-like generic data model to represent a file and tracking from where data
 * comes from. Also holds validation errors.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = ProvSchema.class)
@JsonDeserialize(as = ProvSchema.class)
public abstract class AProvSchema implements Serializable {

    private static final long serialVersionUID = 1L;
        
    /**
     * The schema of the original file. If no schema was found,
     * {@link Schema#of()} is returned.
     */
    @Value.Default
    public Schema getSchema() {
        return Schema.of();
    }
        
    /**
     * Returns the validation errors found in the original schema file.
     * todo what is the type of the refs? only SchemaRef?
     */
    public abstract List<ValidationError> getErrors(); 
    
    
}
