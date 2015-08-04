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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableMap;
import eu.trentorise.opendata.commons.SimpleStyle;
import java.io.Serializable;
import java.util.Map;
import org.immutables.value.Value;

/**
 * The coordinate reference system (CRS) of a GeoJSON object 
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = Crs.class)
@JsonDeserialize(as = Crs.class)
abstract class ACrs implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Value.Default
    public String getType() {
        return "name";
    }

    @Value.Default
    public Map<String, ?> getProperties() {
        return ImmutableMap.of("name", "urn:ogc:def:crs:OGC:1.3:CRS84");
    }

    public static Crs ofName(String name) {
        return Crs.of("name", ImmutableMap.of("name", name));
    }

    public static Crs ofLink(String href) {
        return Crs.of("link", ImmutableMap.of("href", href));
    }

      public static Crs ofLink(String href, String type) {
        return Crs.of("link", ImmutableMap.of("href", href, "type", type));
    }
  
    
    @Value.Check
    protected void check() {
        switch (getType()) {
            case "name":
                checkArgument(getProperties().containsKey("name"), "properties[\"name\"] must be present for 'name' type!");
                checkNotNull(getProperties().get("name"), "properties[\"name\"] must not be null!");
                break;
            case "link":
                checkNotNull(getProperties().get("href"), "properties[\"href\"] must not be null for 'link' type!");
                if (getProperties().containsKey("type")) {
                    checkNotNull(getProperties().get("type"), "properties[\"type\"] must not be null for 'link' type!");
                }
                break;
            case "default":
                throw new IllegalStateException("Unrecognized type " + getType() + "!");
        }

    }
}
