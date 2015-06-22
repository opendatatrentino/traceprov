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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.schema.Mapping;
import eu.trentorise.opendata.traceprov.schema.Schema;
import java.io.Serializable;
import java.util.List;
import org.immutables.value.Value;

/**
 * Tree-like generic data model to represent a file and tracking from where data
 * comes from. Also holds validation errors.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = ProvFile.class)
@JsonDeserialize(as = ProvFile.class)
abstract class AProvFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The Dcat metadata associated to the original file. If no metadata was
     * found, {@link DcatMetadata#of()} is returned.
     */
    @Value.Default
    public DcatMetadata getDcatMetadata() {
        return DcatMetadata.of();
    }

    /**
     * The schema of the original file. If no schema was found,
     * {@link Schema#of()} is returned.
     */
    @Value.Default
    public Schema getDataSchema() {
        return Schema.of();
    }

    /**
     * The high-level mappings from source file elements (columns, schema node
     * paths, ...) to the target schema property paths.
     */
    public abstract ImmutableList<Mapping> getSchemaMappings();

    /**
     * The target schema identifier. If no identifier was associated, an empty
     * string is returned.
     */
    @Value.Default
    public String getTargetSchemaId() {
        return "";
    }

    /**
     * Returns the data content of the file as a hierarchical tree. If no data
     * was found, {@link NodeMap#of()} is returned.
     */
    @Value.Default
    public ANode getData() {
        return NodeMap.of();
    }

    /**
     * Returns the validation errors found in the original file.
     */
    public abstract List<Error> getErrors();

}
