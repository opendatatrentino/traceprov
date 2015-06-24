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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import org.immutables.value.Value;

@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DocRef.class)
@JsonDeserialize(as = DocRef.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
class ADocRef extends ARef {

    /**
     * Creates a reference to a document. 
     * 
     * @param documentId an Identifier of a document (which may be an IRI)
     * @param physicalRow a row index in a text file, starting from 0. If unknown, use -1
     * @param physicalRow a column index in a text file, starting from 0. If unknown, use -1
     */
    public DocRef of(String documentId, int physicalRow, int physicalColumn) {
        return DocRef.builder().setDocumentId(documentId)
                .setPhysicalRow(physicalRow)
                .setPhysicalColumn(physicalColumn).build();
    }

}
