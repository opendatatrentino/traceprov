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
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * TODO fill this class When only name is known use
 * {@link Feature#ofName(java.lang.String)}
 *
 * @author David Leoni
 */
// todo check this Id.NAME is correct
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public abstract class GeoJson {

    @Value.Derived
    public String getType() {
        return this.getClass().getSimpleName();
    }

    public abstract List<Double> getBbox();
    
    @Nullable
    public abstract ACrs getCrs();
    
    /**
     * Although this is a hash map, DO *NOT* MODIFY IT. USE IT *ONLY* AS MAP
     * INTERFACE.
     */
    @Value.Default
    @JsonAnyGetter
    public HashMap<String, Object> getOthers() {
        return new HashMap();
    }

    /**
     * See {@link #getOthers()}
     *
     */
    @JsonAnySetter
    protected void putOthers(String name, Object value) {
        getOthers().put(name, value);
    }

    public static ImmutableList<Double> position(double lat, double lon) {
        return ImmutableList.of(lat, lon);
    }


}
