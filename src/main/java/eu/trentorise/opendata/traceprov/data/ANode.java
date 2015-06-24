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

import eu.trentorise.opendata.traceprov.schema.DocRef;
import eu.trentorise.opendata.traceprov.schema.ARef;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * A node of the tree representation of the
 * {@link eu.trentorise.opendata.traceprov.data.ProvFile} body
 *
 * @author David Leoni
 */
public abstract class ANode implements Serializable {

    ANode() {
    }

    /**
     * A reference to position in the original file from which this node comes
     * from. If unknown, {@link DocRef#of()} is returned.
     */
    @Value.Default
    @Value.Parameter
    public ARef getProvenance() {
        return DocRef.of();
    }

}
