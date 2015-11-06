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
package eu.trentorise.opendata.traceprov.engine;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.TraceData;
import java.util.List;


/**
 * TODO Much WIP
 * @author David Leoni
 */
public abstract class Operation {
            
    /**
     * The list of references to nodes upon which perform the operation
     */
    public abstract List<Ref> getRefs();
    
    /**
     * The generic description of the operation, without considering the actual 
     * @return 
     */
    public abstract String getDescription();
    
    /**
     * 

     */
    public abstract String getDescriptionTemplate();
    
    public abstract TraceData apply(RefResolver resolver, Iterable<TraceData> nodes);
}



