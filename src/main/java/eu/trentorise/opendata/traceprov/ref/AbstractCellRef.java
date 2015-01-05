package eu.trentorise.opendata.traceprov.ref;

import eu.trentorise.opendata.traceprov.SimpleStyle;
import org.immutables.value.Value;



/**
 * Represents a reference to a cell in a dataset in tabular format
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
abstract class AbstractCellRef {

    /**
     * Returns the index of column the cell belongs to.
     */
    @Value.Default
    @Value.Parameter
    public int getRowIndex() {
        return 0;
    }

    /**
     * Returns the index of row the cell belongs to.
     */       
    @Value.Default
    @Value.Parameter
    public int getColumnIndex() {
        return 0;
    }        

}
