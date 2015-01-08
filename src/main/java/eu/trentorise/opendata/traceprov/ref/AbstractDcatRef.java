package eu.trentorise.opendata.traceprov.ref;

import eu.trentorise.opendata.traceprov.SimpleStyle;
import eu.trentorise.opendata.traceprov.dcat.AbstractDcatDataset;
import org.immutables.value.Value;

/**
 * Represent a reference to an element of a dcat dataset, like for example the
 * title
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
abstract class AbstractDcatRef {

    /**
     * Returns the URI of the class containing the referenced element (i.e.
     * {@link AbstractDcatDataset#CLASS_URI})
     */
    @Value.Default
    public String getClassUri() {
        return "";
    }

    /**
     * Let's say we are referring to a title of a dcat dataset, then we would
     * return "http://purl.org/dc/terms/title", as DCAT expresses title with a
     * Dublin core vocabulary attribute
     */
    @Value.Default
    public String getPropertyUri() {
        return "";
    }

}
