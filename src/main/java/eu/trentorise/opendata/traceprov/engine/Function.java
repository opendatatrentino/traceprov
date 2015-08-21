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
package eu.trentorise.opendata.traceprov.engine;

import eu.trentorise.opendata.traceprov.data.DataNode;
import eu.trentorise.opendata.traceprov.types.FunctionType;

/**
 * TODO this is a super draft
 *
 * @author David Leoni
 */
public abstract class Function {

    public abstract String getNamespace();

    public abstract String getDescription();

    public abstract FunctionType getType();

    public abstract String getParamNames();

    /**
     * New nodes shouldn't link to previous nodes to prevent overloading
     * memory... at most they can point to previous values
     *
     */
    public abstract DataNode apply(Iterable<DataNode> args);
}
