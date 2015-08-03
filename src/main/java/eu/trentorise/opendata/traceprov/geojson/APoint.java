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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.BuilderStyle;
import java.util.List;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable(singleton = false)
@BuilderStyle
@JsonSerialize(as = Point.class)
@JsonDeserialize(as = Point.class)
public abstract class APoint extends AGeometry {

    
    @Override
    public abstract List<Double> getCoordinates();
        
    
    @Value.Check
    protected void check(){        
        if (getCoordinates().size() != 2){
            throw new IllegalStateException("A Point needs exactly 2 coordinates, found instead " + getCoordinates().size());
        }
    }
    
    /**
     * Creates a point at given coordinates
     */
    public static Point of(double lat, double lon){
        return Point.builder().addCoordinates(lat,lon).build();
    }    
    
    
    /**
     * Creates a point at 0.0, 0.0     
     */
    public static Point of(){
        return Point.builder().addCoordinates(0.0,0.0).build();
    }
}
