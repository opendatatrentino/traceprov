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
import static com.google.common.base.Preconditions.checkNotNull;

import eu.trentorise.opendata.commons.validation.Ref;
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
    private static final int MAX_PRINTED_NODES = 10;

    private Iterable<INode> nodes;
    private Ref provenance;

    private NodeList() {
        this.nodes = ImmutableList.<INode>of();
        this.provenance = Ref.of();
    }

    private NodeList(Ref provenance, Iterable<? extends INode> nodes) {
        checkNotNull(provenance);
        checkNotNull(nodes);
        this.provenance = provenance;
        this.nodes = (Iterable<INode>) nodes;
    }

    @Override
    public Ref getProvenance() {
        return provenance;
    }

    @Override
    public Iterator<INode> iterator() {
        return nodes.iterator();
    }

    public static NodeList of() {
        return INSTANCE;
    }

    public static NodeList of(Iterable<? extends INode> nodes) {
        return of(Ref.of(), nodes);
    }

    public static NodeList of(INode... nodes) {
        return new NodeList(Ref.of(), ImmutableList.copyOf(nodes));
    }

    public static NodeList of(Ref provenance, Iterable<? extends INode> nodes) {
        return new NodeList(provenance, nodes);
    }

    public static NodeList of(Ref provenance, INode... nodes) {
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
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (INode n : nodes){
            if (i > 0){
                sb.append(", ");
            }            
            if (i < MAX_PRINTED_NODES){
                sb.append(n.toString());
            } else {
                sb.append("...");
                break;                
            }
            i++;
        }
        sb.append("]");
        return "NodeList{" + "nodes=" + sb.toString() + ", provenance=" + provenance + '}';
    }

    @Override
    public void accept(INodeVisitor visitor, INode parent, String field, int pos) {
        int i = 0;
        Iterator<INode> iter = nodes.iterator();

        while (iter.hasNext()) {
            INode node = iter.next();
            node.accept(visitor, this, field, i);
            i++;
        }

        visitor.visit((NodeList) this, parent, field, pos);

    }

    @Override
    public Object asSimpleType() {
        SimpleMapTransformer tran = new SimpleMapTransformer();
        accept(tran, NodeValue.of(), "", 0);
        return tran.getResult();
    }

}
