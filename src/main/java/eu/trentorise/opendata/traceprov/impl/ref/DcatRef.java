package eu.trentorise.opendata.traceprov.impl.ref;

import eu.trentorise.opendata.traceprov.ref.IDcatRef;

/**
 * Implements also equals and hashcode.
 * @author David Leoni
 */
public class DcatRef implements IDcatRef {
        
    private String propertyURI;  

    public String getPropertyURI() {
        return propertyURI;
    }

    public DcatRef(String propertyURI) {
        this.propertyURI = propertyURI;
    }
        
    private DcatRef() {
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
        final DcatRef other = (DcatRef) obj;
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
