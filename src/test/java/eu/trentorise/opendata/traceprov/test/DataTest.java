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
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.NodeList;
import eu.trentorise.opendata.traceprov.data.NodeMap;
import eu.trentorise.opendata.traceprov.data.NodeValue;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DataTest {
    
    
    @Test
    public void testWalkerValue(){                                          
        assertEquals(null, NodeValue.of().asSimpleType());
        assertEquals("a", NodeValue.of("a").asSimpleType());
        assertEquals(Lists.newArrayList(), NodeList.of().asSimpleType());        
        assertEquals(Lists.newArrayList("a"), NodeList.of(NodeValue.of("a")).asSimpleType());
        assertEquals(Lists.newArrayList("a"), NodeList.of(NodeValue.of("a")).asSimpleType());        
    }
    
    @Test
    public void testWalker(){                                  
        
        assertEquals(new HashMap(), NodeMap.of().asSimpleType());
        
        Object res = NodeMap.of(Ref.of(), 
                                ImmutableMap.of("a", NodeValue.of("b"),
                                                "c", NodeValue.of("d"))).asSimpleType();
        
        HashMap hm = (HashMap) res;
        
        assertEquals(2, hm.size());
        assertEquals("b", hm.get("a"));
        assertEquals("d", hm.get("c"));
    }
}
