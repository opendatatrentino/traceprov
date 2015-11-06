/*
 * Copyright 2015 Trento Rise.
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
package eu.trentorise.opendata.traceprov.test.services;

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import eu.trentorise.opendata.traceprov.validation.ILoader;
import java.io.OutputStream;
import java.net.URL;

/**
 * todo - just an experiment, in case we need to move it to separate traceprov-ckan package
 * @author David Leoni
 */
class CkanLoader implements ILoader {

    private String authToken;
    private String catalogUrl;

    public CkanLoader() {
        this.authToken = "";
        this.catalogUrl = "";
    }
        
    public CkanLoader(String authToken, String catalogUrl) {
        this.authToken = authToken;
        this.catalogUrl = catalogUrl;
    }    
    
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }    

    /**
     * Returns the normalized catalog Url (i.e. http://dati.trentino.it)
     */
    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }
    
    
    
    /**
     * This could represent calls to the Ckan storage api      
     * @param url the ckan url of the resource such as http://dati.trentino.it/dataset/prodotti-certificati/resource/fe507a10-4c49-4b18-8bf6-6705198cfd42     
     *              OR directly the download url. 
     */
    @Override
    public void loadData(URL url, OutputStream output) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @param url the url of the resource such as http://dati.trentino.it/dataset/prodotti-certificati/resource/fe507a10-4c49-4b18-8bf6-6705198cfd42     
     */
    @Override
    public ImmutableList<DcatMetadata> loadMetadata(Iterable<URL> url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
