/* 
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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
import org.immutables.value.Value;



/**
 * Represents a reference to a cell in a dataset in tabular format
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as=CellRef.class)
@JsonDeserialize(as=CellRef.class)
abstract class ACellRef extends Ref {
    private static final long serialVersionUID = 1L;
    /**
     * Returns the index of column the cell belongs to, starting from 0.
     */
    @Value.Default
    @Value.Parameter
    public int getRowIndex() {
        return 0;
    }

    /**
     * Returns the index of row the cell belongs to, starting from 0.
     */       
    @Value.Default
    @Value.Parameter
    public int getColumnIndex() {
        return 0;
    }        
    
}