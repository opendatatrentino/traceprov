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

import eu.trentorise.opendata.traceprov.Dict;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;
import eu.trentorise.opendata.traceprov.ref.CellRef;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David Leoni
 */
public class CodeGenTest {

    @Test
    public void testDcatCodeGen() {
        DcatDataset dataset = DcatDataset.of();

        assertEquals("", dataset.getUri());

        assertEquals("a", DcatDataset.builder().setUri("a").build().getUri());

        assertEquals("b", dataset.withUri("b").getUri());
        
        DcatDataset.builder().addAllDistributions(DcatDataset.of().getDistributions());                
    }

    public void dcataDatasetUsageExample() {
        DcatDataset dataset = DcatDataset
                .builder()                
                .setTitle(Dict.of(Locale.ITALIAN, "Impianti di risalita, ViviFiemme 2013"))
                .setLandingPage("http://dati.trentino.it/dataset/impianti-di-risalita-vivifiemme-2013")
                .build();
    }

    @Test
    public void testRefCodeGen() {

        assertEquals(CellRef.of(), CellRef.of(0, 0));
        assertEquals(1, CellRef.of(1, 0).getRowIndex());        

    }

}
