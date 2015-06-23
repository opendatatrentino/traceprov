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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * A Schema of a tree-like file, loosely modeled after what you can express with
 * a jsonld context.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = Schema.class)
@JsonDeserialize(as = Schema.class)
abstract class ASchema implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The id of the type, which may be an IRI to a well-known type like i.e.
     * https://schema.org/Person
     */
    @Value.Default
    @Value.Parameter
    public String getId() {
        return "";
    }

    /**
     * Name of the type preferably in English and camelcased, i.e. CreativeWork,
     * BroadcastService
     */
    @Value.Default
    @Value.Parameter
    public String getName() {
        return "";
    }

    /**
     * The property definitions of the schema
     */
    @Value.Parameter
    public abstract ImmutableList<PropertyDef> getPropertyDefs();

}
