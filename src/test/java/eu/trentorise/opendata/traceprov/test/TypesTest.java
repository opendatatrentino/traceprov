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
import com.google.common.reflect.ClassPath;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.test.jackson.OdtJacksonTester;
import eu.trentorise.opendata.traceprov.TraceProvModule;
import eu.trentorise.opendata.traceprov.geojson.Feature;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.types.Def;
import eu.trentorise.opendata.traceprov.types.IntType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;
import eu.trentorise.opendata.traceprov.types.Types;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class TypesTest {

    private static final Logger LOG = Logger.getLogger(TypesTest.class.getName());

    private ObjectMapper objectMapper;

    @BeforeClass
    public static void setUpClass() {
        OdtConfig.init(TypesTest.class);
    }

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
    public void testDef() {
        OdtJacksonTester.testJsonConv(objectMapper, LOG,
                Def.builder()
                .setOriginId("bla")
                .setDescription(Dict.of("ciao"))
                .setType(IntType.of())
                .build());
    }

    @Test
    public void testCheckTypeId() {
        
        Types.checkTypeId("a", null);
        Types.checkTypeId("a<>", null);
        Types.checkTypeId("a<IntType>", null);
        Types.checkTypeId("a<IntType<MapType<StringType,IntType>>>", null);
        
        try {
            Types.checkTypeId("", null);
            Assert.fail("Shouldn't arrive here!");
        }
        catch (Exception ex) {

        }

        try {
            Types.checkTypeId("a<", null);
            Assert.fail("Shouldn't arrive here!");
        }
        catch (Exception ex) {

        }
        
    }
    
    /**
     * todo...
     */
    @Test
    @Ignore
    public void testCheckTypeIdIrregularNesting(){
        try {
            Types.checkTypeId("a<<>", null);
            Assert.fail("Shouldn't validate irregular nesting: a<<>");
        }
        catch (Exception ex) {

        }
        
    }

    @Test
    public void testStripTypeIdGenerics() {

        assertEquals("a", Types.stripGenerics("a"));
        assertEquals("a", Types.stripGenerics("a<IntType>"));

        assertEquals("a.b", Types.stripGenerics("a.b<IntType>"));

        assertEquals("a.b", Types.stripGenerics("a.b<IntType, MapType<StringType, FloatType>>"));

    }

    @Test
    @Ignore
    public void testRegistry() throws IOException {

        TypeRegistry defaultRegistry = TypeRegistry.of();

        String packagenom = Types.class.getPackage().getName();

        final ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();

        ClassPath classpath = ClassPath.from(loader); // scans the class path used by classloader
        for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses(packagenom)) {
            if (!classInfo.getSimpleName().endsWith("Type")) {
                //defaultRegistry.getDatatype(packagenom);
                // todo
            }
        }
    }
}
