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

import com.google.common.base.Optional;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
 * and maintainer might differ), the {@link AbstractDcatCatalogRecord} class can be
 * used for the latter.
 *
 * @author David Leoni
 */
@Value.Immutable(singleton = true)
@Value.Style(get = {"is*", "get*"}, init = "set*", typeAbstract = {"Abstract*"}, typeImmutable = "" )
public abstract class AbstractDcatDataset {

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
    public abstract Map<Locale, String> getDescription();

    /**
     * Returns the distributions belonging to this dataset.
     */
    public abstract List<DcatDistribution> getDistributions();

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

    ;

    /**
     * Date of formal issuance (e.g., publication) of the dataset.
     *
     * Note Dcat standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     * 
     */
    public abstract Optional<DateTime> getIssued();

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
     * {@link AbstractDcatDistribution#getAccessURL()} and
     * {@link AbstractDcatDistribution#getDownloadURL()} see
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
     * {@link AbstractDcatDistribution#getLanguage()} property).
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
     * a change to the catalog record. An absent value may indicate that the
     * dataset has never changed after its initial publication, or that the date
     * of last modification is not known, or that the dataset is continuously
     * updated.
     *
     * Note Dcat standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     *
     * @see #getAccrualPeriodicity()
     */
    public abstract Optional<DateTime> getModified();

    /**
     * An entity responsible for making the dataset available.
     *
     * Default value is {@link FoafAgent#of()}
     *
     * @see AbstractFoafAgent
     * @see AbstractFoafPerson
     * @see AbstractFoafOrganization
     */
    @Value.Default
    public FoafAgent getPublisher() {
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
     * The main category of the dataset. A dataset can belong to multiple
     * categories. The set of {@link AbstractSkosConcept}s used to categorize the
     * datasets are organized in the {@link AbstractSkosConceptScheme} returned by
     * {@link AbstractDcatCatalog#getCategories()}. Notice that 'theme' is also used as
     * synonym of 'category' in dcat specs.
     */
    public abstract List<SkosConcept> getCategories();

    /**
     * A name given to the dataset as specified by
     * <a href="http://purl.org/dc/terms/title">dct:title</a>
     * i.e. "Apple Production Statistics".
     */
    public abstract Map<Locale, String> getTitle();

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
