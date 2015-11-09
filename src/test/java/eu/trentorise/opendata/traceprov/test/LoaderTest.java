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

import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.traceprov.test.services.UrlLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class LoaderTest {
    
    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(LoaderTest.class);
    }
    
    @Test
    @Ignore
    public void loadFromUrlTest() throws FileNotFoundException, IOException {
        File tempFile = File.createTempFile("traceprov-temp-", "");
        try (OutputStream output = new FileOutputStream(tempFile)) {
            new UrlLoader().loadData(new URL("file://src/test/resources/test-1.csv"), output);
        }
        
        InputStream input = new FileInputStream(tempFile);
        
        String theString = IOUtils.toString(input, "UTF-8"); 
        assertEquals("a", theString);
        input.close();
    }
}
