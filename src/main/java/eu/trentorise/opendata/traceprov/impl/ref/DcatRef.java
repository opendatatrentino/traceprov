package eu.trentorise.opendata.traceprov.impl.ref;

import eu.trentorise.opendata.traceprov.ref.IDcatRef;

/**
 * Implements also equals and hashcode.
 * @author David Leoni
 */
public class DcatRef implements IDcatRef {
        
    private String propertyUri;  

    public String getPropertyUri() {
        return propertyUri;
    }

    public DcatRef(String propertyUri) {
        this.propertyUri = propertyUri;
    }
        
    private DcatRef() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.propertyUri != null ? this.propertyUri.hashCode() : 0);
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
        if ((this.propertyUri == null) ? (other.propertyUri != null) : !this.propertyUri.equals(other.propertyUri)) {
            return false;
        }
        return true;
    }
    
    
    @Override
    public String toString(){
        return "Reference to dataset metadata property " + propertyUri;
    }
    
}
