package eu.trentorise.opendata.traceprov.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.traceprov.tracel.Tracel;

public class TracelTest {

    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(TracelTest.class);
    }

    @Test
    public void testTracel() {
        assertTrue(Tracel.isValidId("$"));
        assertTrue(Tracel.isValidId("a1"));
        
        try {
            Tracel.checkValidId("");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Tracel.checkValidId("1");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Tracel.checkValidId("1a");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Tracel.checkValidId("a b");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Tracel.checkValidId("a ");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Tracel.checkValidId(" a");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        
    }
}
