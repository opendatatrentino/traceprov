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

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.LocalizedString;
import eu.trentorise.opendata.traceprov.TraceProvConfig;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DictTest {

    @BeforeClass
    public static void setUpClass() {        
        TraceProvConfig.of().loadLogConfig();
    }    

    @Test
    public void testDict_0() {
        Dict dict = Dict.of();
        assertTrue(dict.locales().isEmpty());        
    }
    
    
      @Test
    public void testDict_1() {        
        Dict dict = Dict.of("a");
        assertEquals(dict.string(Locale.ROOT), "a");        
    }
    
    @Test
    public void testDict() {
        assertTrue(Dict.of().isEmpty());
        assertTrue(Dict.of("").isEmpty());
        assertFalse(Dict.of("hello").isEmpty());

        Dict dict = Dict.builder()
                .putAll(Locale.FRENCH, "A")
                .putAll(Locale.ITALIAN, "b")
                .putAll(Locale.ENGLISH, "c")
                .build();

        assertTrue(dict.contains("a"));
        assertTrue(dict.contains("B"));

        assertEquals(LocalizedString.of(Locale.ENGLISH, "c"),
                     dict.prettyString(Locale.CHINESE));
        
        assertEquals(LocalizedString.of(), Dict.of().prettyString(Locale.ITALIAN));

        assertEquals(Dict.of("b", "b"),
                     Dict.builder().putAll(Dict.of("b")).putAll(Dict.of("b")).build());

        
                
        assertEquals(Dict.builder().putAll(Dict.of(Locale.GERMAN, "b")).putAll(Dict.of(Locale.ITALIAN, "a")).build(),
                     Dict.builder().putAll(Dict.of(Locale.ITALIAN, "a")).putAll(Dict.of(Locale.GERMAN, "b")).build()
        );
        
        
        
    }
    
    @Test
    public void testNonEmpty(){
        Dict dict = Dict.builder().putAll(Locale.FRENCH, "", "a").build();
        assertEquals("a", dict.nonEmptyString(Locale.FRENCH));        
    }
    
    @Test
    public void testWith(){
        assertEquals(Dict.of("a").with("b"), Dict.of("a","b"));
        assertNotEquals(Dict.of("b").with("a"), Dict.of("a","b"));
        assertEquals(Dict.of("a").with(Locale.ITALIAN, "b"), Dict.of(Locale.ITALIAN, "b").with("a"));
        assertNotEquals(Dict.of(Locale.ENGLISH, "a").with(Locale.ITALIAN, "b"), Dict.of(Locale.ITALIAN, "b").with("a"));
    }
    
    
}
