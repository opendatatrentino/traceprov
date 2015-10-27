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

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.test.jackson.OdtJacksonTester;
import eu.trentorise.opendata.traceprov.TraceProvModule;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.dcat.FoafOrganization;

import java.util.Locale;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DcatTest extends TraceProvTest {
    private static final Logger LOG = Logger.getLogger(DcatTest.class.getName());                 

    @BeforeClass
    public static void setUpClass() {
        OdtConfig.init(DcatTest.class);
    }    
           
    
    @Test    
    public void testJson(){
                 
	OdtJacksonTester.testJsonConv(objectMapper, LOG, FoafOrganization.builder().build());
	
	OdtJacksonTester.testJsonConv(objectMapper, LOG, FoafAgent.builder().build());
	
        OdtJacksonTester.testJsonConv(objectMapper, LOG, DcatDataset.builder().build());
        
        OdtJacksonTester.testJsonConv(objectMapper, LOG, 
        	FoafAgent.builder()
        	.setHomepage("http://hello")
        	.build());
        
        OdtJacksonTester.testJsonConv(objectMapper, LOG, 
        	DcatDataset.builder()
        	.addLanguages(Locale.ROOT)
        	.addKeywords("hello")
        	.setPublisher(
        		FoafAgent.builder()
        		.setHomepage("http://hello")
        		.build())	
        	.build());
    }
    
    
    
}
