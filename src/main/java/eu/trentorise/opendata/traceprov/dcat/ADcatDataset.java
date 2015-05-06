/* 
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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
package eu.trentorise.opendata.traceprov.dcat; 

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.BuilderStyle;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import org.immutables.value.Value;
import org.joda.time.DateTime;

/**
 * A collection of data, published or curated by a single agent, and available
 * for access or download in one or more formats. Defined by
 * <a href="http://www.w3.org/TR/vocab-dcat/#Class:_Dataset"> dcat:dataset </a>
 *
 * This class represents the actual dataset as published by the dataset
 * publisher. In cases where a distinction between the actual dataset and its
 * entry in the catalog is necessary (because metadata such as modification date
 * and maintainer might differ), the {@link ADcatCatalogRecord} class can be
 * used for the latter.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStyle
public abstract class ADcatDataset {

    public static final String CLASS_URI="http://www.w3.org/ns/dcat#dataset";
    
    /**
     * Returns the frequency at which dataset is published, as defined by
     * <a href="http://purl.org/dc/terms/accrualPeriodicity">
     * dct:accrualPeriodicity </a>
     */
    @Value.Default
    public String getAccrualPeriodicity() {
        return "";
    }

    /**
     * Link a dataset to relevant contact information which is provided using
     * VCard, as defined by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:dataset_contactPoint">
     * dcat:contactPoint </a>
     *
     * i.e. dcat:contactPoint <http://example.org/transparency-office/contact>
     */
    @Value.Default
    public String getContactPoint() {
        return "";
    }

    /**
     * Free-text account of the dataset, as specified in
     * <a href="http://purl.org/dc/terms/description">dct:description</a>
     */
    @Value.Default
    public Dict getDescription(){
        return Dict.of();
    };

    /**
     * Returns the distributions belonging to this dataset.
     */
    public abstract List<ADcatDistribution> getDistributions();

    /*
     A unique identifier of the dataset, defined by
     <a href="http://purl.org/dc/terms/identifier">dct:identifier</a>
     The identifier might be used as part of the uri of the dataset, but still 
     having it represented explicitly is useful.
     */
    @Value.Default
    public String getIdentifier() {
        return "";
    }
    
    /**
     * Date of formal issuance (e.g., publication) of the dataset.
     *
     * Note Dcat standard requires dates in string format to be
 <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     * 
     */
    public abstract @Nullable DateTime getIssued();

    /**
     * A set of keywords or tags describing the dataset, as specified by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:dataset_keyword">
     * dcat:keyword </a>
     * For example: "accountability","transparency" ,"payments"
     */
    public abstract List<String> getKeywords();

    /**
     * A Web page that can be navigated to in a Web browser to gain access to
     * the dataset, its distributions and/or additional information. If the
     * distribution(s) are accessible only through a landing page (i.e. direct
     * download URLs are not known), then the landing page link SHOULD be
     * duplicated as accessURL on a distribution. 
     * 
     * For relation with
     * {@link ADcatDistribution#getAccessURL()} and
     * {@link ADcatDistribution#getDownloadURL()} see
     * <a href="http://www.w3.org/TR/vocab-dcat/#example-landing-page">
     * official dcat documentation </a>
     */
    @Value.Default
    public String getLandingPage() {
        return "";
    }

    /**
     * The language of the dataset.
     *
     * This overrides the value of the catalog language in case of conflict. If
     * the dataset is available in multiple languages, use multiple values for
     * this property. If each language is available separately, define an
     * instance of {@link DcatDistribution} for each language and describe the
     * specific language of each distribution (i.e. the dataset will have
     * multiple language values from {@link #getLanguages()} and each
     * distribution will have one of these languages as value of its
     * {@link ADcatDistribution#getLanguage()} property).
     *
     * Java Locale should be created out of Language codes defined by the
     * Library of Congress
     * (<a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO 639-1</a>,
     * <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO 639-2</a>)... If
     * a ISO 639-1 (two-letter) code is defined for language, then its
     * corresponding IRI should be used; if no ISO 639-1 code is defined, then
     * IRI corresponding to the ISO 639-2 (three-letter) code should be used.
     */
    public abstract List<Locale> getLanguages();

    /**
     * Most recent date on which the dataset was changed, updated or modified.
     * The value of this property indicates a change to the actual dataset, not
 a change to the catalog record. An absent value may indicate that the
 dataset has never changed after its initial publication, or that the date
 of last modification is not known, or that the dataset is continuously
 updated.

 Note Dcat standard requires dates in string format to be
 <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     *
     * @see #getAccrualPeriodicity()
     */
    public abstract @Nullable DateTime getModified();

    /**
     * An entity responsible for making the dataset available.
     *
     * Default value is {@link FoafAgent#of()}
     *
     * @see AFoafAgent
     * @see AFoafPerson
     * @see AFoafOrganization
     */
    @Value.Default
    public AFoafAgent getPublisher() {
        return FoafAgent.of();
    }
    
    /**
     * Spatial coverage of the dataset, as specified by
     * <a href="http://purl.org/dc/terms/spatial">dct:spatial</a>
     * i.e. dct:spatial <http://www.geonames.org/6695072> ;
     */
    @Value.Default
    public String getSpatial(){
        return "";
    };

    /**
     * The temporal period that the dataset covers, that is, an interval of time
     * that is named or defined by its start and end dates as defined by
     * <a href="http://purl.org/dc/terms/temporal">dct:temporal</a>
     *
     * i.e. dct:temporal
     * <http://reference.data.gov.uk/id/quarter/2006-Q1> ;
     */
    @Value.Default
    public  String getTemporal(){
        return "";
    };

    /**
     * The main themes  of the dataset. A dataset can belong to multiple
     * themes. The set of {@link ASkosConcept}s used to categorize the
     * datasets are organized in the {@link ASkosConceptScheme} returned by
     * {@link ADcatCatalog#getThemes()}. Notice that 'category' is also used as
     * synonym of 'themes' in dcat specs.
     */
    public abstract List<SkosConcept> getThemes();

    /**
     * A name given to the dataset as specified by
     * <a href="http://purl.org/dc/terms/title">dct:title</a>
     * i.e. "Apple Production Statistics".
     */
    @Value.Default
    public Dict getTitle(){
        return Dict.of();
    };

    /**
     * Returns the URI of the dataset. Not present in the dcat specs.
     *
     * @see #getIdentifier()
     */
    @Value.Default
    public String getUri(){
        return "";
    };

}
