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
import java.io.Serializable;
import java.util.Locale;
import org.immutables.value.Value;

/**
 * Models a <a href="http://www.w3.org/TR/vocab-dcat/#Class:_Distribution">dcat:
 * Distribution</a>
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = DcatDistribution.class)
@JsonDeserialize(as = DcatDistribution.class)
abstract class ADcatDistribution implements Serializable {

    public static final String CLASS_URI = "http://www.w3.org/ns/dcat#distribution";

    private static final long serialVersionUID = 1L;

    /**
     *
     * A landing page, feed, SPARQL endpoint or other type of resource that
     * gives access to the distribution of the dataset. Use this method, and not
     * {@link #getDownloadURL()}, when it is definitely not a download or when
     * you are not sure whether it is. If the distribution(s) are accessible
     * only through a landing page (i.e. direct download URLs are not known),
     * then the link returned by containing dataset
     * {@link ADcatDataset#getLandingPage()} should be returned by this method,
     * too.
     *
     * This field is specified by <a href=
     * "http://www.w3.org/TR/vocab-dcat/#Property:distribution_accessurl">dcat:
     * accessURL</a>. For relation with {@link ADcatDataset#getLandingPage()}
     * see
     * <a href="http://www.w3.org/TR/vocab-dcat/#example-landing-page"> official
     * dcat documentation </a>
     *
     */
    @Value.Default
    public String getAccessURL() {
	return "";
    }

    /**
     * The size of a distribution in bytes, as specified by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_size">
     * dcat:byteSize</a>. The size in bytes can be approximated when the precise
     * size is not known. When the size of the distribution is not known field
     * must be set to 0 (this default value is not defined by DCAT standard).
     */
    @Value.Default
    public int getByteSize() {
	return 0;
    }

    /**
     * Property not in Dcat standard, added for convenience. Result must be the
     * same as containing dataset {@link ADcatDataset#getUri()}
     */
    @Value.Default
    public String getDatasetUri() {
	return "";
    }

    /**
     * Free-text account of the distribution, as specified in
     * <a href="http://purl.org/dc/terms/description">dct:description</a>
     */
    @Value.Default
    public Dict getDescription() {
	return Dict.of();
    }

    /**
     * A file that contains the distribution of the dataset in a given format
     * <a href=
     * "http://www.w3.org/TR/vocab-dcat/#Property:distribution_downloadurl">dcat
     * :downloadURL</a> is a specific form of <a href=
     * "http://www.w3.org/TR/vocab-dcat/#Property:distribution_accessurl">dcat:
     * accessURL</a> (which is returned by {@link #getAccessURL()}.
     * Nevertheless, DCAT does not define dcat:downloadURL as a subproperty of
     * <a href=
     * "http://www.w3.org/TR/vocab-dcat/#Property:distribution_accessurl">dcat:
     * accessURL</a> not to enforce this entailment as DCAT profiles may wish to
     * impose a stronger separation where they only use accessURL for
     * non-download locations.
     *
     * i.e. dcat:downloadURL <http://www.example.org/files/001.csv> ; For
     * relation with {@link ADcatDataset#getLandingPage()} see <a href=
     * "http://www.w3.org/TR/vocab-dcat/#a-dataset-available-only-behind-some-web-page">
     * official dcat documentation </a>
     */
    @Value.Default
    public String getDownloadURL() {
	return "";
    }

    /**
     * The file format of the distribution, as specified in
     * <a href="http://purl.org/dc/terms/format"> dct:format </a>.
     * {@link #getMediaType()} SHOULD be used if the type of the distribution is
     * defined by IANA.
     */
    @Value.Default
    public String getFormat() {
	return "";
    }

    /**
     * Date of formal issuance (e.g., publication) of the distribution. This
     * property should be set using the first known date of issuance.
     *
     * Note DCAT standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11". In TraceProv if date
     * format is unknown prepend it with {@link OdtUtils#UNPARSEABLE} to avoid confusing
     * it with regular dates. If date is unknown the empty string is used.
     *
     * @see #getModified()
     */
    @Value.Default
    public String getIssued() {
	return "";
    }

    ;

    /**
     * The language of the distribution. Note that this does not explicitly
     * appear in the Distribution description in W3C Recommendation of 16
     * January 2014., but it's existence is indeed cited in the Dataset
     * description (see {@link ADcatDataset#getLanguages()}). So we made up the
     * property and the description below to fill the gap.
     *
     * This overrides the value of the dataset and catalog language in case of
     * conflict. The returned language should be also in the list of languages
     * returned by the containing dataset {@link ADcatDataset#getLanguages()}
     * method.
     *
     * Java Locale should be created out of language codes as defined by the
     * Library of Congress (
     * <a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO 639-1</a>,
     * <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO 639-2</a>). If a
     * ISO 639-1 (two-letter) code is defined for language, then its
     * corresponding IRI should be used; if no ISO 639-1 code is defined, then
     * IRI corresponding to the ISO 639-2 (three-letter) code should be used.
     *
     * If no locale is known {@link Locale#ROOT} must be used.
     */
    @Value.Default
    public Locale getLanguage() {
	return Locale.ROOT;
    }

    ;

    /**
     * A link to the license document under which the distribution is made
     * available. The license should be a legal document giving official
     * permission to do something with the resource, as specified in <a href=
     * "http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#license">
     * dct:license</a>
     *
     * @see #getRights()
     * @see ADcatCatalog#getLicense()
     */
    @Value.Default
    public String getLicense() {
	return "";
    }

    /**
     * The media type of the distribution as defined by
     * <a href="http://www.iana.org/assignments/media-types/media-types.xhtml">
     * IANA </a>, for example, "text/csv". This property SHOULD be used when the
     * media type of the distribution is defined in IANA, otherwise
     * <a href="http://purl.org/dc/terms/format">dct:format</a> MAY be used with
     * different values. Property is specified by <a href=
     * "http://www.w3.org/TR/vocab-dcat/#Property:distribution_media_type"> dcat
     * :mediaType</a>.
     */
    @Value.Default
    public String getMediaType() {
	return "";
    }

    /**
     * Most recent date on which the distribution was changed, updated or
     * modified.
     *
     * Note Dcat standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11". In TraceProv if date
     * format is unknown prepend it with {@link OdtUtils#UNPARSEABLE} to avoid confusing
     * it with regular dates. If date is unknown the empty string is used.
     *
     * @see ADcatDataset#getModified()
     */
    @Value.Default
    public String getModified() {
	return "";
    }

    /**
     * Information about rights held in and over the distribution, as specified
     * by <a href="http://purl.org/dc/terms/rights"> dct:rights </a>
     *
     * <a href=
     * "http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#license">
     * dct:license</a> as returned by {@link #getLicense()} (which is also a
     * sub-property of dct:rights), can be used to link a distribution to a
     * license document. However, dct:rights returned by this method allow
     * linking to a rights statement that can include licensing information as
     * well as other information that supplements the licence such as
     * attribution.
     *
     * @see ADcatDistribution#getLicense()
     * @see ADcatCatalog#getRights()
     */
    @Value.Default
    public String getRights() {
	return "";
    }

    /**
     * A human readable name given to the distribution, i.e. "CSV Distribution
     * of Apple Production". Specified by
     * <a href="http://purl.org/dc/terms/title"> dct:title </a>
     *
     */
    @Value.Default
    public Dict getTitle() {
	return Dict.of();
    }

    /**
     * Returns the uri of the distribution (which is not the uri of the file
     * pointed to).
     *
     * @see #getDownloadURL()
     * @see #getAccessURL()
     */
    // todo should we put an example?
    @Value.Default
    public String getUri() {
	return "";
    }

}
