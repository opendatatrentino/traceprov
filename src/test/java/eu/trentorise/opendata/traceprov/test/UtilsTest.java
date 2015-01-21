/* 
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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


import eu.trentorise.opendata.commons.BuildInfo;
import eu.trentorise.opendata.commons.OdtUtils;
import eu.trentorise.opendata.traceprov.TraceProvConfig;
import java.util.Locale;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class UtilsTest {
    
    @BeforeClass
    public static void setUpClass() {        
        TraceProvConfig.of().loadLogConfig();
    }    
    
    @Test
    public void testChecker(){
        try {
            OdtUtils.checkNotEmpty(null, "my string");
            Assert.fail();
        } catch (NullPointerException ex){
            
        }
        try {
            OdtUtils.checkNonEmpty("", "my string");
            Assert.fail();
        } catch (IllegalArgumentException ex){
            
        }
                
    }
    
    @Test
    public void testLanguageTag(){
        // we want gracious null handling
        assertEquals(Locale.ROOT, OdtUtils.languageTagToLocale(null));
        assertEquals("", OdtUtils.localeToLanguageTag(null));
    }
    
    @Test
    public void testBuildInfo(){
        BuildInfo buildInfo = OdtUtils.readBuildInfo(OdtUtils.class);
        assertTrue(buildInfo.getScmUrl().length() > 0);
        assertTrue(buildInfo.getVersion().length() > 0);
    }
    
}
