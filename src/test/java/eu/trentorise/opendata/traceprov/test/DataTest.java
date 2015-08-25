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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.DataArray;
import eu.trentorise.opendata.traceprov.data.DataMap;
import eu.trentorise.opendata.traceprov.data.DataNode;
import eu.trentorise.opendata.traceprov.data.NodeMetadata;
import eu.trentorise.opendata.traceprov.data.DataValue;
import eu.trentorise.opendata.traceprov.types.ClassType;
import eu.trentorise.opendata.traceprov.types.Def;
import eu.trentorise.opendata.traceprov.types.IntType;
import eu.trentorise.opendata.traceprov.types.ListType;
import eu.trentorise.opendata.traceprov.types.StringType;
import eu.trentorise.opendata.traceprov.types.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public void testWalkerValue() {
        assertEquals(null, DataValue.of().asSimpleType());
        assertEquals("a", DataValue.of("a").asSimpleType());
        assertEquals(Lists.newArrayList(), DataArray.of().asSimpleType());
        assertEquals(Lists.newArrayList("a"), DataArray.of(DataValue.of("a")).asSimpleType());
        assertEquals(Lists.newArrayList("a"), DataArray.of(DataValue.of("a")).asSimpleType());
    }

    @Test
    public void testNodeListToString() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 10000; i++) {
            list.add(DataValue.of(3));
        }
        DataArray nodes = DataArray.of(list);
        assertTrue(nodes.toString().contains("..."));
        assertFalse(DataArray.of(DataValue.of()).toString().contains("..."));
    }

    @Test
    public void testWalker() {

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

    /**
     * A little trial to fetch stuff from a CSV-like type
     * {@code ListType<ClassType>}
     */
    @Test
    public void testCsvExample() {

        String prop1Id = "my.org.dati.trentino.it.ProdottiCertificati.myProp1";
        String prop2Id = "my.org.dati.trentino.it.ProdottiCertificati.myProp2";

        String prop1ShortId = "p1";
        String prop2ShortId = "p2";

        Def prop1 = Def.builder().setId(prop1Id).setType(StringType.of()).build();
        Def prop2 = Def.builder().setId(prop2Id).setType(IntType.of()).build();

        Type traceType = ListType.of(ClassType.builder()
                .putPropertyDefs(prop1ShortId, prop1) // this really makes no sense
                .putPropertyDefs(prop2ShortId, prop2)
                .build()
        );

        DataNode data = DataArray.of(
                DataMap.of(
                        Ref.of(),
                        NodeMetadata.of(),
                        ImmutableMap.of(prop1ShortId, DataValue.of("a"),
                                prop2ShortId, DataValue.of(3))),
                DataMap.of(
                        Ref.of(),
                        NodeMetadata.of(),
                        ImmutableMap.of(prop1ShortId, DataValue.of("b"),
                                prop2ShortId, DataValue.of(4)))
        );

        if (traceType instanceof ListType) {
            ListType listType = (ListType) traceType;
            Type subtype = listType.getSubtype();
            if (subtype instanceof ClassType) {
                ClassType classType = (ClassType) subtype;

                List<String> elNames = classType.getPropertyDefs().keySet().asList();

                List<List<String>> rows = new ArrayList();

                rows.add(elNames);

                DataArray dataArray = (DataArray) data;
                for (DataNode dn : dataArray) {
                    List<String> row = new ArrayList();
                    rows.add(row);
                    DataMap dataMap = (DataMap) dn;
                    for (String key : elNames) {
                        DataValue val = (DataValue) dataMap.get(key);
                        String value =  val.getValue().toString();
                        row.add(value);
                    }
                }
                assertEquals(
                        ImmutableList.of(
                                ImmutableList.copyOf(rows.get(0)),
                                ImmutableList.of("a","3"),
                                ImmutableList.of("b","4"))
                        , rows);
                return;

            }
        }

        throw new RuntimeException("Shouldn't arrive here!");

    }
}
