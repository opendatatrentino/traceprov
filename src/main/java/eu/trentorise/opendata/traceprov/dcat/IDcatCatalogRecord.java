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



/**
 * Models a dcat:CatalogRecord http://www.w3.org/TR/vocab-dcat/#Class:_Catalog_record
 * 
 * If the catalog publisher decides to keep metadata describing its records
 * (i.e. the records containing metadata describing the datasets),
 * dcat:CatalogRecord can be used. For example, while :dataset-001 was issued on
 * 2011-12-05, its description on Imaginary Catalog was added on 2011-12-11.
 * This can be represented by DCAT as in the following:
 *
 * :catalog dcat:record :record-001 . :record-001 a dcat:CatalogRecord ;
 * foaf:primaryTopic :dataset-001 ; dct:issued "2011-12-11"^^xsd:date ; .
 *
 * @author David Leoni
 */
public interface IDcatCatalogRecord {

    /**
     * dct:description
     */
    String getDescription();

    /**
     * i.e. dct:issued "2011-12-11"^^xsd:date ;
     */
    String getIssued();

    /**
     * dct:modified
     */
    String getModified();

    IDcatDataset getPrimaryTopic();

    /**
     * dct:title
     */
    String getTitle();

    String getURI();

}
