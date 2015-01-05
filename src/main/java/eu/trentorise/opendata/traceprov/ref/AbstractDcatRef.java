package eu.trentorise.opendata.traceprov.ref;

import eu.trentorise.opendata.traceprov.SimpleStyle;
import org.immutables.value.Value;

/**
 * Represent a reference to an element of a dcat dataset, like for example the title
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
abstract class AbstractDcatRef {
    
    static String DUBLIC_CORE_TERMS_TITLE = "http://purl.org/dc/terms/title";

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
