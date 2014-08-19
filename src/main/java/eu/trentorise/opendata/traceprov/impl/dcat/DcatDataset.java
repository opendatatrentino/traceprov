/** *****************************************************************************
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

import eu.trentorise.opendata.traceprov.dcat.IDcatDataset;
import eu.trentorise.opendata.traceprov.dcat.IDcatDistribution;
import eu.trentorise.opendata.traceprov.dcat.IFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.ISkosConcept;
import java.util.List;


public class DcatDataset implements IDcatDataset {
    
    private String URI;    
    private String accrualPeriodicity;    
    private String contactPoint;    
    private String description;        
    private List<IDcatDistribution> distributions;
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
    }

    
    
    @Override
    public String getAccrualPeriodicity() {
        return accrualPeriodicity;
    }

    public void setAccrualPeriodicity(String accrualPeriodicity) {
        this.accrualPeriodicity = accrualPeriodicity;
    }

    @Override
    public String getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<IDcatDistribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<IDcatDistribution> distributions) {
        this.distributions = distributions;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public IFoafAgent getPublisher() {
        return publisher;
    }

    public void setPublisher(IFoafAgent publisher) {
        this.publisher = publisher;
    }

    @Override
    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        this.spatial = spatial;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

    @Override
    public ISkosConcept getTheme() {
        return theme;
    }

    public void setTheme(ISkosConcept theme) {
        this.theme = theme;
    }

    @Override
    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    
}
