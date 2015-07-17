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
package eu.trentorise.opendata.traceprov.test.services;

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.traceprov.LoadException;
import static eu.trentorise.opendata.traceprov.TraceProvs.TRACEPROV_IRI;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import eu.trentorise.opendata.traceprov.data.ProvFile;
import eu.trentorise.opendata.traceprov.data.ProvType;
import eu.trentorise.opendata.traceprov.types.AType;
import eu.trentorise.opendata.traceprov.validation.IValidator;
import eu.trentorise.opendata.traceprov.types.ClassDef;
import eu.trentorise.opendata.traceprov.types.PropertyDef;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * <h3>Example</h3>
 * Suppose we have an original CSV table like this:
 * <pre>
 *      h1,h2
 *      aa,ab
 *      ba,bb
 * </pre> its JSON view format is supposed to be like this:
 *
 * <pre>
 *  [
 *      ["h1","h2"],
 *      ["aa","ab"],
 *      ["ba","bb"]
 *  ]
 * </pre>
 * <p>
 * We don't use an array of records as original header names may be empty or
 * duplicated. Thus cell "ba" can be pinpointed with the JsonPath expression
 * $[2][0] <br/>
 * The first column can be selected with JsonPath expression $[*][0]
 * </p>
 * <p>
 * Once CSV is correctly loaded and transformed in a proper common tree
 * representation, then we can afford to have a more user friendly version with
 * records like this:
 *
 * <pre>
 *      [
 *          {
 *              "h1":"aa",
 *              "h2":"ab"
 *          },
 *           {
 *              "h1":"ba",
 *              "h2":"bb"
 *          }
 *      ]
 * </pre> First column can be selected with JsonPath $[*].h1
 * </p>
 *
 * @author David Leoni
 */
// todo Just an experiment, in case we need to move this to separate traceprov-csv repo
public class CsvValidator implements IValidator {
    
    private static final Logger LOG = Logger.getLogger(CsvValidator.class.getName());   
    private static final ImmutableList<String> MIMETYPES = ImmutableList.of("text/csv");
    
    @Override
    public ProvFile validate(InputStream stream, AType type, DcatMetadata dcatMetadata) {

        LOG.warning("CURRENT CSV VALIDATOR IS *EXPERIMENTAL*. DON'T TRUST IT!");
        
        ProvFile.Builder builder = ProvFile.builder();
        
        try {
            Reader in =  new InputStreamReader(stream);
            
            Iterable<CSVRecord> firstPass = CSVFormat.DEFAULT.parse(in);
            List<String> headers = new ArrayList();
            if (firstPass.iterator().hasNext()){                
                for (String header : firstPass.iterator().next()){
                    headers.add(header);
                }
                if (headers.isEmpty()){
                    throw new LoadException("Found no headers in CSV!");
                }
            } else {
                throw new LoadException("Provided csv is empty!!!");
            }
            
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            int pyhsicalRow = 0;
            
            for (CSVRecord record : records) {
                long physicalColumn = record.getCharacterPosition();
                
                for (String field : record){
                    
                }
                String lastName = record.get("Last Name");
                String firstName = record.get("First Name");
                pyhsicalRow += 1;
            }
        }
        catch (IOException ex) {
            throw new LoadException(ex);
        }
        throw new RuntimeException("todo implement me");
    }

    @Override
    public ProvType validateType(InputStream stream) {
        try {
            Reader in =  new InputStreamReader(stream);
            
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            List<String> headers = new ArrayList();
            if (records.iterator().hasNext()){                
                for (String header : records.iterator().next()){
                    headers.add(header);
                }
                if (headers.isEmpty()){
                    throw new LoadException("Found no headers in CSV!");
                }
                
                ClassDef.Builder builder = ClassDef.builder();
                builder.setName("");
                builder.setId(TRACEPROV_IRI + "generated-schema/"+UUID.randomUUID());
                builder.addPropertyDefs(PropertyDef.of());
            } else {
                throw new LoadException("Provided csv is empty!!!");
            }
        }
        catch (IOException ex) {
            throw new LoadException("Error while validating schema!", ex);
        }
        throw new RuntimeException("todo implement me!");
    }

    
    
    @Override
    public ImmutableList<String> getSupportedMimetypes() {
        return MIMETYPES;
    }

}
