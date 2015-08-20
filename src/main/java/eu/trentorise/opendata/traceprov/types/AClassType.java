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
package eu.trentorise.opendata.traceprov.types;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.traceprov.data.Data;
import java.util.List;
import org.immutables.value.Value;

/**
 * A special {@code type} to define complex ones. The class is loosely modeled
 * after Typescript classes. Notice that, differently from other types,
 * instances of this type all have their own id, name, description, ...etc
 * according to the class being modeled.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
// json ser???
abstract class AClassType extends Type {

    private static final long serialVersionUID = 1L;

    /**
     * The property definitions of the class
     */
    public abstract List<Def> getPropertyDefs();

    /**
     * The method definitions of the class TODO higly fuzzy thing
     */
    public abstract List<Def<FunctionType>> getMethodDefs();

    /**
     * The unique indexes tht may constrain sets of schema values to be unique
     * in the array they are in.
     *
     */
    public abstract List<UniqueIndex> getUniqueIndexes();

    @Value.Default
    @Override
    public Class getJavaClass() {
        return Data.class; // todo this is important I guess..
    }

    @Override
    public boolean isDirty(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    @Override
    public boolean isDegenerate(Object obj) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    @Override
    public boolean isEmpty(Object object) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    @Override
    public boolean isInstance(Object object) {
        throw new UnsupportedOperationException("todo implement me!");
    }

    @Value.Default
    @Override
    public Dict getDescription() {
        return Dict.of();
    }

    @Value.Default
    @Override
    public Dict getName() {
        return Dict.of();
    }

    @Value.Default
    @Override
    public Concept getConcept() {
        return Concept.of();
    }

    @Value.Default
    @Override
    public String getOriginId() {
        return "";
    }

    @Value.Default
    @Override
    public String getId() {
        return "";
    }

}
