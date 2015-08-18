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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.DataArray;
import eu.trentorise.opendata.traceprov.data.DataMap;
import eu.trentorise.opendata.traceprov.data.NodeMetadata;
import eu.trentorise.opendata.traceprov.data.DataValue;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DataTest {
    
    @BeforeClass
    public static void setUpClass() {
        OdtConfig.init(DataTest.class);
    }
    
    @Test
    public void testWalkerValue(){                                          
        assertEquals(null, DataValue.of().asSimpleType());
        assertEquals("a", DataValue.of("a").asSimpleType());
        assertEquals(Lists.newArrayList(), DataArray.of().asSimpleType());        
        assertEquals(Lists.newArrayList("a"), DataArray.of(DataValue.of("a")).asSimpleType());
        assertEquals(Lists.newArrayList("a"), DataArray.of(DataValue.of("a")).asSimpleType());        
    }
    
    @Test
    public void testNodeListToString(){
        ArrayList list = new ArrayList();
        for (int i = 0; i <10000; i++){
            list.add(DataValue.of(3));
        }
        DataArray nodes = DataArray.of(list);
        assertTrue(nodes.toString().contains("..."));
        assertFalse(DataArray.of(DataValue.of()).toString().contains("..."));
    }    
    
    @Test
    public void testWalker(){                                  
        
        assertEquals(new HashMap(), DataMap.of().asSimpleType());
        
        Object res = DataMap.of(Ref.of(), 
                                NodeMetadata.of(),
                                ImmutableMap.of("a", DataValue.of("b"),
                                                "c", DataValue.of("d"))).asSimpleType();
        
        HashMap hm = (HashMap) res;
        
        assertEquals(2, hm.size());
        assertEquals("b", hm.get("a"));
        assertEquals("d", hm.get("c"));
    }
}
