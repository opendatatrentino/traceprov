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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.jackson.OdtCommonsModule;
import eu.trentorise.opendata.commons.test.jackson.OdtJacksonTester;
import eu.trentorise.opendata.traceprov.TraceProvModule;
import eu.trentorise.opendata.traceprov.geojson.Feature;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.geojson.MultiPoint;
import eu.trentorise.opendata.traceprov.geojson.Point;
import eu.trentorise.opendata.traceprov.geojson.Polygon;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertTrue;
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
        TraceProvModule.registerModulesInto(objectMapper);
    }

    @After
    public void after() {
        objectMapper = null;
    }

    @Test
    public void testGeoJsonFeature() throws JsonProcessingException, IOException {
        HashMap hm = new HashMap();
        hm.put("x", "y");
        //String json = objectMapper.writeValueAsString();

        //objectMapper.readValue(json, Feature.class);
        OdtJacksonTester.testJsonConv(objectMapper, LOG, Feature.builder().setOthers(hm).build());

        OdtJacksonTester.testJsonConv(objectMapper, LOG, Point.of(1.0, 2.0));
        
        OdtJacksonTester.testJsonConv(objectMapper, LOG, Point.of(1.0, 2.0), GeoJson.class);

        OdtJacksonTester.testJsonConv(objectMapper, LOG, MultiPoint.builder().addCoordinates(ImmutableList.of(1.1, 1.2)).build());

        OdtJacksonTester.testJsonConv(objectMapper, LOG, Polygon.builder().addCoordinates(
                ImmutableList.of(
                        ImmutableList.of(1.1, 1.2),
                        ImmutableList.of(1.1, 1.2),
                        ImmutableList.of(1.1, 1.2),
                        ImmutableList.of(1.1, 1.2))).build());
    }

    @Test
    public void testPolygon() throws IOException {
        GeoJson geoJson = objectMapper.readValue("{\"type\":\"Polygon\",\"coordinates\":[[[1.1, 1.2],[2.1, 2.2], [3.1, 3.2], [1.1, 1.2]]]}", GeoJson.class);
        
        Polygon pol = (Polygon) geoJson;
        Double d = pol.getCoordinates().get(0).get(0).get(0);
        assertTrue(1.0 < d);
        assertTrue(d < 1.2);
    }

}
