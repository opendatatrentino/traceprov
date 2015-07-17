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
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.AType;
import java.util.Locale;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = NodeMetadata.class)
@JsonDeserialize(as = NodeMetadata.class)
abstract class ANodeMetadata {   

    /**
     * The language of the node. If none is specified {@Locale#ROOT} is returned
     *
     * Java Locale should be created out of Language codes defined by the
     * Library of Congress
     * (<a href="http://id.loc.gov/vocabulary/iso639-1.html">ISO 639-1</a>,
     * <a href="http://id.loc.gov/vocabulary/iso639-2.html">ISO 639-2</a>)... If
     * a ISO 639-1 (two-letter) code is defined for language, then its
     * corresponding IRI should be used; if no ISO 639-1 code is defined, then
     * ISO 639-2 (three-letter) code should be used.
     */
    @Value.Default
    public Locale getLanguage(){
        return Locale.ROOT;
    };

    /**
     * The temporal period that the dataset covers, that is, an interval of time
     * that is named or defined by its start and end dates as defined by
     * <a href="http://purl.org/dc/terms/temporal">dct:temporal</a>
     *
     * i.e. dct:temporal
     * <http://reference.data.gov.uk/id/quarter/2006-Q1> ;
     * 
     * Allowed values:
     * <ul>
     * <li>A string formatted following
     * <a href="https://en.wikipedia.org/wiki/ISO_8601#Time_intervals">ISO 8601
     * Date and Time interval</a> string format i.e.
     * "2007-03-01T13:00:00Z/2008-05-11T15:30:00Z". </li>
     * <li>A String in natural language, i.e. Summer of 2014</li>
     * <li>An empty string if the interval is unknown</li>
     * </ul>
     */
    @Value.Default
    public String getTemporal(){
        return "";
    };

    /**
     * Spatial coverage of the dataset.
     *
     * The returned object may be either:
     * <ul>
     * <li> a natural language name of the location </li>
     * <li> a url to an identifier of the location, i.e.
     * http://www.geonames.org/6695072 </li>     
     * <li> a GeoJSON object
     * {@link eu.trentorise.opendata.traceprov.geojson.GeoJson} </li>     
     * <li> if spatial value is unknwon the empty string is returned. </li>
     * </ul>
     *
     */
    public Object getSpatial(){
        return "";
    }

    
    @Value.Default
    public AType getType(){
        return AnyType.of();
    }
    
}
