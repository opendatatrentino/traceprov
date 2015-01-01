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
package eu.trentorise.opendata.traceprov.impl.dcat;

import eu.trentorise.opendata.traceprov.dcat.IDcatCatalogRecord;
import static eu.trentorise.opendata.traceprov.impl.TraceProvUtils.checkNonNull;

/**
 * Mutable implementation of a dcat:CatalogRecord
 * http://www.w3.org/TR/vocab-dcat/#Class:_Catalog_record
 *
 * @author David Leoni
 */
public class DcatCatalogRecord implements IDcatCatalogRecord {

    private String uri;
    private String title;
    private String description;
    private String issued;
    private String modified;
    private DcatDataset primaryTopic;

    public DcatCatalogRecord() {
        uri = "";
        title = "";
        description = "";
        issued = "";
        modified = "";
        primaryTopic = DcatDataset.UNKNOWN_DATASET;
    }

    @Override
    public String getTitle() {
        return title;
    }

    /**
     * dct:title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * dct:description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    /**
     * i.e. dct:issued "2011-12-11"^^xsd:date ;
     */
    public void setIssued(String issued) {
        this.issued = issued;
    }

    @Override
    public String getModified() {
        return modified;
    }

    /**
     * dct:modified
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public DcatDataset getPrimaryTopic() {
        return primaryTopic;
    }

    /**
     * Sets the link to the dcat:Dataset resource described in the record.
     * Defined in foaf:primaryTopic. If primary topic is not known, don't put
     * null and use instead a dcat dataset with an empty id like
     * {@link DcatDataset#UNKNOWN_DATASET}
     */
    public void setPrimaryTopic(DcatDataset primaryTopic) {
        checkNonNull(primaryTopic, "dcat dataset primary topic");
        this.primaryTopic = primaryTopic;
    }

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
