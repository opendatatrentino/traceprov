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

import static com.google.common.base.Preconditions.checkNotNull;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * A node of the common tree format representation. It also holds a provenance
 * reference. Use {@link DataVisitor} and
 * {@link #accept(eu.trentorise.opendata.traceprov.data.IDataVisitor, eu.trentorise.opendata.traceprov.data.Data, java.lang.String, int) accept}
 * to navigate it.
 *
 * @author David Leoni
 */
public abstract class DataNode implements Serializable {

    private Ref ref;
    private long id;
    private NodeMetadata metadata;
    private Object data;

    DataNode() {
        this.ref = Ref.of();
        this.metadata = NodeMetadata.of();
        this.data = null;
    }

    DataNode(Ref ref, NodeMetadata metadata, @Nullable Object data) {
        checkNotNull(ref);
        checkNotNull(metadata);
        this.ref = ref;
        this.metadata = metadata;
        this.data = data;
    }

    /**
     * A reference to position in the original file from which this node comes
     * from. If unknown, {@link Ref#of()} is returned.
     */
    public Ref getRef() {
        return ref;
    }

    /**
     * Provenance information about the node.
     */
    public NodeMetadata getMetadata() {
        return metadata;
    }

    /**
     * The subnodes of the node or the terminal value.
     */
    public Object getValue() {
        return data;
    }

    /**
     * Allows traversing the data tree with a {@link DataVisitor}.
     * 
     * <p> Calls {@code accept} on subnodes and then {@code visitor.visit} on this
     * node. </p>
     *
     * @param visitor a node visitor.
     * @param parent the parent of the node. If unknown, use
     * {@link DataValue#of()}
     * @param field The immediate field of a map under which current node is
     * stored. If unknown, use "".
     * @param pos The position of the node in the parent array. If it is not in
     * an array, use 0.
     */
    public abstract void accept(DataVisitor visitor, DataNode parent, String field, int pos);

    /**
     * Converts the node to a simple Map/List tree suitable for JSON
     * serialization without the metadata information such as provenance.
     *
     * @return a tree made of the following objects:
     * HashMap/ArrayList/String/Number/null
     */
    // todo asSimpleJavaType maybe is more descriptive
    public abstract Object asSimpleType(TypeRegistry typeRegistry);

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.ref);
        hash = 17 * hash + Objects.hashCode(this.metadata);
        hash = 17 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataNode other = (DataNode) obj;
        if (!Objects.equals(this.ref, other.ref)) {
            return false;
        }
        if (!Objects.equals(this.metadata, other.metadata)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "ref=" + getRef() + ", metadata=" + getMetadata() + ", data=" + getValue() + '}';
    }

    

}
