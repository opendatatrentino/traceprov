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
package eu.trentorise.opendata.traceprov.services;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import static com.google.common.base.Preconditions.checkNotNull;
import eu.trentorise.opendata.commons.LocalizedString;
import eu.trentorise.opendata.traceprov.schema.Ref;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an error in a {@link eu.trentorise.opendata.traceprov.data.ProvFile}
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ValidationError implements Serializable {        
    
    private static final long serialVersionUID = 1L;
    
    private final static ValidationError INSTANCE = new ValidationError(Ref.of(), LocalizedString.of());
    
    private Ref ref;
    private LocalizedString reason;  
    
    ValidationError(Ref ref, LocalizedString reason){
        checkNotNull(ref);
        checkNotNull(reason);
        this.ref = ref;
        this.reason = reason;
    }
    
    /**
     * A reference to an element with errors in a {@link eu.trentorise.opendata.traceprov.data.ProvFile}
     */
    public Ref getRef(){
        return ref;
    }
    
    /**
     * Human readable error reason
     */
    public LocalizedString getReason(){
        return reason;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.ref);
        hash = 37 * hash + Objects.hashCode(this.reason);
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
        final ValidationError other = (ValidationError) obj;
        if (!Objects.equals(this.ref, other.ref)) {
            return false;
        }
        if (!Objects.equals(this.reason, other.reason)) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns an generic error     
     */
    public static ValidationError of(){
        return  INSTANCE;
    }
    
}
