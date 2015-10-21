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
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;
import eu.trentorise.opendata.traceprov.data.TraceNode;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import java.util.List;
import java.util.Map;
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
abstract class AClassType extends TraceType {

    private static final long serialVersionUID = 1L;

    /**
     * The property definitions of the class as map. The keys are camelcased
     * English short ids relative to this particular class, like
     * {@code workAddress}, and they are intended to be used in scripting
     * environments. The key does not necessarily needs to be equal to the last
     * part of {#link Def#getId() Def id}
     */
    public abstract Map<String, Def> getPropertyDefs();

    /**
     * Returns true if class has given property
     *
     * @see #getPropertyDef(java.lang.String)
     */
    public boolean hasPropertyDef(String propertyName) {
        checkNotEmpty(propertyName, "Invalid property name!");
        return getPropertyDefs().containsKey(propertyName);
    }

    /**
     * Returns given property, or throws exception if not found
     *
     * @throws eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException
     * @see #hasPropertyDef(java.lang.String)
     */
    public Def getPropertyDef(String propertyName) {
        checkNotEmpty(propertyName, "Invalid property name!");
        if (getPropertyDefs().containsKey(propertyName)) {
            return getPropertyDefs().get(propertyName);
        } else {
            throw new TraceProvNotFoundException("Couldn't find property " + propertyName);
        }
    }

    /**
     * The method definitions of the class TODO higly fuzzy thing
     */
    public abstract Map<String, Def<FunctionType>> getMethodDefs();

    /**
     * The unique indexes tht may constrain sets of schema values to be unique
     * in the array they are in.
     *
     */
    public abstract List<UniqueIndex> getUniqueIndexes();

    @Value.Default
    @Override
    public Class getJavaClass() {
        return TraceNode.class; // todo this is important I guess..
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
    public DefMetadata getMetadata() {
        return DefMetadata.builder()
                .setName(Dict.of())
                .setDescription(Dict.of())
                .setConcept(Concept.of())
                .setOriginId("")
                .build();
    }

    @Value.Default
    @Override
    public String getId() {
        return "";
    }

    @Value.Default
    public boolean isImmutable(){
	return false;
    }
}
