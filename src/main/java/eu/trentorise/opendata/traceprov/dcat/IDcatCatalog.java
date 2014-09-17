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
 * Models a dcat:catalog http://www.w3.org/TR/vocab-dcat/#class-catalog <br/>
 * Following properties were not included: <br/>
 * List<DcatDataset> datasets and List<DcatCatalogRecord> records
 *
 * @author David Leoni
 */
public interface IDcatCatalog {

    /**
     * dct:description
     */
    String getDescription();

    /**
     * foaf:homepage
     */
    String getHomepage();

    /**
     * i.e. dct:issued "2011-12-11"^^xsd:date ;
     */
    String getIssued();

    /**
     * i.e. dct:language <http://id.loc.gov/vocabulary/iso639-1/en> ;
     */
    String getLanguage();

    /**
     * dct:license
     */
    String getLicense();

    /**
     * dct:modified
     */
    String getModified();

    /**
     * foaf:Agent
     */
    IFoafAgent getPublisher();

    /**
     * dct:rights
     */
    String getRights();

    /**
     * dct:title
     * 
     * http://purl.org/dc/terms/title
     */
    String getTitle();

    String getURI();

}
