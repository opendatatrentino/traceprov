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

import eu.trentorise.opendata.commons.LocalizedString;
import eu.trentorise.opendata.traceprov.schema.Ref;

/**
 * Represent a generic validation error in a {@link eu.trentorise.opendata.traceprov.data.ProvFile}
 * @author David Leoni 
 */
public class GenericError extends ValidationError {

    private static final long serialVersionUID = 1L;
    
    private final static GenericError INSTANCE = new GenericError(Ref.of(), LocalizedString.of());
    
    private GenericError(Ref ref, LocalizedString reason) {
        super(ref, reason);
    }
    
    public static GenericError of(Ref ref, LocalizedString reason){
        return new  GenericError(ref, reason);
    }
    
    /**
     * Returns an empty generic error     
     */
    public static GenericError of(){
        return  INSTANCE;
    }
}
