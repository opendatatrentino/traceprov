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
                .addStrings(Locale.FRENCH, "A")
                .addStrings(Locale.ITALIAN, "b")
                .addStrings(Locale.ENGLISH, "c")
                .build();

        assertTrue(dict.contains("a"));
        assertTrue(dict.contains("B"));

        assertEquals(LocalizedString.of(Locale.ENGLISH, "c"),
                     dict.prettyString(Locale.CHINESE));
        
        assertEquals(LocalizedString.of(), Dict.of().prettyString(Locale.ITALIAN));

        assertEquals(Dict.of("b").merge(Dict.of("b")), 
                     Dict.of("b"));           

        assertEquals(Dict.of(Locale.ITALIAN, "a").merge(Dict.of(Locale.GERMAN, "b")),
                Dict.of(Locale.GERMAN, "b").merge(Dict.of(Locale.ITALIAN, "a"))
        );
        
        
        
    }
    
    @Test
    public void testNonEmpty(){
        Dict dict = Dict.builder().addString("").addStrings(Locale.FRENCH, "a").build();
        assertEquals("a", dict.nonEmptyString(Locale.FRENCH));        
    }
    
    
}
