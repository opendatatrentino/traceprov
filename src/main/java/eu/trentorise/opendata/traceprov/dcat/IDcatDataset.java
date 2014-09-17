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

import java.util.List;

/**
 * Models a dcat:Dataset http://www.w3.org/TR/vocab-dcat/#Class:_Dataset
 *
 * @author David Leoni
 */
public interface IDcatDataset {

    /**
     * i.e. dct:accrualPeriodicity
     * <http://purl.org/linked-data/sdmx/2009/code#freq-W> ;
     *
     */
    String getAccrualPeriodicity();

    /**
     * dcat:contactPoint <http://example.org/transparency-office/contact> ; mmm
     * should be in vcard-rdf format: http://www.w3.org/TR/vcard-rdf/
     */
    String getContactPoint();

    /**
     * dct:description http://purl.org/dc/terms/description
     *
     * @return
     */
    String getDescription();

    List<? extends IDcatDistribution> getDistributions();

    /*
     A unique identifier of the dataset. 
     dct:identifier http://purl.org/dc/terms/identifier
     The identifier might be used as part of the URI of the dataset, but still 
     having it represented explicitly is useful.
     */
    String getIdentifier();

    /**
     * Let's keep it a String for now i.e. dct:issued "2011-12-05"^^xsd:date ;
     */
    String getIssued();

    /**
     * i.e. dcat:keyword "accountability","transparency" ,"payments" ;
     */
    List<String> getKeywords();

    /**
     * For relation with Distribution.accessURL and downloadURL see
     * http://www.w3.org/TR/vocab-dcat/#a-dataset-available-only-behind-some-web-page
     */
    String getLandingPage();

    /**
     * i.e. dct:language <http://id.loc.gov/vocabulary/iso639-1/en> ;
     */
    String getLanguage();

    /**
     * Let's keep it a String for now i.e. dct:modified "2011-12-05"^^xsd:date ;
     */
    String getModified();

    /**
     * i.e. dct:publisher :finance-ministry ;
     */
    IFoafAgent getPublisher();

    /**
     * i.e. dct:spatial <http://www.geonames.org/6695072> ;
     */
    String getSpatial();

    /**
     * i.e. dct:temporal <http://reference.data.gov.uk/id/quarter/2006-Q1> ;
     */
    String getTemporal();

    
    ISkosConcept getTheme();

    /**
     * i.e. "Imaginary dataset"
     */
    String getTitle();

    /**
     * Returns the URI of the dataset.
     */
    String getURI();

}
