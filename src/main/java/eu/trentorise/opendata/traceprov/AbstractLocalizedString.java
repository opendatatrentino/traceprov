package eu.trentorise.opendata.traceprov;

import java.util.Locale;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
abstract class AbstractLocalizedString {
    
    /**
     * Default locale is {@link Locale#ROOT}
     */
    @Value.Default
    @Value.Parameter
    public Locale getLocale(){
        return Locale.ROOT;
    }
    
    @Value.Default
    @Value.Parameter
    public String getString(){
        return "";
    }
}
