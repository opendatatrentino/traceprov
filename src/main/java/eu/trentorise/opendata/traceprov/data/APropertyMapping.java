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
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.commons.validation.Preconditions;

import eu.trentorise.opendata.commons.validation.Ref;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * A mapping from an element in a source file to a target schema property path.
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = PropertyMapping.class)
@JsonDeserialize(as = PropertyMapping.class)
abstract class APropertyMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A reference to an element in the source file
     */
    @Value.Default
    public Ref getSourceRef() {
        return Ref.of();
    }

    /**
     * A reference to a target schema property id path, like
     * ["workplaces","address","zip"] <br/>
     */
    public abstract List<String> getTargetPath();

    /**
     * The optional confidence for the mapping in the range [0,1]. By default
     * it's 1.0
     */
    @Value.Default
    public double getScore() {
        return 1.0;
    }

    /**
     * Returns new immutable {@code PropertyMapping}, with score 1.0
     *
     * @param sourceRef A reference to an element in the source file
     * @param targetRef A reference to a target schema attribute path
     */
    public static PropertyMapping of(Ref sourceRef, Iterable<String> targetPath) {
        return PropertyMapping.of(sourceRef, targetPath, 1.0);
    }

    @Value.Check
    protected void check() {
        Preconditions.checkScore(getScore(), "Invalid score!");
    }

}
