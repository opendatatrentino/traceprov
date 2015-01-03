package eu.trentorise.opendata.traceprov.ref;

import org.immutables.value.Value;

/**
 * Represents a reference to a column in a tabular dataset.
 *
 * @author David Leoni
 */
@Value.Immutable(singleton = true, builder = false)
@Value.Style(get = {"is*", "get*"}, init = "set*", typeAbstract = {"Abstract*"}, typeImmutable = "")
public abstract class AbstractColumnRef {

    /**
     * Column index starting from zero
     */
    @Value.Default
    public int getIndex() {
        return 0;
    }

}
