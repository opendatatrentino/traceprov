package eu.trentorise.opendata.trasceprov.test;

import eu.trentorise.opendata.traceprov.impl.TraceProvUtils;
import java.util.Locale;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class UtilsTest {
    
    @Test
    public void testChecker(){
        try {
            TraceProvUtils.checkNonEmpty(null, "my string");
            Assert.fail();
        } catch (IllegalArgumentException ex){
            
        }
        try {
            TraceProvUtils.checkNonEmpty("", "my string");
            Assert.fail();
        } catch (IllegalArgumentException ex){
            
        }
                
        TraceProvUtils.checkNonNull("", "my obj");
        
        try {
            TraceProvUtils.checkNonNull(null, "my obj");
            Assert.fail();
        } catch (IllegalArgumentException ex){
            
        }
    }
    
    @Test
    public void testLanguageTag(){
        // we want gracious null handling
        assertEquals(Locale.ROOT, TraceProvUtils.languageTagToLocale(null));
        assertEquals("", TraceProvUtils.localeToLanguageTag(null));
    }
}
