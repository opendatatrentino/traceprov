/**
 * *****************************************************************************
 * Copyright 2013-2014 Trento Rise (www.trentorise.eu/)
 * 
* All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 * 
* http://www.gnu.org/licenses/lgpl-2.1.html
 * 
* This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
*******************************************************************************
 */
package eu.trentorise.opendata.traceprov.dcat;

import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * A record in a data catalog, describing a single dataset, as specified by
 * <a href="http://www.w3.org/TR/vocab-dcat/#Class:_Catalog_record">dcat:CatalogRecorda</a>
 *
 * This class is optional and not all catalogs will use it. It exists for
 * catalogs where a distinction is made between metadata about a dataset and
 * metadata about the dataset's entry in the catalog. For example, the
 * publication date property of the dataset reflects the date when the
 * information was originally made available by the publishing agency, while the
 * publication date of the catalog record is the date when the dataset was added
 * to the catalog.
 *
 * As a practical example, while :dataset-001 was issued on 2011-12-05, its
 * description on Imaginary Catalog was added on 2011-12-11. This can be represented by
 * DCAT as in the following:
 *
 * <pre>
 * :catalog dcat:record :record-001 .
 * :record-001 a dcat:CatalogRecord ;
 *             foaf:primaryTopic :dataset-001 ;
 *             dct:issued "2011-12-11"^^xsd:date; .
 * </pre>
 *
 * In cases where both dates differ, or where only the latter is known, the
 * publication date should only be specified for the catalog record. Notice that
 * the W3C PROV Ontology [prov-o] allows describing further provenance
 * information such as the details of the process and the agent involved in a
 * particular change to a dataset.
 *
 * @author David Leoni
 */
public interface IDcatCatalogRecord {

    /**
     * free-text account of the record, as specified by
     * <a href="http://purl.org/dc/terms/description">dct:description </a>
     */
    Map<Locale, String> getDescription();

    /**
     * The date of listing the corresponding dataset in the catalog, as
     * specified by <a href="http://purl.org/dc/terms/issued">dct:issued</a>.
     * This indicates the date of listing the dataset in the catalog and not the
     * publication date of the dataset itself.
     *
     * Note DCAT standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     *
     * @see IDcatDataset#getIssued()
     */
    DateTime getIssued();

    /**
     * Most recent date on which the catalog entry was changed, updated or
     * modified.
     *
     * This indicates the date of last change of a catalog entry, i.e. the
     * catalog metadata description of the dataset, and not the date of the
     * dataset itself.
     *
     * Specified by
     * <a href="http://purl.org/dc/terms/modified">dct:modified</a>
     *
     * @see IDcatDataset#getModified()
     */
    DateTime getModified();

    /**
     * Links the catalog record to the dcat:Dataset resource described in the
     * record. Each catalog record can have at most one primary topic i.e.
     * describes one dataset. If no primary topic is available,
     * {@link eu.trentorise.opendata.traceprov.impl.dcat.DcatDataset#UNKNOWN_DATASET}
     * will be returned instead. Specified by
     * <a href="http://xmlns.com/foaf/spec/#term_primaryTopic">
     * foaf:primaryTopic </a>
     */
    IDcatDataset getPrimaryTopic();

    /**
     * A name given to the record, as specified by
     * <a href="http://purl.org/dc/terms/title">dct:title</a>
     */
    Map<Locale, String> getTitle();

    /**
     * Property not in DCAT spec. This should uniquely identify the record.
     */
    String getUri();

}
