package eu.trentorise.opendata.traceprov.impl.ref;

import eu.trentorise.opendata.traceprov.ref.ICellRef;
import javax.annotation.concurrent.Immutable;

/**
 * Implements also equals and hashcode.
 * @author David Leoni
 */
@Immutable
public class CellRef implements ICellRef {
    public final int row;
    public final int columnIndex;

    public CellRef(int row, int columnIndex) {
        this.row = row;
        this.columnIndex = columnIndex;
    }
    
    private CellRef() {
        this.row = 0;
        this.columnIndex = 0;
    }
    

    @Override
    public int getRowIndex() {
        return row;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }        

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.row;
        hash = 97 * hash + this.columnIndex;
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
        final CellRef other = (CellRef) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.columnIndex != other.columnIndex) {
            return false;
        }
        return true;
    }
    
    
    
}

