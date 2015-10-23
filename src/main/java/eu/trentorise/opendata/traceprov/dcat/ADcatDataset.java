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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.geojson.Feature;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import org.immutables.value.Value;

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
@BuilderStylePublic
@JsonSerialize(as = DcatDataset.class)
@JsonDeserialize(as = DcatDataset.class)
abstract class ADcatDataset implements Serializable {

    public static final String CLASS_URI = "http://www.w3.org/ns/dcat#dataset";

    private static final long serialVersionUID = 1L;

    /**
     * The frequency at which dataset is published, as defined by
     * <a href="http://purl.org/dc/terms/accrualPeriodicity"> dct:
     * accrualPeriodicity </a>
     *
     * Returned format could be either
     * <ul>
     * <li>A String encoded in
     * <a href="https://en.wikipedia.org/wiki/ISO_8601#Durations" target=
     * "_blank">ISO 8601 Duration format</a>, like i.e. P3Y6M4DT12H30M5S</li>
     * <li>A String in natural language, like 'Every two weeks except during
     * summer'</li>
     * <li>the empty string, in case duration is unknown /li>
     * </ul>
     */
    @Value.Default
    public String getAccrualPeriodicity() {
	return "";
    }

    /**
     * Links a dataset to relevant contact information which is provided using
     * VCard, as defined by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:dataset_contactPoint">
     * dcat:contactPoint </a>
     *
     * i.e. dcat:contactPoint <http://example.org/transparency-office/contact>
     */
    @Value.Default
    public VCard getContactPoint() {
	return VCard.of();
    }

    /**
     * Free-text account of the dataset, as specified in
     * <a href="http://purl.org/dc/terms/description">dct:description</a>
     */
    @Value.Default
    public Dict getDescription() {
	return Dict.of();
    }

    /**
     * The distributions belonging to this dataset.
     */
    public abstract List<DcatDistribution> getDistributions();

    /**
     * A unique identifier of the dataset, defined by
     * <a href="http://purl.org/dc/terms/identifier">dct:identifier</a> The
     * identifier might be used as part of the uri of the dataset, but still
     * having it represented explicitly is useful.
     */
    @Value.Default
    public String getIdentifier() {
	return "";
    }

    /**
     * Date of formal issuance (e.g., publication) of the dataset.
     *
     * Note Dcat standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11". If date is unknown the
     * empty string is used.
     */
    @Value.Default
    public String getIssued() {
	return "";
    }

    /**
     * A set of keywords or tags describing the dataset, as specified by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:dataset_keyword"> dcat
     * :keyword </a> For example: "accountability","transparency" ,"payments"
     */
    public abstract List<String> getKeywords();

    /**
     * A Web page that can be navigated to in a Web browser to gain access to
     * the dataset, its distributions and/or additional information. If the
     * distribution(s) are accessible only through a landing page (i.e. direct
     * download URLs are not known), then the landing page link SHOULD be
     * duplicated as accessURL on a distribution.
     *
     * For relation with {@link ADcatDistribution#getAccessURL()} and
     * {@link ADcatDistribution#getDownloadURL()} see
     * <a href="http://www.w3.org/TR/vocab-dcat/#example-landing-page"> official
     * dcat documentation </a>
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
     * Library of Congress (
     * <a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO 639-1</a>,
     * <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO 639-2</a>)... If
     * a ISO 639-1 (two-letter) code is defined for language, then its
     * corresponding IRI should be used; if no ISO 639-1 code is defined, then
     * IRI corresponding to the ISO 639-2 (three-letter) code should be used.
     */
    public abstract List<Locale> getLanguages();

    /**
     * Most recent date on which the dataset was changed, updated or modified.
     * The value of this property indicates a change to the actual dataset, not
     * a change to the catalog record. An absent value may indicate that the
     * dataset has never changed after its initial publication, or that the date
     * of last modification is not known, or that the dataset is continuously
     * updated.
     *
     * Note Dcat standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11". If date is unknown the
     * empty string is used.
     *
     * @see #getAccrualPeriodicity()
     */
    @Value.Default
    public String getModified() {
	return "";
    }

    /**
     * An entity responsible for making the dataset available. Specified by
     * <a href="http://purl.org/dc/terms/publisher" target="_bank">dct:publisher
     * </a>
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
     * <a href="http://purl.org/dc/terms/spatial">dct:spatial</a> i.e.
     * dct:spatial <http://www.geonames.org/6695072> ;
     *
     * The returned object may be either:
     * <ul>
     * <li>A {@link eu.trentorise.opendata.traceprov.geojson.GeoJson GeoJSON}
     * object</li>
     * <li>A natural language name inside
     * {@link eu.trentorise.opendata.traceprov.geojson.Feature#ofName(java.lang.String)
     * Feature object}</li>
     * <li>a url to an identifier of the location, i.e.
     * http://www.geonames.org/6695072 (in this case you can use
     * {@link eu.trentorise.opendata.traceprov.geojson.Feature#ofId(java.lang.String)
     * Feature.ofId}</li>
     * <li>if spatial value is unknwon
     * {@link eu.trentorise.opendata.traceprov.geojson.Feature#of() is returned.
     * </li>
     * </ul>
     *
     */
    @Value.Default
    public GeoJson getSpatial() {
	return Feature.of();
    }
    
 

    /**
     * The temporal period that the dataset covers, that is, an interval of time
     * that is named or defined by its start and end dates as defined by
     * <a href="http://purl.org/dc/terms/temporal">dct:temporal</a>
     *
     * {@code 
     * i.e. dct:temporal
     * <http://reference.data.gov.uk/id/quarter/2006-Q1> ; }
     *
     * For allowed values, see 
     * <ul>
     * <li>A string formatted following
     * <a href="https://en.wikipedia.org/wiki/ISO_8601#Time_intervals">ISO 8601
     * Date and Time interval</a> string format i.e.
     * "2007-03-01T13:00:00Z/2008-05-11T15:30:00Z". Note ISO 8601 only allows
     * <i>closed intervals</i></li>
     * <li>An open interval where one of the two dates is missing. Interval
     * shall either begin with '/' or end with '/'. The present date string will
     * be formatted following <a href="http://www.w3.org/TR/NOTE-datetime">ISO
     * 8601 Date and Time format</a> NOTE: open intervals are <i> not <i> part
     * of ISO 8601 standard.</li>
     * <li>A String in natural language, i.e. Summer of 2014</li>
     * <li>An empty string if the interval is unknown</li>
     * </ul>
     */
    @Value.Default
    public String getTemporal() {
	return "";
    }

    /**
     * The main themes of the dataset. A dataset can belong to multiple themes.
     * The set of {@link ASkosConcept}s used to categorize the datasets are
     * organized in the {@link ASkosConceptScheme} returned by
     * {@link ADcatCatalog#getThemes()}. Notice that 'category' is also used as
     * synonym of 'themes' in dcat specs.
     */
    public abstract List<SkosConcept> getThemes();

    /**
     * A name given to the dataset as specified by
     * <a href="http://purl.org/dc/terms/title">dct:title</a> i.e.
     * "Apple Production Statistics".
     */
    @Value.Default
    public Dict getTitle() {
	return Dict.of();
    }

    /**
     * Returns the URI of the dataset. Not present in the dcat specs.
     *
     * @see #getIdentifier()
     */
    @Value.Default
    public String getUri() {
	return "";
    }

}
