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

/**
 * Mutable implementation of a dcat:CatalogRecord  http://www.w3.org/TR/vocab-dcat/#Class:_Catalog_record
 * @author David Leoni
 */
public class DcatCatalogRecord implements IDcatCatalogRecord {
    
    private String URI;    
    private String title;    
    private String description;
    private String issued;
    private String modified;
    private DcatDataset primaryTopic;

    public DcatCatalogRecord() {
    }

    
    
    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    @Override
    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public DcatDataset getPrimaryTopic() {
        return primaryTopic;
    }

    public void setPrimaryTopic(DcatDataset primaryTopic) {
        this.primaryTopic = primaryTopic;
    }

    @Override
    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
}
