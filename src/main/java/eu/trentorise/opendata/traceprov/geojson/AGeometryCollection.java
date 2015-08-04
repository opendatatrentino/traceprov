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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import java.util.List;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = GeometryCollection.class)
@JsonDeserialize(as = GeometryCollection.class)
abstract class AGeometryCollection extends AGeometry {

    private static final long serialVersionUID = 1L;
    
    /**
     * A Geometry Collection does not have coordinates.
     */
    @Override
    public List getCoordinates(){
        return ImmutableList.of();
    }
    

    public abstract List<AGeometry> getGeometries();
    
}
