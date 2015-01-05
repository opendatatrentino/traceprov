package eu.trentorise.opendata.traceprov.ref;

import eu.trentorise.opendata.traceprov.SimpleStyle;
import org.immutables.value.Value;

/**
 * Represents a reference to a column in a tabular dataset.
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
abstract class AbstractColumnRef {

    /**
     * Column index starting from zero
     */
    @Value.Default
    public int getIndex() {
        return 0;
    }

}
