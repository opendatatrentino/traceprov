package eu.trentorise.opendata.traceprov.impl.ref;

import eu.trentorise.opendata.traceprov.ref.IDcatMetadataRef;

/**
 * Implements also equals and hashcode.
 * @author David Leoni
 */
public class DcatMetadataRef implements IDcatMetadataRef {
        
    private String propertyURI;  

    public String getPropertyURI() {
        return propertyURI;
    }

    public DcatMetadataRef(String propertyURI) {
        this.propertyURI = propertyURI;
    }
        
    private DcatMetadataRef() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.propertyURI != null ? this.propertyURI.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DcatMetadataRef other = (DcatMetadataRef) obj;
        if ((this.propertyURI == null) ? (other.propertyURI != null) : !this.propertyURI.equals(other.propertyURI)) {
            return false;
        }
        return true;
    }
    
    
    @Override
    public String toString(){
        return "Reference to dataset metadata property " + propertyURI;
    }
    
}
