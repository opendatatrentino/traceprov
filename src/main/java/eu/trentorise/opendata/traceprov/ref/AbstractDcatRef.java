package eu.trentorise.opendata.traceprov.ref;

import org.immutables.value.Value;

/**
 * Represent a reference to an element of a dcat dataset, like for example the title
 * @author David Leoni
 */
@Value.Immutable(singleton = true, builder = false)
@Value.Style(get = {"is*", "get*"}, init = "set*", typeAbstract = {"Abstract*"}, typeImmutable = "")
public abstract class AbstractDcatRef {
    
    static String DUBLIC_CORE_TERMS_TITLE = "http://purl.org/dc/terms/title";

    /**
     * Let's say we are referring to a title of a dcat dataset, then we would
     * return "http://purl.org/dc/terms/title", as DCAT express title with a
     * Dublin core vocabulary attribute
     */    
    @Value.Default
    public String getPropertyUri() {
        return "";
    }
    
}
