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
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.test.jackson.OdtJacksonTester;
import eu.trentorise.opendata.traceprov.TraceProvModule;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.ClassType;
import eu.trentorise.opendata.traceprov.types.Def;
import eu.trentorise.opendata.traceprov.types.DefMetadata;
import eu.trentorise.opendata.traceprov.types.FunctionType;
import eu.trentorise.opendata.traceprov.types.IntType;
import eu.trentorise.opendata.traceprov.types.ListType;
import eu.trentorise.opendata.traceprov.types.StringType;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;
import eu.trentorise.opendata.traceprov.types.TypeVisitor;
import eu.trentorise.opendata.traceprov.types.TraceTypes;
import eu.trentorise.opendata.traceprov.types.TupleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
public class TypesTest extends TraceProvTest {

    private static final Logger LOG = Logger.getLogger(TypesTest.class.getName());
    

    @BeforeClass
    public static void setUpClass() {
        OdtConfig.init(TypesTest.class);
    }

    
    @Test
    public void testDefMetadataJackson() {
        OdtJacksonTester.testJsonConv(objectMapper, LOG, DefMetadata.builder()
                .setOriginId("bla")
                .setDescription(Dict.of("ciao"))
                .build());
    }

    @Test
    public void testDefJackson() {
        OdtJacksonTester.testJsonConv(objectMapper, LOG,
                Def.builder()
                .setMetadata(
                        DefMetadata.builder()
                        .setOriginId("bla")
                        .setDescription(Dict.of("ciao"))
                        .build())
                .setType(IntType.of())
                .build());
    }

    @Test
    public void testTypeJackson() {
	for (TraceType t : TypeRegistry.of().getTypes().values()){
	    try {
		OdtJacksonTester.testJsonConv(objectMapper, LOG, t);
	    } catch (Exception ex){
		throw new RuntimeException("JACKSON ERROR FOR TYPE: " + t.toString(), ex);
	    }
	}
    }
    
    
    @Test
    public void testCheckTypeId() {

        TraceTypes.checkTypeId("a", null);
        TraceTypes.checkTypeId("a<>", null);
        TraceTypes.checkTypeId("a<IntType>", null);
        TraceTypes.checkTypeId("a<IntType<MapType<StringType,IntType>>>", null);

        try {
            TraceTypes.checkTypeId("", null);
            Assert.fail("Shouldn't arrive here!");
        }
        catch (Exception ex) {

        }

        try {
            TraceTypes.checkTypeId("a<", null);
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
    public void testCheckTypeIdIrregularNesting() {
        try {
            TraceTypes.checkTypeId("a<<>", null);
            Assert.fail("Shouldn't validate irregular nesting: a<<>");
        }
        catch (Exception ex) {

        }

    }

    @Test
    public void testStripTypeIdGenerics() {

        assertEquals("a", TraceTypes.stripGenerics("a"));
        assertEquals("a", TraceTypes.stripGenerics("a<IntType>"));

        assertEquals("a.b", TraceTypes.stripGenerics("a.b<IntType>"));

        assertEquals("a.b", TraceTypes.stripGenerics("a.b<IntType, MapType<StringType, FloatType>>"));

    }

    @Test
    @Ignore
    public void testRegistry() throws IOException {

        TypeRegistry defaultRegistry = TypeRegistry.of();

        String packagenom = TraceTypes.class.getPackage().getName();

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

    public static class TestPrinter extends TypeVisitor {

        List<TraceType> visitedNodes = new ArrayList();
        List<Class<? extends TraceType>> calledMethods = new ArrayList();

        @Override
        public void visitDefault(TraceType type) {
            LOG.log(Level.FINE, "default visit, type id is {0}", type.getId());
            visitedNodes.add(type);
            calledMethods.add(TraceType.class);
        }

        public void visit(AnyType type) {           
            visitedNodes.add(type);
            calledMethods.add(AnyType.class);
        }

    }

    @Test
    public void testVisitorNoSpecificMethod() {
        TypeVisitor vis = new TestPrinter();
        vis.visit(IntType.of());
        assertEquals(ImmutableList.of(IntType.of()), ((TestPrinter) vis).visitedNodes);
        assertEquals(ImmutableList.of(TraceType.class), ((TestPrinter) vis).calledMethods);
    }

    @Test
    public void testVisitorWithSpecificMethod() {
        TypeVisitor vis = new TestPrinter();
        vis.visit(AnyType.of());
        assertEquals(ImmutableList.of(AnyType.of()), ((TestPrinter) vis).visitedNodes);
        assertEquals(ImmutableList.of(AnyType.class), ((TestPrinter) vis).calledMethods);
    }

    @Test
    public void testClassType() {
        ClassType myPersonType = ClassType
                .builder()
                .setId("org.mycompany.Person")
                .putPropertyDefs(
                        "age", // property name within the class, short, english and camelcased
                        Def
                        .builder()
                        // id for eventual condivision of property def with other types. 
                        .setId("org.mycompany.properties.age")
                        .setType(IntType.of())
                        .build())
                .putPropertyDefs(
                        "name",
                        Def
                        .builder()
                        .setId("org.mycompany.properties.name")
                        .setType(StringType.of())
                        .build())
                .putMethodDefs("walk",
                        Def
                        .builder()
                        .setId("org.mycompany.methods.walk")
                        .setType(FunctionType.of()) // todo add better example                                
                        .build())
                .build();

    }

}
