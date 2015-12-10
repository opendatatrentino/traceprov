package eu.trentorise.opendata.traceprov.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.traceprov.engine.Engine;

public class EngineTest {

    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(EngineTest.class);
    }

    @Test
    public void testEngine() {
        assertTrue(Engine.isValidId("$"));
        assertTrue(Engine.isValidId("a1"));
        
        try {
            Engine.checkValidId("");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Engine.checkValidId("1");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Engine.checkValidId("1a");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Engine.checkValidId("a b");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Engine.checkValidId("a ");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        try {
            Engine.checkValidId(" a");
            Assert.fail("Shouldn't arrive here");
        } catch (IllegalArgumentException ex){
            
        }
        
        
    }
}
