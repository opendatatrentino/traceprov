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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.annotation.concurrent.Immutable;
import org.immutables.value.Value;

/**
 * A reference to an element of a dataset or its metadata. The reference is both
 * logical (i.e. the path to a json node a/b/c) and physical (the row and column number (13,48) in
 * the json file of origin).
 *
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSerialize(as = ARef.class)
@JsonDeserialize(as = ARef.class)
@Immutable
public abstract class ARef implements Serializable {

    private static final long serialVersionUID = 1L;    

    private String documentId;    
    private int physicalRow;
    private int physicalColumn;

    ARef() {
        this.documentId = "";
        this.physicalRow = -1;
        this.physicalColumn = -1;
    }

       
    /**
     * An identifier (possibly an IRI) for the original document.
     * @return 
     */
    @Value.Default    
    public String getDocumentId() {
        return documentId;
    }

    /**
     * The row index in the physical file (starting from 0), in case the file of reference is in text format.
     * In case paramter is not set -1 is returned.
     */
    @Value.Default   
    public int getPhysicalRow(){
        return physicalRow;
    }

    /**
     * The column index in the physical file (starting from 0), in case the file of reference is in text format.
     * In case paramter is not set -1 is returned.
     */    
    @Value.Default
    public int getPhysicalColumn(){
        return physicalColumn;
    }


     @Value.Check
     protected void check() {
       Preconditions.checkState(getPhysicalRow() >= -1, "physical row should be grater or equal to -1, found instead %s ", getPhysicalRow());
       Preconditions.checkState(getPhysicalColumn() >= -1, "physical column should be grater or equal to -1, found instead %s ", getPhysicalColumn());
     }    
     
    
}

