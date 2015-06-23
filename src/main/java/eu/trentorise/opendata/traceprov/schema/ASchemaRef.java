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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import static eu.trentorise.opendata.commons.OdtUtils.checkNotEmpty;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * Reference to a simple a/b/c property ids path 
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as=SchemaRef.class)
@JsonDeserialize(as=SchemaRef.class)
abstract  class ASchemaRef extends Ref implements Serializable  {
    private static final long serialVersionUID = 1L;
    /**
     * Returns a path of property ids in a reference schema
     */    
    @Value.Parameter
    public abstract List<String> getPropertyIds();

    @Value.Check
    protected void check() {
        for (String item : getPropertyIds()) {            
            checkNotEmpty(item, "Invalid item in schema path " + getPropertyIds());
        }
    }

}
