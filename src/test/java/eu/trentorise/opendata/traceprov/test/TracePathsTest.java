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
import eu.trentorise.opendata.traceprov.tracel.TracePaths;
import eu.trentorise.opendata.traceprov.types.TraceRefs;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class TracePathsTest {

    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(TracePathsTest.class);
    }

    @Test
    public void testTable() {
        try {
            TracePaths.tablePath(-2, 0);
        } catch (IllegalArgumentException ex) {

        }

        try {
            TracePaths.tablePath(0, -2);
        } catch (IllegalArgumentException ex) {

        }

        assertEquals("$[0][ALL]", TracePaths.tablePath(0, -1));

        assertEquals("$[ALL][0]", TracePaths.tablePath(-1, 0));

        assertEquals("$[ALL][ALL]", TracePaths.tablePath(-1, -1));

        assertEquals("$[0].ALL", TracePaths.tablePath(0, "ALL"));

        assertEquals("$[ALL].a", TracePaths.tablePath(-1, "a"));

        assertEquals("$[ALL].ALL", TracePaths.tablePath(-1, "ALL"));

    }

    @Test
    public void testDataNodes() {
        try {
            assertEquals(Ref.builder()
                            .setDocumentId(DataNodes.DATANODES_IRI)
                            .setTracePath("$[ALL]")
                            .build(),
                    TraceRefs.dataNodesRef(ImmutableList.<Long> of()));
            Assert.fail("Shouldn't arrive here!");
        } catch (IllegalArgumentException ex) {
        }

        assertEquals(Ref.builder()
                        .setDocumentId(DataNodes.DATANODES_IRI)
                        .setTracePath("$[1,3]")
                        .build(),
                TraceRefs.dataNodesRef(ImmutableList.of(1L, 3L)));
    }
/*
    @Test
    public void testDcat() {
        assertEquals("catalog", PropertyPath.of(DcatMetadata.class, "catalog"));

        try {
            PropertyPath.of(DcatMetadata.class, "bla");
            Assert.fail();
        } catch (IllegalArgumentException ex) {

        }

        try {
            PropertyPath.of(DcatMetadata.class, ImmutableList.<String> of());
        } catch (IllegalArgumentException ex) {

        }

        try {
            PropertyPath.of(DcatMetadata.class, " ", "publisher");
        } catch (IllegalArgumentException ex) {

        }

        assertEquals("catalog.publisher", PropertyPath.of(DcatMetadata.class, "catalog", "publisher"));

        assertEquals("dataset.themes[ALL].uri", PropertyPath.of(DcatMetadata.class, "dataset", "themes", "uri"));
    }
*/
}
