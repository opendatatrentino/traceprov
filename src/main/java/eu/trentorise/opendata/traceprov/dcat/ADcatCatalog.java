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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.Dict;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import org.immutables.value.Value;

/**
 * A data catalog is a curated collection of metadata about datasets, as
 * specified by
 * <a href="http://www.w3.org/TR/vocab-dcat/#class-catalog">dcat:catalog</a>
 *
 * Typically, a web-based data catalog is represented as a single instance of
 * this class.
 *
 * Following properties were not included: <br/>
 * List<DcatDataset> datasets and List<DcatCatalogRecord> records
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DcatCatalog.class)
@JsonDeserialize(as = DcatCatalog.class)
abstract class ADcatCatalog implements Serializable {

    public static final String CLASS_URI = "http://www.w3.org/ns/dcat#Catalog";

    private static final long serialVersionUID = 1L;

    /**
     * A free-text account of the catalog, as specified by
     * <a href="http://purl.org/dc/terms/description">dct:description </a>
     */
    @Value.Default
    public Dict getDescription() {
	return Dict.of();
    }

    ;

    /**
     * The homepage of the catalog. It should be unique and precisely identify
     * the catalog. This allows smushing various descriptions of the catalog
     * when different URIs are used. Specified by
     * <a href="http://xmlns.com/foaf/spec/#term_homepage">foaf:homepage</a>
     */
    @Value.Default
    public String getHomepage() {
	return "";
    }

    /**
     * Date of formal issuance (e.g., publication) of the catalog, as specified
     * by <a href="http://purl.org/dc/terms/issued">dct:issued</a>
     *
     * Note DCAT standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11". In TraceProv if date
     * format is unknown prepend it with {@link OdtUtils#UNPARSEABLE} to avoid confusing
     * it with regular dates. If date is unknown the empty string is used.
     *
     * @see ADcatDataset#getIssued()
     * @see ADcatDistribution#getIssued()
     * @see ADcatCatalogRecord#getIssued()
     *
     */
    @Value.Default
    public String getIssued() {
	return "";
    }


    /**
     * The languages of the catalog. This refers to the language used in the
     * textual metadata describing titles, descriptions, etc. of the datasets in
     * the catalog. The publisher might also choose to describe the language on
     * the dataset level.
     *
     * Java Locale should be created out of language codes defined by the
     * Library of Congress (
     * <a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO 639-1</a>,
     * <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO 639-2</a>). If a
     * ISO 639-1 (two-letter) code is defined for language, then its
     * corresponding IRI should be used; if no ISO 639-1 code is defined, then
     * IRI corresponding to the ISO 639-2 (three-letter) code should be used.
     *
     * @see ADcatDataset#getLanguages()
     */
    public abstract List<Locale> getLanguages();

    /**
     * This links to the license document under which the <b>catalog</b> is made
     * available and <b>not the datasets</b>. Even if the license of the catalog
     * applies to all of its datasets and distributions, it should be replicated
     * on each distribution. Specified by <a href=""> dct:license </a>
     */
    @Value.Default
    public String getLicense() {
	return "";
    }

    /**
     * Most recent date on which the catalog was changed, updated or modified.
     * Must be in a <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date
     * and Time compliant</a> string format i.e. "2011-12-11" . Specified by
     * <a href="http://purl.org/dc/terms/modified">dct:modified</a> In TraceProv
     * if date format is unknown prepend it with {@link OdtUtils#UNPARSEABLE} to avoid
     * confusing it with regular dates. If date is unknown the empty string is
     * used.
     */
    @Value.Default
    public String getModified() {
	return "";
    }

    /**
     * The entity responsible for making the catalog online.
     *
     * Default value is {@link FoafAgent#of()}.
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
     * This describes the rights under which <b>the catalog</b> can be
     * used/reused and <b> not the datasets </b>. Even if these rights apply to
     * all the catalog datasets and distributions, it should be replicated on
     * each distribution. Specified by
     * <a href="http://purl.org/dc/terms/rights">dct:rights</a>.
     *
     * @see #getLicense()
     * @see ADcatDistribution#getRights()
     */
    @Value.Default
    public String getRights() {
	return "";
    }

    /**
     * Returns the geographical area covered by the catalog, as defined by
     * <a href="http://purl.org/dc/terms/spatial">dct:spatial</a>
     */
    @Value.Default
    public String getSpatial() {
	return "";
    }

    /**
     * The taxonomy of themes used to classify catalog's datasets, as specified
     * by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:catalog_themes"> dcat:
     * themeTaxonomy </a>. Notice that 'category' is also used as synonym of
     * 'themes' in dcat specs.
     *
     * When field is not available {@link SkosConceptScheme#of()} is returned.
     *
     * @see ADcatDataset#getThemes()
     */
    @Value.Default
    public ASkosConceptScheme getThemes() {
	return SkosConceptScheme.of();
    }

    /**
     * A name given to the catalog, as specified by
     * <a href="http://purl.org/dc/terms/title">dct:title</a>
     */
    // todo put example
    @Value.Default
    public Dict getTitle() {
	return Dict.of();
    }

    /**
     * Property not in DCAT spec. This should uniquely identify the catalog,
     * giving the same result as {@link #getHomepage()}
     */
    @Value.Default
    public String getUri() {
	return "";
    }

}
