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
package eu.trentorise.opendata.traceprov.impl.dcat;

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.traceprov.dcat.IDcatDataset;
import eu.trentorise.opendata.traceprov.dcat.IDcatDistribution;
import eu.trentorise.opendata.traceprov.dcat.IFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.ISkosConcept;
import static eu.trentorise.opendata.traceprov.impl.TraceProvUtils.checkNonNull;
import java.util.List;

/**
 * Mutable implementation of a dcat:Dataset
 * http://www.w3.org/TR/vocab-dcat/#Class:_Dataset
 *
 * @author David Leoni
 */
public class DcatDataset implements IDcatDataset {

    /**
     * to be used when there is to assign somewhere a dcat dataset which is not
     * known. The id of the dataset will be the empty string.
     */
    public static final DcatDataset UNKNOWN_DATASET = new DcatDataset();

    private String uri;
    private String accrualPeriodicity;
    private String contactPoint;
    private String description;
    private List<? extends IDcatDistribution> distributions;
    private String issued;
    private String identifier;
    private List<String> keywords;
    private String landingPage;
    private String language;
    private String modified;
    private IFoafAgent publisher;
    private String spatial;
    private String title;
    private String temporal;
    private ISkosConcept theme;

    public DcatDataset() {
        uri = "";
        accrualPeriodicity = "";
        contactPoint = "";
        description = "";
        distributions = ImmutableList.of();
        issued = "";
        identifier = "";
        keywords = ImmutableList.of();
        landingPage = "";
        language = "";
        modified = "";
        publisher = FoafAgent.UNKNOWN_AGENT;
        spatial = "";
        title = "";
        temporal = "";
        theme = SkosConcept.UNKWOWN_SKOS_CONCEPT;
    }

    @Override
    public String getAccrualPeriodicity() {
        return accrualPeriodicity;
    }

    
    public void setAccrualPeriodicity(String accrualPeriodicity) {
        if (accrualPeriodicity == null) {
            throw new IllegalArgumentException("null accrualPeriodicity is not allowed!");
        }
        this.accrualPeriodicity = accrualPeriodicity;
    }

    @Override
    public String getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(String contactPoint) {
        if (contactPoint == null) {
            throw new IllegalArgumentException("null contactPoint is not allowed!");
        }

        this.contactPoint = contactPoint;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("null description is not allowed!");
        }
        this.description = description;
    }

    @Override
    public List<? extends IDcatDistribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<? extends IDcatDistribution> distributions) {
        if (distributions == null) {
            throw new IllegalArgumentException("null distributions are not allowed!");
        }

        this.distributions = distributions;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        checkNonNull(issued, "dcat dataset issued date");
        this.issued = issued;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        checkNonNull(identifier, "dcat dataset identifier");

        this.identifier = identifier;
    }

    @Override
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        checkNonNull(keywords, "dcat dataset keywords");
        this.keywords = keywords;
    }

    @Override
    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        checkNonNull(landingPage, "dcat dataset landing page");

        this.landingPage = landingPage;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        checkNonNull(language, "dcat dataset language");
        this.language = language;
    }

    @Override
    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        checkNonNull(modified, "dcat dataset modified date");
        this.modified = modified;
    }

    @Override
    public IFoafAgent getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher as a FoafAgent. If the publisher is unknown, don't put
     * null, use instead an object like {@link FoafAgent#UNKNOWN_AGENT}
     */
    public void setPublisher(IFoafAgent publisher) {
        checkNonNull(publisher, "dcat dataset publisher");

        this.publisher = publisher;
    }

    @Override
    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        if (spatial == null) {
            throw new IllegalArgumentException("null spatial is not allowed!");
        }

        this.spatial = spatial;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("null title is not allowed!");
        }

        this.title = title;
    }

    @Override
    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        if (temporal == null) {
            throw new IllegalArgumentException("null temporal is not allowed!");
        }

        this.temporal = temporal;
    }

    @Override
    public ISkosConcept getCategory() {
        return theme;
    }

    public void setTheme(ISkosConcept theme) {
        if (theme == null) {
            throw new IllegalArgumentException("null theme is not allowed!");
        }

        this.theme = theme;
    }

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("nulluri is not allowed!");
        }

        this.uri = uri;
    }

}
