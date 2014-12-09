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

import eu.trentorise.opendata.traceprov.dcat.IDcatDistribution;

/**
 * Mutable implementation of a dcat:Distribution: http://www.w3.org/TR/vocab-dcat/#Class:_Distribution
 * @author David Leoni
 */
public class DcatDistribution implements IDcatDistribution {
    
    private String URI;    
    private int byteSize;
    private String accessURL;    
    private String datasetIdentifier;    
    private String description;
    private String downloadURL;
    private String format;
    private String issued;    
    private String license;
    private String mediaType;
    private String modified;     
    private String rights;    
    private String spatial;       
    private String title;

    public DcatDistribution() {
        URI = "";    
        byteSize = -1;
        accessURL = "";    
        datasetIdentifier = "";    
        description = "";
        downloadURL = "";
        format = "";
        issued = "";    
        license = "";
        mediaType = "";
        modified = "";     
        rights = "";    
        spatial = "";       
        title = "";        
    }

    
    
    @Override
    public int getByteSize() {
        return byteSize;
    }
           
    public void setByteSize(int byteSize) {
        if (byteSize < - 1){
            throw new IllegalArgumentException("bytesize must be >= -1  Found instead: " + byteSize);
        }
        this.byteSize = byteSize;
    }

    @Override
    public String getAccessURL() {
        return accessURL;
    }

    public void setAccessURL(String accessURL) {
        if (accessURL == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        this.accessURL = accessURL;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.description = description;
    }

    @Override
    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        if (downloadURL == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.downloadURL = downloadURL;
    }

    @Override
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        if (format == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.format = format;
    }

    @Override
    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        if (issued == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.issued = issued;
    }

    @Override
    public String getLicense() {
        return license;
    }

    /**
     * A legal document giving official permission to do something with the resource.
     * dct:license  http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#license     
     */    
    public void setLicense(String license) {
        if (license == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.license = license;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        if (mediaType == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.mediaType = mediaType;
    }

    @Override
    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        if (modified == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.modified = modified;
    }

    @Override
    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        if (rights == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.rights = rights;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Property not in Dcat standard, added for convenience
    */
    @Override
    public String getDatasetURI() {
        return datasetIdentifier;
    }

    /**
     * Property not in Dcat standard, added for convenience
    */
    public void setDatasetIdentifier(String datasetIdentifier) {
        if (datasetIdentifier == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.datasetIdentifier = datasetIdentifier;
    }

    @Override
    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        if (spatial == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.spatial = spatial;
    }

    @Override
    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        if (URI == null){
            throw new IllegalArgumentException("null values are not accepted!");
        }
        
        this.URI = URI;
    }
    
}
