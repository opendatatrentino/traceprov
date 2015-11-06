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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.test.jackson.OdtJacksonTester;
import eu.trentorise.opendata.traceprov.geojson.Crs;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.geojson.MultiPoint;
import eu.trentorise.opendata.traceprov.geojson.Point;
import eu.trentorise.opendata.traceprov.geojson.Polygon;

/**
 *
 * @author David Leoni
 */
public class GeoJsonTest extends TraceProvTest {

    private static final Logger LOG = Logger.getLogger(GeoJsonTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
	OdtConfig.init(GeoJsonTest.class);
    }

    @Test
    public void testPoint() {
	try {
	    Point.builder().setCoordinates(ImmutableList.of(1.0)).build();
	    Assert.fail();
	} catch (IllegalStateException ex) {

	}
    }

    @Test
    public void testJacksonGeoJsonFeature() throws JsonProcessingException, IOException {

	OdtJacksonTester.testJsonConv(objectMapper, LOG, Point.of(1.0, 2.0));

	OdtJacksonTester.testJsonConv(objectMapper, LOG, Point.of(1.0, 2.0), GeoJson.class);

	OdtJacksonTester.testJsonConv(objectMapper, LOG,
		MultiPoint.builder().addCoordinates(ImmutableList.of(1.1, 1.2)).build(), GeoJson.class);

	OdtJacksonTester.testJsonConv(objectMapper, LOG, Polygon.builder().addCoordinates(
		ImmutableList.of(
			ImmutableList.of(1.1, 1.2),
			ImmutableList.of(1.1, 1.2),
			ImmutableList.of(1.1, 1.2),
			ImmutableList.of(1.1, 1.2)))
		.build());

	OdtJacksonTester.testJsonConv(objectMapper, LOG, Crs.ofName("a"));
	OdtJacksonTester.testJsonConv(objectMapper, LOG,
		Crs.of("name", ImmutableMap.of(
			"name", "hello",
			"x", ImmutableMap.of("x", ImmutableList.of(1, "w")))));

    }

    /**
     * todo 'Others' field seem problematic with Immutables. See https://github.com/immutables/immutables/issues/185
     */
    @Test
    @Ignore
    public void testJacksonGeoJsonOthers(){
	HashMap hm = new HashMap();
        hm.put("x", "y");
        //String json = objectMapper.writeValueAsString();

        //objectMapper.readValue(json, Feature.class);
        //OdtJacksonTester.testJsonConv(objectMapper, LOG, Feature.builder().setOthers(hm).build(), GeoJson.class);

    }

    @Test
    public void testJacksonPolygon() throws IOException {
	GeoJson geoJson = objectMapper.readValue(
		"{\"type\":\"Polygon\",\"coordinates\":[[[1.1, 1.2],[2.1, 2.2], [3.1, 3.2], [1.1, 1.2]]]}",
		GeoJson.class);

	Polygon pol = (Polygon) geoJson;
	Double d = pol.getCoordinates().get(0).get(0).get(0);
	assertTrue(1.0 < d);
	assertTrue(d < 1.2);
    }

}
