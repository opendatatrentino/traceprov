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

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.traceprov.schema.DocRef;
import eu.trentorise.opendata.traceprov.schema.ARef;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;

/**
 * {@link ProvFile} body node containing an array of other nodes.
 *
 * @author David Leoni
 */
public class NodeArray implements INode, Iterable<INode> {

    private static final long serialVersionUID = 1L;
    private static final NodeArray INSTANCE = new NodeArray();

    private Iterable values;
    private ARef provenance;

    public ARef getProvenance() {
        return DocRef.of();
    }

    public Iterator<INode> iterator() {
        return values.iterator();
    }

    private NodeArray() {
        this.values = ImmutableList.of();
        this.provenance = DocRef.of();
    }

    private NodeArray(ARef provenance, Iterable<INode> nodes) {
        checkNotNull(provenance);
        checkNotNull(nodes);        
        this.provenance = provenance;
        this.values = nodes;
    }

    public static NodeArray of() {
        return INSTANCE;
    }
    
    public static NodeArray of(Iterable<INode> iterable) {
        return of(DocRef.of(), iterable);
    }
    
    public static NodeArray of(INode... nodes) {
        return new NodeArray(DocRef.of(), ImmutableList.copyOf(nodes));
    }

    public static NodeArray of(ARef provenance, Iterable<INode> iterable) {
        return new NodeArray(provenance, iterable);
    }
    
    public static NodeArray of(ARef provenance, INode... nodes) {
        return new NodeArray(provenance, ImmutableList.copyOf(nodes));
    }
}
