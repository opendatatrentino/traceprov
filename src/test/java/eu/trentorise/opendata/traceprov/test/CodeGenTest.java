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
import com.google.common.collect.ImmutableMap;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.OdtConfig;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.NodeList;
import eu.trentorise.opendata.traceprov.data.NodeMap;
import eu.trentorise.opendata.traceprov.data.NodeMetadata;
import eu.trentorise.opendata.traceprov.data.NodeValue;
import eu.trentorise.opendata.traceprov.data.ProvFile;
import eu.trentorise.opendata.traceprov.data.ProvType;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.types.ProvRefs;
import eu.trentorise.opendata.traceprov.types.PropertyMapping;
import eu.trentorise.opendata.traceprov.types.Type;
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
        assertEquals("", dataset.getModified());
    }

    @Test
    public void testRef() {
        assertEquals("0.0", ProvRefs.tablePath(0, 0));        
        assertEquals("*.1", ProvRefs.tablePath(-1, 1));  
        assertEquals("1.*", ProvRefs.tablePath(1, -1));          
        assertEquals("*", ProvRefs.tablePath(-1, -1));
    }

    @Test
    public void testDataModel() {
        assertEquals(ProvFile.of().getData(), NodeMap.of());
        assertEquals(ProvFile.of().getMappings(), ImmutableList.of());

        
        
        ProvFile.builder().setMappings(ImmutableList.of(PropertyMapping.of(Ref.of(),
                                                        ImmutableList.of("a"))));
        
        ProvType ps = ProvType.builder().setType(Type.of()).build();
        assertEquals(ImmutableList.of(), ps.getErrors());
        
        NodeMap.of(Ref.of(), NodeMetadata.of(), ImmutableMap.of("a", 
                NodeList.of(NodeValue.of(Ref.of(), NodeMetadata.of(), 3))));
    }

}
