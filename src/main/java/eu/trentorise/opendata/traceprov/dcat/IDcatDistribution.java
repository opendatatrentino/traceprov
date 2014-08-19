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
 * Models a dcat:Distribution: http://www.w3.org/TR/vocab-dcat/#Class:_Distribution
 * @author David Leoni
 */
public interface IDcatDistribution {

    /**
     * dcat:accessURL . For relation with Dataset.landingPage see
     * http://www.w3.org/TR/vocab-dcat/#a-dataset-available-only-behind-some-web-page
     */
    String getAccessURL();

    /**
     * i.e. dcat:byteSize "5120"^^xsd:decimal ;
     */
    int getByteSize();

    /**
     * Property not in Dcat standard, added for convenience
     */
    String getDatasetURI();

    /**
     * dct:description
     */
    String getDescription();

    /**
     * i.e. dcat:downloadURL <http://www.example.org/files/001.csv> ; For
     * relation with Dataset.landingPage and see
     * http://www.w3.org/TR/vocab-dcat/#a-dataset-available-only-behind-some-web-page
     */
    String getDownloadURL();

    /**
     * dct:format
     */
    String getFormat();

    /**
     * i.e. dct:issued "2011-12-05"^^xsd:date ;
     */
    String getIssued();

    /**
     * dct:license
     */
    String getLicense();

    /**
     * i.e. dcat:mediaType "text/csv" ;
     */
    String getMediaType();

    /**
     * i.e. dct:modified "2011-12-05"^^xsd:date ;
     */
    String getModified();

    /**
     * dct:rights
     */
    String getRights();

    /**
     * i.e. dct:spatial <http://www.geonames.org/6695072> ;
     */
    String getSpatial();

    /**
     * Human readable name i.e. dct:title "CSV distribution of imaginary dataset
     * 001" ;
     */
    String getTitle();

    /**
     * Returns the URI of the distribution (which is not the uri of the file pointed to).
     */
    String getURI();

}
