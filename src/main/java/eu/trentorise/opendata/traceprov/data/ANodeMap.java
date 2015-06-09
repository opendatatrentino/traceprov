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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.schema.Ref;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreeNode;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as=NodeMap.class)
@JsonDeserialize(as=NodeMap.class)
abstract  class ANodeMap extends ADataNode {

    private static final long serialVersionUID = 1L;
    
    @Value.Default
    @Value.Parameter
    @Override
    public Ref getProvenance(){
        return Ref.of();
    };           
        
    public abstract  List<Map.Entry<String, TreeNode>> getElements();
}
