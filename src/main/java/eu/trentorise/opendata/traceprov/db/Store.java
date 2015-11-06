package eu.trentorise.opendata.traceprov.db;

import java.util.Map;

/**
 *
 * Superceeds {@link eu.trentorise.opendata.opendatarise.ekb.Store}
 *
 *
 * @author David Leoni
 *
 */
class Store<C extends Controller> {

    /**
     * Maps expanded urls [originUrl, url] (i.e. "http://entitypedia.org",
     * "http://entitypedia.org/entities/14323") pairs to corresponding
     * controllers
     */
    private Map<String, Map<String, C>> storedValues;

    /**
     * Maps odr ids to controllers
     */
    private Map<Long, C> storedIds;

    private Class<C> controllerClass;

    private Store() {
        this.controllerClass = null;
    }

    private Store(Class<C> c) {
        this();
        this.controllerClass = c;
    }

}
