package eu.trentorise.opendata.traceprov.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.commons.jackson.TodCommonsModule;
import eu.trentorise.opendata.traceprov.TraceProvModule;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;

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

    @Test
    public void quickJacksonRegistrationExample() throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper om = new ObjectMapper();
        TraceProvModule.registerModulesInto(om); 
        
        String json = om.writeValueAsString(
                DcatDataset.builder()
                .setTitle(Dict.of("hello"))
                .build());
        System.out.println(json);
        DcatDataset reconstructedDataset = om.readValue(json, DcatDataset.class);
    }
    
    @Test
    public void jacksonExample() throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        om.registerModule(new TodCommonsModule());
        om.registerModule(new TraceProvModule());

        String json = om.writeValueAsString(
                DcatDataset.builder()
                .setTitle(Dict.of("hello"))
                .build());
        System.out.println(json);
        DcatDataset reconstructedDataset = om.readValue(json, DcatDataset.class);
        
    }
}
