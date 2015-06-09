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
import org.immutables.value.Value;

/**
 * A reference to an element of a dataset or its metadata.
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class Ref {
    
    private static final Ref INSTANCE = new Ref();
    
    private String documentId;
    
    Ref(){
        this.documentId = "";
    }
    
    @Value.Default
    public String getDocumentId() {
        return "";
    }            
    
    /** Silly workaround for immutables giving 
     \Users\david_2\Da\prj\trentorise\traceprov\prj\target\generated-sources\annotations\eu\trentorise\opendata\traceprov\schema\CellRef.java:[168,41] error: cannot find symbol*/
    public String documentId(){
        return documentId;
    }
    
    /**
     * Returns a missing ref.
     */
    public static Ref of(){
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "Ref{" + "documentId=" + documentId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.documentId != null ? this.documentId.hashCode() : 0);
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
        final Ref other = (Ref) obj;
        if ((this.documentId == null) ? (other.documentId != null) : !this.documentId.equals(other.documentId)) {
            return false;
        }
        return true;
    }
    
    
}
