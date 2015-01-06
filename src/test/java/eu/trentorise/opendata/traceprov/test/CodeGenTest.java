/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
