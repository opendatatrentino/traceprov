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

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import eu.trentorise.opendata.traceprov.data.ProvFile;
import eu.trentorise.opendata.traceprov.data.ProvType;
import eu.trentorise.opendata.traceprov.types.AType;
import java.io.InputStream;

/**
 * Interface to validate a given file to produces a common tree format.
 *
 * @author David Leoni
 */
public interface IValidator {

    /**
     * Validates the input stream and returns the resulting a common tree format (which may still have errors). 
     *
     * @param stream the stream to validate
     * @param mimetype the mimetype of the stream, i.e. text/csv,
     * @param schema the expected schema of the file. If unknown, pass {@link AType#of()}
     * application/json, ...
     * 
     * @throws eu.trentorise.opendata.traceprov.LoadException if some error
     * occurs while physically loading stream. In case
     * format errors are found in the original file, they should be reported within the
     * output object instead of throwing an exception.
     */
    ProvFile validate(InputStream stream, AType type, DcatMetadata dcatMetadata);
    
    /**
     * Validates the schema present in the input stream and returns the parsed result (which may still have errors). 
     * 
     * @param stream the stream to validate          
     * 
     * @throws eu.trentorise.opendata.traceprov.LoadException if some error
     * occurs while physically loading stream. In case
     * format errors are found in the original file, they should be reported within the
     * output object instead of throwing an exception.
     */
    ProvType validateType(InputStream stream);
    
    /**
     * Returns a list of the mimetypes this validator can parse.
     */
    ImmutableList<String> getSupportedMimetypes();
}
