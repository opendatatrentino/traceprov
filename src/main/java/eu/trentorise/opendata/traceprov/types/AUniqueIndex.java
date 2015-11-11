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
package eu.trentorise.opendata.traceprov.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * A unique index is a set of property definitions 
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = UniqueIndex.class)
@JsonDeserialize(as = UniqueIndex.class)
abstract class AUniqueIndex implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    /**
     * The id of the unique index, which may be a url. If unknown, it will be
     * the empty string.
     *
     */
    @Value.Default
    public String getId() {
        return "";
    }    
    
    /**
     * The name of the unique index. If unknown, it will be the empty string.
     *
     */
    @Value.Default
    public String getName() {
        return "";
    }


    /**
     * The list of property definition ids of which the unique index is
     * composed.
     */
    public abstract List<String> getPropertyDefIds();

}
