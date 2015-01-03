package eu.trentorise.opendata.traceprov.ref;

import org.immutables.value.Value;



/**
 * Represents a reference to a cell in a dataset in tabular format
 *
 * @author David Leoni
 */
@Value.Immutable(singleton = true, builder = false)
@Value.Style(get = {"is*", "get*"}, init = "set*", typeAbstract = {"Abstract*"}, typeImmutable = "")
public abstract class AbstractCellRef {

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
