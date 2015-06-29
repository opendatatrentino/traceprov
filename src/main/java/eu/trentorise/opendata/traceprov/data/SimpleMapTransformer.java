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

import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Produces a simple HashMap/ArrayList version of an INode tree
 * @author David Leoni 
 */
class SimpleMapTransformer implements INodeVisitor {
    private LinkedList<Map.Entry> stack;    

    public SimpleMapTransformer() {
        stack = new LinkedList();
    }
        
    
    @Override
    public void visit(NodeMap nodeMap, INode parent, String key, int pos) {
        
        Map map = new HashMap();
        
        for (int i = 0; i < nodeMap.getValues().size(); i++){
            Map.Entry entry = stack.removeFirst();
            map.put(entry.getKey(), entry.getValue());
        }
        stack.addFirst(Maps.immutableEntry(key, map));
    }

    @Override
    public void visit(NodeList nodeList, INode parent, String key, int pos) {
        List ret = new ArrayList();
        
        for (int i = 0; i < Iterables.size(nodeList); i++){
            Map.Entry entry = stack.removeFirst();
            ret.add(entry.getValue());
        }
        Collections.reverse(ret);
        stack.addFirst(Maps.immutableEntry(key, ret));
        
    }

    @Override
    public void visit(NodeValue nodeValue, INode parent, String key, int pos) {
        stack.addFirst(Maps.immutableEntry(key, nodeValue.getValue()));
    }
    
    public Object getResult(){
        checkState(stack.size() == 1, "Stack should have only one element, found instead %s elements", stack.size());
        return stack.getFirst().getValue();
    }
}