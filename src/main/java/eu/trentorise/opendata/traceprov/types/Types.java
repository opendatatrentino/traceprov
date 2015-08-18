package eu.trentorise.opendata.traceprov.types;

import eu.trentorise.opendata.traceprov.TraceProvs;
import static eu.trentorise.opendata.traceprov.TraceProvs.TRACEPROV_PREFIX;
import eu.trentorise.opendata.traceprov.data.NodeArray;
import eu.trentorise.opendata.traceprov.data.NodeMap;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Class to model data types. <br/>
 * <br/>
 * Primitive datatypes are a subset of <a
 * target="_blank" href="http://www.w3.org/TR/2004/REC-rdf-mt-20040210/">
 * RDF Semantics, XSD datatypes section</a>. Complex types such as
 * todo
 * <i>oe:structure</i>
 * and <i>oe:entity</i> are newly defined. <br/>
 * <br/>
 * Datatypes are mapped to Java objects according to the following scheme: <br/>
 * <br/>
 * STRING : {@link java.lang.String} <br/>
 * XSD_BOOLEAN : {@link java.lang.Boolean} <br/>
 * XSD_DATETIME : {@link java.lang.String} <br/>
 * XSD_INT : {@link java.lang.Integer} <br/>
 * XSD_LONG : {@link java.lang.Long} <br/>
 * XSD_FLOAT : {@link java.lang.Float} <br/>
 * XSD_DOUBLE : {@link java.lang.Double} <br/>
 * LIST : {@link NodeArray} <br/>
 * MAP : {@link NodeMap} <br/>
 * <br/>
 *
 * @author David Leoni <david.leoni@unitn.it>
 *
 *
 */
public final class Types {

    public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
    
    public static final String TRACEPROV_TYPES = TraceProvs.TRACEPROV_IRI + "types/";
    
    public static final String XSD_PREFIX = "xsd:";


    private static final Map<String, String> DATATYPE_PRETTY_NAMES_IT = new HashMap();
    private static final Map<String, String> DATATYPE_PRETTY_NAMES_EN = new HashMap();
    private static final Map<Locale, Map<String, String>> DATATYPE_PRETTY_NAMES_MAP = new HashMap();
    private static final Map JAVA_DATATYPES = new HashMap();

    static {
/*
        DATATYPE_PRETTY_NAMES_EN.put(XSD_STRING, "String");
        DATATYPE_PRETTY_NAMES_EN.put(XSD_BOOLEAN, "Boolean");
        DATATYPE_PRETTY_NAMES_EN.put(XSD_DATETIME, "Datetime");
        DATATYPE_PRETTY_NAMES_EN.put(XSD_INT, "Integer");
        DATATYPE_PRETTY_NAMES_EN.put(XSD_FLOAT, "Single precision number");
        DATATYPE_PRETTY_NAMES_EN.put(XSD_DOUBLE, "Double precision number");
        DATATYPE_PRETTY_NAMES_EN.put(XSD_LONG, "Long integer");
        DATATYPE_PRETTY_NAMES_EN.put(LIST, "List");
        DATATYPE_PRETTY_NAMES_EN.put(MAP, "Map");
        DATATYPE_PRETTY_NAMES_MAP.put(Locale.ENGLISH, DATATYPE_PRETTY_NAMES_EN);

        DATATYPE_PRETTY_NAMES_IT.put(XSD_STRING, "Stringa");
        DATATYPE_PRETTY_NAMES_IT.put(XSD_BOOLEAN, "Booleano");
        DATATYPE_PRETTY_NAMES_IT.put(XSD_DATETIME, "Data");
        DATATYPE_PRETTY_NAMES_IT.put(XSD_INT, "Intero");
        DATATYPE_PRETTY_NAMES_IT.put(XSD_FLOAT, "Numero a precisione singola");
        DATATYPE_PRETTY_NAMES_IT.put(XSD_DOUBLE, "Numero a precisione doppia");
        DATATYPE_PRETTY_NAMES_IT.put(XSD_LONG, "Intero grande");
        DATATYPE_PRETTY_NAMES_IT.put(LIST, "Lista");
        DATATYPE_PRETTY_NAMES_IT.put(MAP, "Mappa");
        DATATYPE_PRETTY_NAMES_MAP.put(Locale.ITALIAN, DATATYPE_PRETTY_NAMES_IT);

        JAVA_DATATYPES.put(XSD_STRING, String.class);
        JAVA_DATATYPES.put(XSD_BOOLEAN, Boolean.class);
        JAVA_DATATYPES.put(XSD_DATETIME, Date.class);
        JAVA_DATATYPES.put(XSD_INT, Integer.class);
        JAVA_DATATYPES.put(XSD_FLOAT, Float.class);
        JAVA_DATATYPES.put(XSD_DOUBLE, Double.class);
        JAVA_DATATYPES.put(XSD_LONG, Long.class);
        JAVA_DATATYPES.put(LIST, NodeArray.class);
        JAVA_DATATYPES.put(MAP, NodeMap.class);
*/
    }

    /**
     * Provides a map of the supported types. Each type name is mapped
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

    private Types() {

    }

    
}

