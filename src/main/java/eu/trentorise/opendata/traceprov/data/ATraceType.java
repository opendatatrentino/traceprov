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
import eu.trentorise.opendata.commons.validation.ValidationError;
import eu.trentorise.opendata.traceprov.types.Type;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.ClassType;
import eu.trentorise.opendata.traceprov.types.Def;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * Type and validation errors.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = TraceType.class)
@JsonDeserialize(as = TraceType.class)
abstract class ATraceType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The schema definition of the original file. If no definition was found,
     * {@link AnyType#of()} is returned.
     */
    @Value.Default
    public Type getType() {
        return AnyType.of();
    }

    /**
     * Returns the class definitions of all class references present in the
     * result of {#getType()}
     */
    public abstract List<Def<ClassType>> getClassDefs();

    /**
     * Returns the validation errors found in the original schema file.
     */
    public abstract List<ValidationError> getErrors();

}
