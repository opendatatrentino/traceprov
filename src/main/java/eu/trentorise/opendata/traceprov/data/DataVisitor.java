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

/**
 * Visitor for data nodes.
 *
 * @author David Leoni
 */
public interface DataVisitor {

    /**
     * Visits a {@link DataMap}
     * 
     * @param node the node to visite
     * @param parent the immediate parent of the node which is being visited
     * @param field if parent is a {@link DataMap}, the name of the field under
     * which the node to be visited lies. Otherwise it is unspecified.
     * @param pos If the parent node is a {@link DataArray}, pos is the position
     * in such list. Otherwise it is zero.
     */
    void visit(DataMap node, DataNode parent, String field, int pos);

    /**
     * Visits a {@link DataList}
     * 
     * @param node the node to visit
     * @param parent the immediate parent of the node which is being visited
     * @param field if parent is a {@link DataMap}, the name of the field under
     * which the node to be visited lies. Otherwise it is unspecified.
     * @param pos If the parent node is a {@link DataArray}, pos is the position
     * in such list. Otherwise it is unspecified.
     */    
    void visit(DataArray node, DataNode parent, String field, int pos);

    /**
     * Visits a {@link DataValue}
     * 
     * @param node the node to visit
     * @param parent the immediate parent of the node which is being visited
     * @param field if parent is a {@link DataMap}, the name of the field under
     * which the node to be visited lies. Otherwise it is unspecified.
     * @param pos If the parent node is a {@link DataArray}, pos is the position
     * in such list. Otherwise it is unspecified.
     */    
    void visit(DataValue node, DataNode parent, String field, int pos);
}
