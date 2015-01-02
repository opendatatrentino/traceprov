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
import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Models a
 * <a href="http://www.w3.org/TR/vocab-dcat/#Class:_Distribution">dcat:Distribution</a>
 *
 * @author David Leoni
 */
public interface IDcatDistribution {

    /**
     *
     * A landing page, feed, SPARQL endpoint or other type of resource that
     * gives access to the distribution of the dataset. Use this method, and not
     * {@link #getDownloadURL()}, when it is definitely not a download or when
     * you are not sure whether it is. If the distribution(s) are accessible
     * only through a landing page (i.e. direct download URLs are not known),
     * then the link returned by containing dataset
     * {@link IDcatDataset#getLandingPage()} should be returned by this method,
     * too.
     *
     * This field is specified by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_accessurl">dcat:accessURL</a>.
     * For relation with {@link IDcatDataset#getLandingPage()} see
     * <a href="http://www.w3.org/TR/vocab-dcat/#example-landing-page">
     * official dcat documentation </a>
     *
     */
    String getAccessURL();

    /**
     * The size of a distribution in bytes, as specified by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_size">dcat:byteSize</a>.
     * The size in bytes can be approximated when the precise size is not known.
     * When the size of the distribution is not known returns 0 (this default
     * value is not defined by DCAT standard).
     */
    int getByteSize();

    /**
     * Property not in Dcat standard, added for convenience. Result must be the
     * same as containing dataset {@link IDcatDataset#getUri()}
     */
    String getDatasetUri();

    /**
     * Free-text account of the distribution, as specified in
     * <a href="http://purl.org/dc/terms/description">dct:description</a>
     */
    Map<Locale, String> getDescription();

    /**
     * A file that contains the distribution of the dataset in a given format
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_downloadurl">dcat:downloadURL</a>
     * is a specific form of
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_accessurl">dcat:accessURL</a>
     * (which is returned by {@link #getAccessURL()}. Nevertheless, DCAT does
     * not define dcat:downloadURL as a subproperty of
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_accessurl">dcat:accessURL</a>
     * not to enforce this entailment as DCAT profiles may wish to impose a
     * stronger separation where they only use accessURL for non-download
     * locations.
     *
     * i.e. dcat:downloadURL <http://www.example.org/files/001.csv> ; For
     * relation with {@link IDcatDataset#getLandingPage()} see
     * <a href="http://www.w3.org/TR/vocab-dcat/#a-dataset-available-only-behind-some-web-page">
     * official dcat documentation </a>
     */
    String getDownloadURL();

    /**
     * The file format of the distribution, as specified in
     * <a href="http://purl.org/dc/terms/format"> dct:format </a>.
     * {@link #getMediaType()} SHOULD be used if the type of the distribution is
     * defined by IANA.
     */
    String getFormat();

    /**
     * Date of formal issuance (e.g., publication) of the distribution. This
     * property should be set using the first known date of issuance.
     *
     * Note DCAT standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     *
     * @see #getModified()
     */
    DateTime getIssued();

    /**
     * The language of the distribution. Note that this does not explicitly
     * appear in the Distribution description in W3C Recommendation of 16
     * January 2014., but it's existence is indeed cited in the Dataset
     * description (see {@link IDcatDataset#getLanguage()}). So we made up the
     * property and the description below to fill the gap.
     *
     * This overrides the value of the dataset and catalog language in case of
     * conflict. The returned language should be also in the list of languages
     * returned by the containing dataset {@link IDcatDataset#getLanguage()}
     * method.
     *
     * Java Locale should be created out of language codes as defined by the
     * Library of Congress
     * (<a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO 639-1</a>,
     * <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO 639-2</a>). If a
     * ISO 639-1 (two-letter) code is defined for language, then its
     * corresponding IRI should be used; if no ISO 639-1 code is defined, then
     * IRI corresponding to the ISO 639-2 (three-letter) code should be used.
     */
    Locale getLanguage();

    /**
     * A link to the license document under which the distribution is made
     * available. The license should be a legal document giving official
     * permission to do something with the resource, as specified in
     * <a href="http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#license">
     * dct:license</a>
     *
     * @see #getRights()
     * @see IDcatCatalog#getLicense()
     */
    String getLicense();

    /**
     * The media type of the distribution as defined by
     * <a href="http://www.iana.org/assignments/media-types/media-types.xhtml">
     * IANA </a>, for example, "text/csv". This property SHOULD be used when the
     * media type of the distribution is defined in IANA, otherwise
     * <a href="http://purl.org/dc/terms/format">dct:format</a> MAY be used with
     * different values. Property is specified by
     * <a href="http://www.w3.org/TR/vocab-dcat/#Property:distribution_media_type">
     * dcat:mediaType</a>.
     */
    String getMediaType();

    /**
     * Most recent date on which the distribution was changed, updated or
     * modified.
     *
     * Note Dcat standard requires dates in string format to be
     * <a href="http://www.w3.org/TR/NOTE-datetime">ISO 8601 Date and Time
     * compliant</a> string format i.e. "2011-12-11".
     *
     * @see IDcatDataset#getModified()
     */
    DateTime getModified();

    /**
     * Information about rights held in and over the distribution, as specified
     * by <a href="http://purl.org/dc/terms/rights"> dct:rights </a>
     *
     * <a href="http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#license">
     * dct:license</a> as returned by {@link #getLicense()} (which is also a
     * sub-property of dct:rights), can be used to link a distribution to a
     * license document. However, dct:rights returned by this method allow
     * linking to a rights statement that can include licensing information as
     * well as other information that supplements the licence such as
     * attribution.
     *
     * @see IDcatDistribution#getLicense()
     * @see IDcatCatalog#getRights()
     */
    String getRights();

    /**
     * A human readable name given to the distribution, i.e. "CSV Distribution
     * of Apple Production". Specified by
     * <a href="http://purl.org/dc/terms/title"> dct:title </a>
     *
     */
    Map<Locale, String> getTitle();

    /**
     * Returns the uri of the distribution (which is not the uri of the file
     * pointed to).
     *
     * @see #getDownloadURL()
     * @see #getAccessURL()
     */
    // todo should we put an example?
    String getUri();

}
