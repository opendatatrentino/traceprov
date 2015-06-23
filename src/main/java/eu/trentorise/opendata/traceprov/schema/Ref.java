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
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
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
@JsonSerialize(as = Ref.class)
@JsonDeserialize(as = Ref.class)
@Immutable
public class Ref implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Ref INSTANCE = new Ref();

    private String documentId;    
    private int physicalRow;
    private int physicalColumn;

    Ref() {
        this.documentId = "";
        this.physicalRow = -1;
        this.physicalColumn = -1;
    }

    private Ref(String documentId, int physicalRow, int physicalColumn) {
        checkArgument(physicalRow >= -1, "Invalid physical row, it should be >= -1, found instead %s", physicalRow);
        checkArgument(physicalColumn >= -1, "Invalid physical column, it should be >= -1, found instead %s", physicalColumn);
        checkNotNull(documentId);
        this.documentId = documentId;
        this.physicalRow = physicalRow;
        this.physicalColumn = physicalColumn;
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
     * The row in the physical file, in case the file of reference is in text format.
     * In case paramter is not set -1 is returned.
     */
    @Value.Default   
    public  int getPhysicalRow(){
        return physicalRow;
    }

    /**
     * The column in the physical file, in case the file of reference is in text format.
     * In case paramter is not set -1 is returned.
     */    
    @Value.Default
    public int getPhysicalColumn(){
        return physicalColumn;
    }

    /**
     * Returns a missing ref.
     */
    public static Ref of() {
        return INSTANCE;
    }
    
    /**
     * Creates a new reference
     * @param documentId an identifier (possibly an IRI) for the original document.
     * @param physicalRow The physical row in the original text file. If unknown, use -1
     * @param physicalColumn The physical column in the original text file. If unknown, use -1     
     */
    public static Ref of(String documentId, int physicalRow, int physicalColumn){
        return new Ref(documentId, physicalRow, physicalColumn);
    };

}
