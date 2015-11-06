package eu.trentorise.opendata.traceprov.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

/**
 * Implementations can be stored in
 * {@link eu.trentorise.opendata.opendatarise.db.TraceDb} and must be serializable
 * with Jackson
 *
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface Storable extends Serializable {

    @JsonProperty("@id")
    long getId();

};
