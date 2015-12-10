package eu.trentorise.opendata.traceprov.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.traceprov.engine.Engine;
import eu.trentorise.opendata.traceprov.tracel.java.PropertyPath;

public class EngineTest {

    private Engine engine;
    
    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(EngineTest.class);        
    }

    @Before
    public void before(){
        engine = Engine.of();
    }
    
    @Before
    public void after(){
        engine = null;
    }
    
    @Test
    public void testId() {
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
    
    @Test
    public void testScripting(){
        Number n = (Number) engine.execute("1 + 1");
        assertEquals(2, n.intValue());
        
        assertEquals(PropertyPath.of("a","b"), PropertyPath.parse("a.b"));        
    }
}
