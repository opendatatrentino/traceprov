package eu.trentorise.opendata.traceprov.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.traceprov.TraceProvModule;

public class TraceProvTest {

    protected ObjectMapper objectMapper;    

    @Before
    public void before() {
        objectMapper = new ObjectMapper();
        TraceProvModule.registerModulesInto(objectMapper);
    }

    @After
    public void after() {
        objectMapper = null;
    }

    
}
