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
import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.commons.test.jackson.TodJacksonTester;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.DataArray;
import eu.trentorise.opendata.traceprov.data.DataMap;
import eu.trentorise.opendata.traceprov.data.TraceData;
import eu.trentorise.opendata.traceprov.data.DataObject;
import eu.trentorise.opendata.traceprov.data.NodeMetadata;
import eu.trentorise.opendata.traceprov.data.DataValue;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.ClassType;
import eu.trentorise.opendata.traceprov.types.Def;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.types.IntType;
import eu.trentorise.opendata.traceprov.types.ListType;
import eu.trentorise.opendata.traceprov.types.StringType;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DataTest extends TraceProvTest {

    private static final Logger LOG = Logger.getLogger(DataTest.class.getCanonicalName());
    
    private TypeRegistry emptyReg;
  
    @Before
    public void before() {
    	super.before();
    	emptyReg = TypeRegistry.empty();
    }

    @After
    public void after() {
    	super.after();
    	emptyReg = null;
    	TraceDb.getDb().setTypeRegistry(TypeRegistry.of());
    }

    @Test
    public void testWalkerValue() {
    	DataValue dv = DataValue.of(Ref.of(), NodeMetadata.of(), "a");
    	TraceDb.getDb().setTypeRegistry(emptyReg);
    	assertEquals(null, DataValue.of().asSimpleType());
    	assertEquals("a", dv.asSimpleType());
    	assertEquals(Lists.newArrayList(), DataArray.of().asSimpleType());
    	assertEquals(Lists.newArrayList("a"), DataArray.of(Ref.of(), NodeMetadata.of(), dv).asSimpleType());
    	assertEquals(Lists.newArrayList("a"), DataArray.of(Ref.of(), NodeMetadata.of(), dv).asSimpleType());
    }

    @Test
    public void testNodeListToString() {
	ArrayList list = new ArrayList();
	for (int i = 0; i < 10000; i++) {
	    list.add(DataValue.of(Ref.of(), NodeMetadata.of(), 3));
	}
	DataArray nodes = DataArray.of(Ref.of(), NodeMetadata.of(), list);
	assertTrue(nodes.toString().contains("..."));
	assertFalse(DataArray.of(Ref.of(), NodeMetadata.of(), DataValue.of()).toString().contains("..."));
    }

    @Test
    public void testWalker() {

    	TraceDb.getDb().setTypeRegistry(TypeRegistry.empty());
    
    	assertEquals(new HashMap(), DataMap.of().asSimpleType());
    
    	Object res = DataMap.of(Ref.of(),
    		NodeMetadata.of(),
    		ImmutableMap.of("a", DataValue.of(Ref.of(), NodeMetadata.of(), "b"),
    			"c", DataValue.of(Ref.of(), NodeMetadata.of(), "d")))
    		.asSimpleType();
    
    	HashMap hm = (HashMap) res;
    
    	assertEquals(2, hm.size());
    	assertEquals("b", hm.get("a"));
    	assertEquals("d", hm.get("c"));
    }

    /**
     * A little trial to fetch stuff from a CSV-like type {@code ListType
     * <ClassType>}
     */
    @Test
    public void testCsvExample() {

	String prop1Id = "my.org.dati.trentino.it.ProdottiCertificati.myProp1";
	String prop2Id = "my.org.dati.trentino.it.ProdottiCertificati.myProp2";

	String prop1ShortId = "p1";
	String prop2ShortId = "p2";

	Def prop1 = Def.builder().setId(prop1Id).setType(StringType.of()).build();
	Def prop2 = Def.builder().setId(prop2Id).setType(IntType.of()).build();

	TraceType traceType = ListType.of(ClassType.builder()
		.putPropertyDefs(prop1ShortId, prop1) // this really makes no
						      // sense
		.putPropertyDefs(prop2ShortId, prop2)
		.build());

	TraceData data = DataArray.of(
		Ref.of(),
		NodeMetadata.of(),
		DataMap.of(
			Ref.of(),
			NodeMetadata.of(),
			ImmutableMap.of(prop1ShortId, DataValue.of(Ref.of(), NodeMetadata.of(), "a"),
				prop2ShortId, DataValue.of(Ref.of(), NodeMetadata.of(), 3))),
		DataMap.of(
			Ref.of(),
			NodeMetadata.of(),
			ImmutableMap.of(prop1ShortId, DataValue.of(Ref.of(), NodeMetadata.of(), "b"),
				prop2ShortId, DataValue.of(Ref.of(), NodeMetadata.of(), 4))));

	if (traceType instanceof ListType) {
	    ListType listType = (ListType) traceType;
	    TraceType subtype = listType.getSubtype();
	    if (subtype instanceof ClassType) {
		ClassType classType = (ClassType) subtype;

		List<String> elNames = classType.getPropertyDefs().keySet().asList();

		List<List<String>> rows = new ArrayList();

		rows.add(elNames);

		DataArray dataArray = (DataArray) data;
		for (TraceData dn : dataArray) {
		    List<String> row = new ArrayList();
		    rows.add(row);
		    DataMap dataMap = (DataMap) dn;
		    for (String key : elNames) {
			DataValue val = (DataValue) dataMap.get(key);
			String value = val.getRawValue().toString();
			row.add(value);
		    }
		}
		assertEquals(
			ImmutableList.of(
				ImmutableList.copyOf(rows.get(0)),
				ImmutableList.of("a", "3"),
				ImmutableList.of("b", "4")),
			rows);
		return;

	    }
	}

	throw new RuntimeException("Shouldn't arrive here!");

    }

    @Test
    public void testFromThisBuilder() {
	DataObject dn = DataObject.builder().setId(1).build();

	TraceData dn2 = dn.fromThis().setRawValue(2).build();
	assertEquals(1, dn2.getId());
	assertEquals(2, dn2.getRawValue());
    }

    @Test
    public void testGenerics() {
    	DataObject.Builder<String> dosb = DataObject.builder();
    
    	DataObject<String> dos = dosb.setRawValue("a").build();
    
    	assertEquals("a", dos.getRawValue());
    
    	String b = dos.withId(4).getRawValue();

    }

    @Test
    public void testJackson() {
        TodJacksonTester.testJsonConv(objectMapper, LOG, AnyType.of());
    }

}
