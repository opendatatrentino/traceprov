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

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.traceprov.exceptions.LoadException;
import static eu.trentorise.opendata.traceprov.TraceProvs.TRACEPROV_IRI;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import eu.trentorise.opendata.traceprov.data.TraceFile;
import eu.trentorise.opendata.traceprov.data.ParsedType;
import eu.trentorise.opendata.traceprov.types.ClassType;
import eu.trentorise.opendata.traceprov.types.Def;
import eu.trentorise.opendata.traceprov.types.DefMetadata;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.validation.IValidator;
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
 * See manual for detailed explanation of how a CSV should be searched. todo put link
 */
// todo Just an experiment, in case we need to move this to separate
// traceprov-csv repo
public class CsvValidator implements IValidator {

    private static final Logger LOG = Logger.getLogger(CsvValidator.class.getName());
    private static final ImmutableList<String> MIMETYPES = ImmutableList.of("text/csv");

    @Override
    public TraceFile validate(InputStream stream, String mimeType, TraceType type, DcatMetadata dcatMetadata) {

	LOG.warning("CURRENT CSV VALIDATOR IS *EXPERIMENTAL*. DON'T TRUST IT!");

	TraceFile.Builder builder = TraceFile.builder();

	try {
	    Reader in = new InputStreamReader(stream);

	    Iterable<CSVRecord> firstPass = CSVFormat.DEFAULT.parse(in);
	    List<String> headers = new ArrayList();
	    if (firstPass.iterator().hasNext()) {
		for (String header : firstPass.iterator().next()) {
		    headers.add(header);
		}
		if (headers.isEmpty()) {
		    throw new LoadException("Found no headers in CSV!");
		}
	    } else {
		throw new LoadException("Provided csv is empty!!!");
	    }

	    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
	    int pyhsicalRow = 0;

	    for (CSVRecord record : records) {
		long physicalColumn = record.getCharacterPosition();

		for (String field : record) {

		}
		String lastName = record.get("Last Name");
		String firstName = record.get("First Name");
		pyhsicalRow += 1;
	    }
	} catch (IOException ex) {
	    throw new LoadException(ex);
	}
	throw new RuntimeException("todo implement me");
    }

    @Override
    public ParsedType validateType(InputStream stream, String mimeType) {
	try {
	    Reader in = new InputStreamReader(stream);

	    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
	    List<String> headers = new ArrayList();
	    if (records.iterator().hasNext()) {
		for (String header : records.iterator().next()) {
		    headers.add(header);
		}
		if (headers.isEmpty()) {
		    throw new LoadException("Found no headers in CSV!");
		}

		Def.Builder<ClassType> builder = Def.builder();

		builder.setType(ClassType.builder().putPropertyDefs("a", Def.of())
			.setMetadata(DefMetadata.builder().setName(Dict.of()).build())
			.setId(TRACEPROV_IRI + "generated-schema/" + UUID.randomUUID())

		.build());
	    } else {
		throw new LoadException("Provided csv is empty!!!");
	    }
	} catch (IOException ex) {
	    throw new LoadException("Error while validating schema!", ex);
	}
	throw new RuntimeException("todo implement me!");
    }

    @Override
    public ImmutableList<String> getSupportedMimetypes() {
	return MIMETYPES;
    }

}
