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

import eu.trentorise.opendata.commons.validation.IRef;
import eu.trentorise.opendata.commons.validation.Ref;
import java.io.Serializable;

/**
 * A node of the common tree format representation. It also holds a provenance
 * reference.
 *
 * @author David Leoni
 */
public interface INode extends Serializable {

    /**
     * A reference to position in the original file from which this node comes
     * from. If unknown, {@link Ref#of()} is returned.
     */
    IRef getProvenance();
    
    
    /**
     * 
     * @param visitor
     * @param parent the parent of the node. If unknown, use {@link NodeValue#of()}
     * @param field The immediate field of a map under which current node is stored. If unknown, use "".
     * @param pos The position of the node in the parent array. If it is not in an array, use 0.
     */
    void accept(INodeVisitor visitor, INode parent, String field, int pos);
    
    /**
     * Converts the node to a simple Map/List tree suitable for JSON
     * serialization without the metadata information such as provenance.
     * @return a tree made of the following objects: HashMap/ArrayList/String/Number/null      
     */
    Object asSimpleType();
}







