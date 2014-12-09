

package eu.trentorise.opendata.traceprov.impl.ref;

import eu.trentorise.opendata.traceprov.ref.IColumnRef;
import javax.annotation.concurrent.Immutable;

/**
 * Implements also equals and hashcode.
 * @author David Leoni
 */
@Immutable
public class ColumnRef implements IColumnRef {
    private int index;

    /**
     * 
     * @param index column index starting from zero
     */
    public ColumnRef(int index) {
        this.index = index;
    }

    private ColumnRef(){
    }    

     /** @returns index column index starting from zero  */
    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.index;
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
        final ColumnRef other = (ColumnRef) obj;
        if (this.index != other.index) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        return "Reference to column with index = " + index;
    }

    
}
