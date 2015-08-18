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
package eu.trentorise.opendata.traceprov.validation;

import static com.google.common.base.Preconditions.checkArgument;
import eu.trentorise.opendata.traceprov.data.Data;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import eu.trentorise.opendata.traceprov.data.ProvFile;
import eu.trentorise.opendata.traceprov.types.Type;
import eu.trentorise.opendata.traceprov.types.AnyType;

/**
 *
 * @author David Leoni
 */
public final class Validator {
    
    public Type inferType(Data node, DcatMetadata dcatMetadata){
        throw new UnsupportedOperationException("todo implement me");
    }
    
    /**
     * Provfile MUST have a proper type.      
     */
    public ProvFile validate(ProvFile provFile){
        // todo implement better check
        checkArgument(!provFile.getType().equals(AnyType.of()), "Need a proper Type to validate against, found instead %s!", provFile.getType());
                    
        throw new UnsupportedOperationException("todo implement me");
    }
    
    /**
     * Validates the input stream and returns the resulting a common tree format
     * (which may still have errors).
     *
     * @param stream the stream to validate
     * @param mimetype the mimetype of the stream, i.e. text/csv.
     * @param type the expected schema of the file. If unknown, pass
     * {@link AnyType#of()}.
     *
     * @throws eu.trentorise.opendata.traceprov.LoadException if some error
     * occurs while physically loading stream. In case format errors are found
     * in the original file, they should be reported within the output object
     * instead of throwing an exception.
     */
    ProvFile validate(ProvFile provFile, String mimetype, Type type, DcatMetadata dcatMetadata){
        throw new UnsupportedOperationException("todo implement me");
    }
}
