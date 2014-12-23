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
import static eu.trentorise.opendata.traceprov.impl.TraceProvUtils.checkNonNull;

/**
 * Mutable implementation of a dcat:Catalog  http://www.w3.org/TR/vocab-dcat/#Class:_Dataset
 * @author David Leoni
 */
public class DcatCatalog implements IDcatCatalog {
    public static final DcatCatalog UNKNOWN_CATALOG = new DcatCatalog();

    private String URI;
    private String description;
    private String homepage;
    private String issued;
    private String language;
    private String license;
    private String modified;
    private IFoafAgent publisher;
    private String rights;
    private String title;

    public DcatCatalog() {
        URI = "";
        description = "";
        homepage = "";
        issued = "";
        language = "";
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

    public void setHomepage(String homepage) {
        checkNonNull(description, "dcat catalog homepage");
        this.homepage = homepage;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        checkNonNull(description, "dcat catalog issued");
        this.issued = issued;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        checkNonNull(description, "dcat catalog language");
        this.language = language;
    }

    @Override
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        checkNonNull(description, "dcat catalog license");
        this.license = license;
    }

    @Override
    public String getModified() {
        return modified;
    }

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

    public void setRights(String rights) {
        checkNonNull(description, "dcat catalog rights");
        this.rights = rights;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        checkNonNull(description, "dcat catalog title");
        this.title = title;
    }

    @Override
    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        checkNonNull(description, "dcat catalog URI");
        this.URI = URI;
    }

}
