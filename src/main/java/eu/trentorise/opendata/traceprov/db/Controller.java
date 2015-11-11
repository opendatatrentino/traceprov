package eu.trentorise.opendata.traceprov.db;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A controller for the given type T of objects
 * 
 * @author David Leoni
 * @param <T> the type of the controlled object
 */

public interface Controller<T> {
    /**
     * @return The URI of the controller. For controllers of new objects, the URL might not correspond to a real URL on the server.
     */
    @JsonProperty("@id")
    String getId();
    
    /**
     * Returns the original controlled object 
     */
    T getOriginal();   
                 
}
