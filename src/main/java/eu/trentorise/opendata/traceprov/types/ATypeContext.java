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
package eu.trentorise.opendata.traceprov.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * A Type with a collection of all the class definitions that can recursively be
 * found referenced in the
 * {@link eu.trentorise.opendata.traceprov.types.IdType}s within the type
 * itself.
 *
 * @author David Leoni
 */
// maybe we could also put type aliases
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = TypeContext.class)
@JsonDeserialize(as = TypeContext.class)
public abstract class ATypeContext implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     * Default is {@link eu.trentorise.opendata.traceprov.types.AnyType#of()}.
     */
    @Value.Default
    public TraceType getType() {
        return AnyType.of();
    }

    public abstract List<Def<ClassType>> getClassDefs();

    @Value.Check
    protected void check() {
        throw new UnsupportedOperationException("todo implement me");
    }
}
