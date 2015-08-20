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
package eu.trentorise.opendata.traceprov.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import java.util.Map;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * 
 * @author David Leoni 
 * @since 0.3
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = Feature.class)
@JsonDeserialize(as = Feature.class)
abstract class AFeature extends GeoJson {            
    
    private static final long serialVersionUID = 1L;
    
    /**
     * The id of the feature, if present. The empty string otherwise.     
     */
    @JsonProperty("@id")
    @Value.Default
    public String getId(){
        return "";
    }
    

    @Nullable
    @Value.Default
    public Object getProperties() {
        return ImmutableMap.of();
    }
    
    /**
     * Returns the name of the feature if present, the empty string otherwise.     
     */
    public String name(){
        if (getProperties() != null && getProperties() instanceof Map){
            Object nob =  ((Map) getProperties()).get("name");
            if (nob instanceof String){
                return (String) nob;
            }                         
        }
        return "";
    }
    
    @Nullable
    public abstract AGeometry getGeometry();

    /**
     * <pre>
     * {
     *  "type": "Feature",
     *  "geometry":null,
     *  "properties":{"name":""}
     * }
     * </pre>
     *     
     */
    public static Feature ofName(String string) {        
        return Feature.builder().setProperties(ImmutableMap.of("name", string)).build();
    }
       
    
   /**
     * <pre>
     * {
     *  "type": "Feature",
     *  "geometry":null,
     *  "properties":{"name":""}
     * }
     * </pre>
     *     
     */
    public static Feature ofId(String id) {
        return Feature.builder().setId(id).build();        
    }    
    
    /**
     * Returns 
     * <pre>
     * {
     *  "type": "Feature",
     *  "geometry":null,
     *  "properties":{"name":""}
     * }
     * </pre>
     *     
     */
    public static Feature of() {
        return ofName("");        
    }    

    
}
