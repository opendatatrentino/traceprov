/* 
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.traceprov.data.NodeMap;
import eu.trentorise.opendata.traceprov.data.ProvFile;
import eu.trentorise.opendata.traceprov.data.ProvSchema;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.schema.CellRef;
import eu.trentorise.opendata.traceprov.schema.Mapping;
import eu.trentorise.opendata.traceprov.schema.DocRef;
import eu.trentorise.opendata.traceprov.schema.Schema;
import eu.trentorise.opendata.traceprov.schema.SchemaRef;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author David Leoni
 */
public class CodeGenTest {

    @BeforeClass
    public static void setUpClass() {
        OdtConfig.init(CodeGenTest.class);
    }

    @Test
    public void testDcat() {
        DcatDataset dataset = DcatDataset.of();

        assertEquals("", dataset.getUri());

        assertEquals("a", DcatDataset.builder().setUri("a").build().getUri());

        assertEquals("b", dataset.withUri("b").getUri());

        DcatDataset.builder().addAllDistributions(DcatDataset.of().getDistributions());
        FoafAgent.builder();
    }

    @Test
    public void dcataDatasetUsageExample() {
        DcatDataset dataset = DcatDataset
                .builder()
                .setTitle(Dict.of(Locale.ITALIAN, "Impianti di risalita, ViviFiemme 2013"))
                .setLandingPage("http://dati.trentino.it/dataset/impianti-di-risalita-vivifiemme-2013")
                .build();
        assertEquals(null, dataset.getModified());
    }

    @Test
    public void testRef() {

        assertEquals(CellRef.of(), CellRef.of(0, 0));
        assertEquals(CellRef.of().withDocumentId("a"), CellRef.of(0, 0).withDocumentId("a"));
        assertEquals(1, CellRef.of(1, 0).getRowIndex());

    }

    @Test
    public void testDataModel() {
        assertEquals(ProvFile.of().getData(), NodeMap.of());
        assertEquals(ProvFile.of().getSchemaMappings(), ImmutableList.of());

        ProvFile.builder().addSchemaMappings(Mapping.of(DocRef.of(),
                        SchemaRef.of(ImmutableList.of("a", "b"))
                ));

        SchemaRef schemaRef
                = SchemaRef.builder()
                .setDocumentId("http://mysite/res.csv")
                .setPhysicalColumn(3)
                .setPhysicalRow(2)
                .addPropertyIds("a", "b")
                .build();
        
        ProvSchema ps = ProvSchema.builder().setSchema(Schema.of()).build();
        assertEquals(ImmutableList.of(), ps.getErrors());
        
    }

}
