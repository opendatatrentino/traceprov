/*
 * Copyright 2015 Trento Rise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.trentorise.opendata.traceprov.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.jackson.OdtCommonsModule;
import eu.trentorise.opendata.commons.test.jackson.OdtJacksonTester;
import eu.trentorise.opendata.traceprov.geojson.Feature;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class JacksonTest {

    private static final Logger LOG = Logger.getLogger(JacksonTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
        OdtConfig.init(JacksonTest.class);
    }
    private ObjectMapper objectMapper;
    
   @Before
    public void before() {
        objectMapper = new ObjectMapper();
        OdtCommonsModule.registerModulesInto(objectMapper);
    }

    @After
    public void after() {
        objectMapper = null;
    }    
    
    @Test
    public void testFeature() throws JsonProcessingException, IOException{
        HashMap hm = new HashMap();
        hm.put("x", "y");
        //String json = objectMapper.writeValueAsString();
        
        //objectMapper.readValue(json, Feature.class);
        
        OdtJacksonTester.testJsonConv(objectMapper, LOG, Feature.builder().setOthers(hm).build());
    }
  
}
