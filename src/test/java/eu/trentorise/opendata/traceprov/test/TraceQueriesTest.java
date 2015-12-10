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
import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.DataNodes;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import eu.trentorise.opendata.traceprov.engine.Engine;
import eu.trentorise.opendata.traceprov.types.TraceRefs;
import eu.trentorise.opendata.traceprov.tracel.java.PropertyPath;
import eu.trentorise.opendata.traceprov.tracel.java.TraceQueries;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class TraceQueriesTest {

    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(TraceQueriesTest.class);
    }   
    
    @Test
    public void testTable() {
        try {
            Engine.tablePath(TraceQueries.ROOT_EXPR, -2L, 0L);
        } catch (IllegalArgumentException ex) {

        }

        try {
            Engine.tablePath(TraceQueries.ROOT_EXPR, 0, -2);
        } catch (IllegalArgumentException ex) {

        }

        assertEquals("S[0][ALL]", Engine.tablePath(TraceQueries.ROOT_EXPR,0, -1).toText());

        assertEquals("S[ALL][0]", Engine.tablePath(TraceQueries.ROOT_EXPR,-1, 0).toText());

        assertEquals("S[ALL][ALL]", Engine.tablePath(TraceQueries.ROOT_EXPR,-1, -1).toText());

        assertEquals("S[0][ALL]", Engine.tablePath(TraceQueries.ROOT_EXPR,0, "ALL").toText());

        assertEquals("S[ALL].a", Engine.tablePath(TraceQueries.ROOT_EXPR,-1, "a").toText());

        assertEquals("S[ALL][ALL]", Engine.tablePath(TraceQueries.ROOT_EXPR,-1, "ALL").toText());

    }

    @Test
    @Ignore
    public void testDataNodes() {
        try {
            assertEquals(Ref.builder()
                            .setDocumentId(DataNodes.DATANODES_IRI)
                            .setTracePath("S[ALL]")
                            .build(),
                    TraceRefs.dataNodesRef(ImmutableList.<Long> of()));
            Assert.fail("Shouldn't arrive here!");
        } catch (IllegalArgumentException ex) {
        }

        assertEquals(Ref.builder()
                        .setDocumentId(DataNodes.DATANODES_IRI)
                        .setTracePath("S[1,3]")
                        .build(),
                TraceRefs.dataNodesRef(ImmutableList.of(1L, 3L)));
    }

    @Test
    public void testDcat() {
        Engine.checkPathFromClass(DcatMetadata.class, PropertyPath.of( "catalog"));
        

        try { // checks PropertyPath is *not* smart
            Engine.checkPathFromClass( DcatMetadata.class, PropertyPath.of("this", "catalog"));
            Assert.fail("Shoudln't arrive here!");
        } catch (IllegalArgumentException ex) {

        }

        try {
            Engine.checkPathFromClass(DcatMetadata.class, PropertyPath.of("this"));
            Assert.fail("Shoudln't arrive here!");
            
        } catch (IllegalArgumentException ex) {

        }

        try {           
            Engine.checkPathFromClass(DcatMetadata.class, PropertyPath.of(TraceQueries.ROOT.getLabel(),
                    " ", 
                    "publisher"));
            Assert.fail("Shoudln't arrive here!");
        } catch (IllegalArgumentException ex) {

        }

        Engine.checkPathFromClass(DcatMetadata.class, PropertyPath.of("catalog", "publisher"));

        Engine.checkPathFromClass(DcatMetadata.class, PropertyPath.of("dataset", "themes", "uri"));
    }

}
