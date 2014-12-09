package eu.trentorise.opendata.trasceprov.test;

import eu.trentorise.opendata.traceprov.impl.TraceProvUtils;
import org.junit.Assert;
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
}
