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
package eu.trentorise.opendata.traceprov.schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.OdtUtils;
import eu.trentorise.opendata.commons.SimpleStyle;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * A mapping from an element in a source file to a target schema attribute path.
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = RefMapping.class)
@JsonDeserialize(as = RefMapping.class)
abstract class ARefMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A reference to an element in the source file
     */
    @Value.Default
    public ARef getSourceRef() {
        return DocRef.of();
    }

    /**
     * A reference to a target schema attribute path
     */
    @Value.Default
    public SchemaRef getTargetRef() {
        return SchemaRef.of();
    }

    /**
     * The optional confidence for the mapping in the range [0,1]. By default
     * it's 1.0
     */
    @Value.Default
    public double getScore() {
        return 1.0;
    }

    @Value.Check
    protected void check() {
        OdtUtils.checkScore(getScore(), "Invalid score!");
    }

  /**
   * Returns new immutable {@code RefMapping}, with score 1.0
   * 
   * @param sourceRef A reference to an element in the source file
   * @param targetRef A reference to a target schema attribute path   
   */
  public static RefMapping of(ARef sourceRef, SchemaRef targetRef) {
        return RefMapping.of(sourceRef, targetRef, 1.0);
  }    
    
}
