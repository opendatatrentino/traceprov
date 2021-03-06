/*
 * Copyright 2015 Trento Rise.
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
package eu.trentorise.opendata.traceprov.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.PeriodOfTime;
import eu.trentorise.opendata.traceprov.dcat.AFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.geojson.Feature;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.TraceType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;

import javax.annotation.Nullable;

import org.immutables.value.Value;

/**
 * A dcat inspired metadata that can be reasonably shared by many nodes in a
 * tree-like dataset.
 * 
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = NodeMetadata.class)
@JsonDeserialize(as = NodeMetadata.class)
abstract class ANodeMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The language of the node. If none is specified {
     *
     * @Locale#ROOT} is returned
     *
     *               Java Locale should be created out of Language codes defined
     *               by the Library of Congress (
     *               <a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO
     *               639-1</a>,
     *               <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO
     *               639-2</a>)... If a ISO 639-1 (two-letter) code is defined
     *               for language, then its corresponding IRI should be used; if
     *               no ISO 639-1 code is defined, then ISO 639-2 (three-letter)
     *               code should be used.
     */
    @Value.Default
    public Locale getLanguage() {
	return Locale.ROOT;
    }

    /**
     *
     * /** The temporal period that the value stored in the node covers, that
     * is, an interval of time that is named or defined by its start and end
     * dates as defined by
     * <a href="http://purl.org/dc/terms/temporal">dct:temporal</a>
     *
     * </ul>
     * 
     * By default is {@link PeriodOfTime#of()}
     */
    @Value.Default
    public PeriodOfTime getTemporal() {
	return PeriodOfTime.of();
    }

    /**
     * Spatial coverage of the value, as specified by
     * <a href="http://purl.org/dc/terms/spatial">dct:spatial</a> i.e.
     * dct:spatial <http://www.geonames.org/6695072> ;
     *
     * The returned object may be either:
     * <ul>
     * <li>A {@link eu.trentorise.opendata.traceprov.geojson.GeoJson GeoJSON}
     * object</li>
     * <li>A natural language name inside
     * {@link eu.trentorise.opendata.traceprov.geojson.Feature#ofName(java.lang.String)
     * Feature object}</li>
     * <li>a url to an identifier of the location, i.e.
     * http://www.geonames.org/6695072 (in this case you can use
     * {@link eu.trentorise.opendata.traceprov.geojson.Feature#ofId(java.lang.String)
     * Feature.ofId}</li>
     * <li>if spatial value is unknwon
     * {@link eu.trentorise.opendata.traceprov.geojson.Feature#of() is returned.
     * </li>
     * </ul>
     *
     */
    @Value.Default
    public GeoJson getSpatial() {
	return Feature.of();
    }

    /**
     * The type of the value stored in the node, expressed as {@link TraceType
     * TraceProv Type}. Defaults to {@link AnyType}
     */
    @Value.Default
    public TraceType getType() {
	return AnyType.of();
    }

    /**
     * An entity responsible for making the data available. Specified by
     * <a href="http://purl.org/dc/terms/publisher" target="_bank">dct:publisher
     * </a>
     *
     * The field is expressed as the traceprov internal id of the publisher.
     * Default value is -1. 
     *
     * @see AFoafAgent
     * @see AFoafPerson
     * @see AFoafOrganization
     */
    @Value.Default
    public long getPublisherId() {
	return -1;
    }

    /**
     * A link to the license document under which the value is made available.
     * The license should be a legal document giving official permission to do
     * something with the resource, as specified in <a href=
     * "http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#license">
     * dct:license</a>
     *
     */
    @Value.Default
    public String getLicense() {
	return "";
    }

    /**
     * The timestamp of the instant when the node was stored in traceprov
     * system. Note this timestamp is generated by traceprov and is different
     * from {@link #getTemporal()}.
     */
    @Nullable
    public abstract Timestamp getTimestamp();

}
