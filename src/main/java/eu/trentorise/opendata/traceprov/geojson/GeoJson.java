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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * GeoJSON always consists of a single object. This object (referred to as the
 * GeoJSON object below) represents a geometry, feature, or collection of
 * features.
 *
 * When only geographical name is known use
 * {@link Feature#ofName(java.lang.String) Feature.ofName}
 *
 * When only entity id/url is known use
 * {@link Feature#ofId(java.lang.String) Feature.ofId}
 * 
 * Notice currently it is not possible to store extra fields, 
 * see https://github.com/immutables/immutables/issues/185
 * 
 * @author David Leoni
 * @since 0.3
 */
// todo check this Id.NAME is correct

public abstract class GeoJson implements Serializable {    
        
    @JsonProperty
    @Value.Derived
    public String getType() {
        return this.getClass().getSimpleName();
    }

    public abstract List<Double> getBbox();

    @Nullable
    public abstract Crs getCrs();

    public static ImmutableList<Double> position(double lat, double lon) {
        return ImmutableList.of(lat, lon);
    }

}
