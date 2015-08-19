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
package eu.trentorise.opendata.traceprov.experimental;

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.traceprov.types.Concept;
import eu.trentorise.opendata.traceprov.types.Def;
import eu.trentorise.opendata.traceprov.types.Type;

/**
 *
 * @author David Leoni
 */
class NamedType<T extends Type> extends Type {

    private Def<T> def;

    @Override
    public String getId() {
        return def.getId();
    }

    @Override
    public Concept getConcept() {
        return def.getConcept();
    }
/*
    public String getCanonicalName() {
        return def.getCanonicalName();
    }

    public Dict getName() {
        return def.getName();
    }

    public Dict getDescription() {
        return def.getDescription();
    }

    @Override
    public String getDatatypeStandardId() {
        return def.getType().getDatatypeStandardId();
    }
*/
    @Override
    public Class getJavaClass() {
        return def.getType().getJavaClass();
    }

}
