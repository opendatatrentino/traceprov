package eu.trentorise.opendata.traceprov.schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import static eu.trentorise.opendata.traceprov.TraceProvs.TRACEPROV_PREFIX;
import eu.trentorise.opendata.traceprov.data.NodeList;
import eu.trentorise.opendata.traceprov.data.NodeMap;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.immutables.value.Value;

/**
 * Class to model data types. <br/>
 * <br/>
 * Primitive datatypes are a subset of <a
 * target="_blank" href="http://www.w3.org/TR/2004/REC-rdf-mt-20040210/">
 * RDF Semantics, XSD datatypes section</a>. Complex types such as
 * <i>oe:structure</i>
 * and <i>oe:entity</i> are newly defined. <br/>
 * <br/>
 * Datatypes are mapped to Java objects according to the following scheme: <br/>
 * <br/>
 * STRING : {@link java.lang.String} <br/>
 * BOOLEAN : {@link java.lang.Boolean} <br/>
 * DATE : {@link java.util.Date} <br/>
 * INTEGER : {@link java.lang.Integer} <br/>
 * LONG : {@link java.lang.Long} <br/>
 * FLOAT : {@link java.lang.Float} <br/>
 * DOUBLE : {@link java.lang.Double} <br/>
 * ARRAY : {@link NodeList} <br/>
 * MAP : {@link NodeMap} <br/>
 * <br/>
 *
 * @author David Leoni <david.leoni@unitn.it>
 *
 *
 */
public final class Datatypes {

    public static final String XSD_PREFIX = "xsd:";

    public static final String STRING = XSD_PREFIX + "string";
    public static final String BOOLEAN = XSD_PREFIX + "boolean";
    public static final String DATE = XSD_PREFIX + "dateTime";
    public static final String INTEGER = XSD_PREFIX + "int";
    public static final String FLOAT = XSD_PREFIX + "float";
    public static final String LONG = XSD_PREFIX + "long";
    public static final String LIST = TRACEPROV_PREFIX + "list";
    public static final String MAP = TRACEPROV_PREFIX + "map";

    private static final Map<String, String> DATATYPE_PRETTY_NAMES_IT = new HashMap<String, String>();
    private static final Map<String, String> DATATYPE_PRETTY_NAMES_EN = new HashMap<String, String>();
    private static final Map<Locale, Map<String, String>> DATATYPE_PRETTY_NAMES_MAP = new HashMap<Locale, Map<String, String>>();
    private static final Map JAVA_DATATYPES = new HashMap<String, Class>();

    static {

        DATATYPE_PRETTY_NAMES_EN.put(STRING, "String");
        DATATYPE_PRETTY_NAMES_EN.put(BOOLEAN, "Boolean");
        DATATYPE_PRETTY_NAMES_EN.put(DATE, "Date");
        DATATYPE_PRETTY_NAMES_EN.put(INTEGER, "Integer");
        DATATYPE_PRETTY_NAMES_EN.put(FLOAT, "Single precision number");
        DATATYPE_PRETTY_NAMES_EN.put(LONG, "Long integer");
        DATATYPE_PRETTY_NAMES_EN.put(LIST, "List");
        DATATYPE_PRETTY_NAMES_EN.put(MAP, "Map");
        DATATYPE_PRETTY_NAMES_MAP.put(Locale.ENGLISH, DATATYPE_PRETTY_NAMES_EN);

        DATATYPE_PRETTY_NAMES_IT.put(STRING, "Stringa");
        DATATYPE_PRETTY_NAMES_IT.put(BOOLEAN, "Booleano");
        DATATYPE_PRETTY_NAMES_IT.put(DATE, "Data");
        DATATYPE_PRETTY_NAMES_IT.put(INTEGER, "Intero");
        DATATYPE_PRETTY_NAMES_IT.put(FLOAT, "Numero a precisione singola");
        DATATYPE_PRETTY_NAMES_IT.put(LONG, "Intero grande");
        DATATYPE_PRETTY_NAMES_IT.put(LIST, "Lista");
        DATATYPE_PRETTY_NAMES_IT.put(MAP, "Mappa");
        DATATYPE_PRETTY_NAMES_MAP.put(Locale.ITALIAN, DATATYPE_PRETTY_NAMES_IT);

        JAVA_DATATYPES.put(STRING, String.class);
        JAVA_DATATYPES.put(BOOLEAN, Boolean.class);
        JAVA_DATATYPES.put(DATE, Date.class);
        JAVA_DATATYPES.put(INTEGER, Integer.class);
        JAVA_DATATYPES.put(FLOAT, Float.class);
        JAVA_DATATYPES.put(LONG, Long.class);
        JAVA_DATATYPES.put(LIST, NodeList.class);
        JAVA_DATATYPES.put(MAP, NodeMap.class);

    }

    /**
     * Provides a map of the supported datatypes. Each datatype name is mapped
     * to the java class or interface that represents it.
     *
     * @return an unmodifiable map of the supported data types
     */
    public static Map<String, Class> getDataTypes() {
        return Collections.unmodifiableMap(JAVA_DATATYPES);
    }

    /**
     * Returns human-readable name of a datatype in the provided locale
     *
     * @param datatype the given datatype
     * @param locale the language of the desired translation
     * @return the datatype in a human-readable form in the provided locale if
     * the translation is present and the datatype is supported, returns null
     * otherwise.
     */
    public static String prettyDataType(String datatype, Locale locale) {

        Map map = DATATYPE_PRETTY_NAMES_MAP.get(locale);
        if (map == null) {
            return null;
        } else {
            return DATATYPE_PRETTY_NAMES_MAP.get(locale).get(datatype);
        }

    }

    /**
     *
     * @return a set of the supported locales
     */
    public static Set<Locale> supportedLocales() {
        return Collections.unmodifiableSet(DATATYPE_PRETTY_NAMES_MAP.keySet());
    }

    private Datatypes() {

    }

    
    
}

@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = TypeSig.class)
@JsonDeserialize(as = TypeSig.class)

class DatatypeContext {
    void registerType(Type schema){
        
    }
}