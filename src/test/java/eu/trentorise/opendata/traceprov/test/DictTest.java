package eu.trentorise.opendata.traceprov.test;

import eu.trentorise.opendata.traceprov.Dict;
import eu.trentorise.opendata.traceprov.LocalizedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DictTest {

    @Test
    public void testDict() {
        assertTrue(Dict.of().isEmpty());
        assertTrue(Dict.of("").isEmpty());
        assertFalse(Dict.of("hello").isEmpty());

        Dict dict = Dict.builder()
                .putAll(Locale.FRENCH, "A")
                .putAll(Locale.ITALIAN, "b")
                .putAll(Locale.ENGLISH, "c")
                .build();

        assertTrue(dict.contains("a"));
        assertTrue(dict.contains("B"));

        assertEquals(LocalizedString.of(Locale.ENGLISH, "c"),
                     dict.prettyString(Locale.CHINESE));
        
        assertEquals(LocalizedString.of(), Dict.of().prettyString(Locale.ITALIAN));

        assertEquals(Dict.of("b", "b"),
                     Dict.builder().putAll(Dict.of("b")).putAll(Dict.of("b")).build());

        
                
        assertEquals(Dict.builder().putAll(Dict.of(Locale.GERMAN, "b")).putAll(Dict.of(Locale.ITALIAN, "a")).build(),
                     Dict.builder().putAll(Dict.of(Locale.ITALIAN, "a")).putAll(Dict.of(Locale.GERMAN, "b")).build()
        );
        
        
        
    }
    
    @Test
    public void testNonEmpty(){
        Dict dict = Dict.builder().putAll(Locale.FRENCH, "", "a").build();
        assertEquals("a", dict.nonEmptyString(Locale.FRENCH));        
    }
    
    
}
