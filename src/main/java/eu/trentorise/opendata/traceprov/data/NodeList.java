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
import java.util.Objects;

/**
 * {@link ProvFile} body node containing an array of other nodes.
 *
 * @author David Leoni
 */
public class NodeList implements INode, Iterable<INode> {

    private static final long serialVersionUID = 1L;
    private static final NodeList INSTANCE = new NodeList();

    private Iterable<INode> nodes;
    private ARef provenance;

    private NodeList() {
        this.nodes = ImmutableList.<INode>of();
        this.provenance = DocRef.of();
    }

    private NodeList(ARef provenance, Iterable<? extends INode> nodes) {
        checkNotNull(provenance);
        checkNotNull(nodes);
        this.provenance = provenance;
        this.nodes = (Iterable<INode>) nodes;
    }

    public ARef getProvenance() {
        return provenance;
    }

    public Iterator<INode> iterator() {
        return nodes.iterator();
    }

    public static NodeList of() {
        return INSTANCE;
    }

    public static NodeList of(Iterable<? extends INode> nodes) {
        return of(DocRef.of(), nodes);
    }

    public static NodeList of(INode... nodes) {
        return new NodeList(DocRef.of(), ImmutableList.copyOf(nodes));
    }

    public static NodeList of(ARef provenance, Iterable<? extends INode> nodes) {
        return new NodeList(provenance, nodes);
    }

    public static NodeList of(ARef provenance, INode... nodes) {
        return new NodeList(provenance, ImmutableList.copyOf(nodes));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.nodes);
        hash = 53 * hash + Objects.hashCode(this.provenance);
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
        final NodeList other = (NodeList) obj;
        if (!Objects.equals(this.nodes, other.nodes)) {
            return false;
        }
        if (!Objects.equals(this.provenance, other.provenance)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NodeList{" + "nodes=" + nodes + ", provenance=" + provenance + '}';
    }

   

    @Override
    public void accept(INodeVisitor visitor, INode parent, String field, int pos) {
        int i = 0;
        Iterator<INode> iter = nodes.iterator();
        
        while (iter.hasNext()){
            INode node = iter.next();
            node.accept(visitor, this, field, i);
            i++;
        }
        
        visitor.visit((NodeList) this, parent, field, pos);

    }

    @Override
    public Object asSimpleType() {
        SimpleMapTransformer tran = new SimpleMapTransformer();        
        accept(tran, NodeValue.of(), "",0);        
        return tran.getResult();
    }
       
    
}
