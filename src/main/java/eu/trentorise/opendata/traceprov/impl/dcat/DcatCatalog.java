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

import eu.trentorise.opendata.traceprov.dcat.IDcatCatalog;
import eu.trentorise.opendata.traceprov.dcat.IFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.ISkosConceptScheme;
import static eu.trentorise.opendata.traceprov.impl.TraceProvUtils.checkNonNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Mutable implementation of a dcat:Catalog
 * http://www.w3.org/TR/vocab-dcat/#Class:_Dataset
 *
 * @author David Leoni
 */
public class DcatCatalog implements IDcatCatalog {

    public static final DcatCatalog UNKNOWN_CATALOG = new DcatCatalog();

    private String uri;
    private String description;
    private String homepage;
    private String issued;
    private List<String> languages;
    private String license;
    private String modified;
    private IFoafAgent publisher;
    private String rights;
    private String title;

    public DcatCatalog() {
       uri = "";
        description = "";
        homepage = "";
        issued = "";
        languages = new ArrayList();
        license = "";
        modified = "";
        publisher = FoafAgent.UNKNOWN_AGENT;
        rights = "";
        title = "";
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        checkNonNull(description, "dcat catalog description");
        this.description = description;
    }

    @Override
    public String getHomepage() {
        return homepage;
    }

    /**
     * Sets a foaf:homepage
     */
    public void setHomepage(String homepage) {
        checkNonNull(description, "dcat catalog homepage");
        this.homepage = homepage;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    /**
     * i.e. dct:issued "2011-12-11"^^xsd:date ;
     */
    public void setIssued(String issued) {
        checkNonNull(description, "dcat catalog issued");
        this.issued = issued;
    }

    @Override
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * Sets language in format specified by dct:language
     * <http://id.loc.gov/vocabulary/iso639-1/en>
     */
    public void setLanguages(List<String> languages) {
        checkNonNull(languages, "dcat catalog languages");
        this.languages = languages;
    }

    @Override
    public String getLicense() {
        return license;
    }

    /**
     * Sets dct:license
     */     
    public void setLicense(String license) {
        checkNonNull(description, "dcat catalog license");
        this.license = license;
    }

    @Override
    public String getModified() {
        return modified;
    }

    /**
     * Sets dct:modified
     */
    public void setModified(String modified) {
        checkNonNull(description, "dcat catalog modified");
        this.modified = modified;
    }

    @Override
    public IFoafAgent getPublisher() {
        return publisher;
    }

    public void setPublisher(IFoafAgent publisher) {
        checkNonNull(description, "dcat catalog publisher");
        this.publisher = publisher;
    }

    
    @Override
    public String getRights() {
        return rights;
    }

    /**
     * Sets rights as specified by dct:rights
     */    
    public void setRights(String rights) {
        checkNonNull(description, "dcat catalog rights");
        this.rights = rights;
    }

    @Override
    public String getTitle() {
        return title;
    }

    
    /**
     * Sets title as specified by 
     * <a href="http://purl.org/dc/terms/title">dct:title</a>
     */    
    public void setTitle(String title) {
        checkNonNull(description, "dcat catalog title");
        this.title = title;
    }

    @Override
    public String getUri() {
        return uri;
    }
    
    
    public void setUri(String uri) {
        checkNonNull(description, "dcat cataloguri");
        this.uri =uri;
    }

    public String getSpatial() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ISkosConceptScheme getCategories() {
        return 
    }

}
